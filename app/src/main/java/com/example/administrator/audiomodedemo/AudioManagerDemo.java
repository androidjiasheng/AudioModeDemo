package com.example.administrator.audiomodedemo;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by jiasheng on 2017/3/20.
 * email 898478073@qq.com
 * Description: AudioManager的使用
 */

public class AudioManagerDemo extends AppCompatActivity{
    private android.widget.Button reduce;
    private android.widget.Button add;
    int voiceInt = 0;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audiomanager);
        this.add = (Button) findViewById(R.id.add);
        this.reduce = (Button) findViewById(R.id.reduce);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
//                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });

        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
//                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }
        });
    }
}
