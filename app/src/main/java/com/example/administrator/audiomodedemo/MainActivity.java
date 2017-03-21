package com.example.administrator.audiomodedemo;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：1.1.0
 * 创建日期：2016/11/24
 * 描    述:为了好寻找，故意将AudioModeManger写在了activity中，
 * PlayLocalMusicController其实已经实现，不过注释掉了
 * ================================================
 */
public class MainActivity extends AppCompatActivity {

    private AudioModeManger audioModeManger;
    private TextView modelView;
    private TextView hintView;
    private static String PATH ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        modelView = (TextView) findViewById(R.id.model);
        hintView = (TextView) findViewById(R.id.hint);
        hintView.setMovementMethod(ScrollingMovementMethod.getInstance());
        PATH = Environment.getExternalStorageDirectory() + "/" + "music.mp3";
        addHint("onCreate");
        modelView.setText("手机型号:" + Build.BRAND + " " + Build.MODEL);
        addHint("Hi~ o(*￣▽￣*)ブ---音频转移中。。。");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    saveToSDCard("music.mp3");
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    addHint("音频转移失败"+throwable.getMessage());
                }finally {
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           addHint("Hi~ o(*￣▽￣*)ブ----音频转移完成！！！");
                       }
                   });
                }
            }
        }).start();


    }

    @Override
    protected void onStart() {
        super.onStart();
        addHint("onStart");
        if (audioModeManger == null)
            audioModeManger = new AudioModeManger();
        audioModeManger.register();
        audioModeManger.setOnSpeakerListener(new AudioModeManger.onSpeakerListener() {
            @Override
            public void onSpeakerChanged(boolean isSpeakerOn) {
                if (isSpeakerOn)
                addHint("远离距离感应器,打开扬声器" );
                else addHint("靠近距离感应器,打开听筒" );
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        addHint("onStop");
        if (audioModeManger != null)
            audioModeManger.unregister();
    }

    private void addHint(String hint){
        hintView.append(hint);
        hintView.append("\n");
    }


    public void onClick(View v) {
        if (v.getId() == R.id.play){
            Log.i("JayGoo", "onClick: "+PATH);
            PlayLocalMusicController.getInstance().playMusic(PATH, null, new PlayLocalMusicController.PlayMusicErrorListener() {
                @Override
                public void playMusicError() {
                    addHint("播放出错了");
                }
            });
        }

        if (v.getId() == R.id.stop){
            addHint("播放结束了");
            PlayLocalMusicController.getInstance().stopMediaPlayer();
        }
    }

    public void saveToSDCard(String name) throws Throwable {
        InputStream inStream = getResources().openRawResource(R.raw.music);
        File file = new File(Environment.getExternalStorageDirectory(), name);
        FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
        byte[] buffer = new byte[100];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int len = 0;
        while((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] bs = outStream.toByteArray();
        fileOutputStream.write(bs);
        outStream.close();
        inStream.close();
        fileOutputStream.flush();
        fileOutputStream.close();
        Log.i("JayGoo", "saveToSDCard: success! "+file.getAbsolutePath());
    }
}
