package com.example.administrator.audiomodedemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by jiasheng on 2017/3/20.
 * email 898478073@qq.com
 * Description: AudioModeDemo
 */

public class PlayMusic extends AppCompatActivity {
    private Button play;
    private Button stop;
    private boolean     isReleased      = false;
    private boolean     isPlaying       = false;

    private MediaPlayer mMediaPlayer = new MediaPlayer();
    //定义一个标志用来标示是否点击了静音按钮
    private int flag = 1;

    private AudioManager am;
    private Button addvoice;
    private Button reducevoice;
    private Button noVocie;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.reducevoice = (Button) findViewById(R.id.reducevoice);
        this.addvoice = (Button) findViewById(R.id.addvoice);
        play = (Button)findViewById(R.id.main_play);
        stop = (Button)findViewById(R.id.main_stop);
        noVocie = (Button) findViewById(R.id.novoice);
//        获取AudioManager
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.setSpeakerphoneOn(false);
                setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
                am.setMode(AudioManager.MODE_IN_CALL);
                try
                {
                    if ( !isPlaying )
                    {
                        /* 装载资源中的音乐 */
                        mMediaPlayer = MediaPlayer.create(PlayMusic.this, R.raw.music);
                        isPlaying = true;
                        isReleased = false;
                        /* 设置是否循环 */
                        mMediaPlayer.setLooping(true);
                        //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
                        try
                        {
                            mMediaPlayer.prepare();
                        }
                        catch (IllegalStateException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        mMediaPlayer.start();

                    }
                }
                catch (IllegalStateException e)
                {
                    e.printStackTrace();
                }
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                {
                    // @Override
                    public void onCompletion(MediaPlayer arg0)
                    {
                        mMediaPlayer.release();
                        am.setMode(AudioManager.MODE_NORMAL);
                    }
                });

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isPlaying&&!isReleased){
                    isReleased = true;
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    isPlaying = false;
                    am.setMode(AudioManager.MODE_NORMAL);
                }
            }
        });
    }
}
