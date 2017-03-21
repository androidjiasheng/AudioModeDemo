package com.example.administrator.audiomodedemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;

/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：1.1.0
 * 创建日期：2016/11/24
 * 描    述:音频听筒扬声器切换控制器
 * ================================================
 */
public class AudioModeManger {

    private AudioManager audioManager;
    private SensorManager sensorManager;
    private Sensor mProximiny;
    private onSpeakerListener mOnSpeakerListener;

    /**
     * 扬声器状态监听器
     * 如果要做成类似微信那种切换后重新播放音频的效果，需要这个监听回调
     * isSpeakerOn 扬声器是否打开
     */
    public interface onSpeakerListener{
        void onSpeakerChanged(boolean isSpeakerOn);
    }


    public void setOnSpeakerListener(onSpeakerListener listener){
        if (listener != null){
            mOnSpeakerListener = listener;
        }
    }

    public AudioModeManger(){

    }

    /**
     * 距离传感器监听者
     */
    private SensorEventListener mDistanceSensorListener = new SensorEventListener() {

        /**
         ** <ul>
         * <li> values[0]: Acceleration minus Gx on the x-axis </li>
         * <li> values[1]: Acceleration minus Gy on the y-axis </li>
         * <li> values[2]: Acceleration minus Gz on the z-axis </li>
         * </ul>
         */
        @Override
        public void onSensorChanged(SensorEvent event) {
            float f_proximiny = event.values[0];
            //扬声器模式
            //魅蓝E传感器得到的值竟然比最大值都要大？what fuck ？
            if (f_proximiny >= mProximiny.getMaximumRange()) {

                setSpeakerPhoneOn(true);
                if (mOnSpeakerListener != null){
                    mOnSpeakerListener.onSpeakerChanged(true);
                }

            } else {//听筒模式

                setSpeakerPhoneOn(false);

                if (mOnSpeakerListener != null){
                    mOnSpeakerListener.onSpeakerChanged(false);
                }
            }
        }
//      返回传感器的种类 和 精确值
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };



    /**
     * 注册距离传感器监听
     */
    public void register(){
//      音频管理
        audioManager = (AudioManager) App.getInstance()
                .getSystemService(Context.AUDIO_SERVICE);
//      传感器的管理类
        sensorManager = (SensorManager) App.getInstance()
                .getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null && mDistanceSensorListener != null) {
//                        返回默认的传感器跟如果存在的的请求类型 （前提是存在的 并且是有权限的） 否则 返回的是控

//            Sensor.TYPE_PROXIMITY
//                    接近感应检测
            mProximiny = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
//            注册感应器的回掉方法
            sensorManager.registerListener(mDistanceSensorListener, mProximiny,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

    }

    /**
     * 取消注册距离传感器监听
     */
    public void unregister(){

        if (sensorManager != null &&mDistanceSensorListener != null ) {
            sensorManager.unregisterListener(mDistanceSensorListener);
        }

    }


    /**
     adjustVolume(int direction, int flags)：
     控制手机音量,调大或者调小一个单位,根据第一个参数进行判断
     AudioManager.ADJUST_LOWER,可调小一个单位;
     AudioManager.ADJUST_RAISE,可调大一个单位
     adjustStreamVolume(int streamType, int direction, int flags)：
     同上,不过可以选择调节的声音类型
     1）streamType参数,指定声音类型,有下述几种声音类型:
     STREAM_ALARM：手机闹铃 STREAM_MUSIC：手机音乐
     STREAM_RING：电话铃声 STREAM_SYSTEAM：手机系统
     STREAM_DTMF：音调 STREAM_NOTIFICATION：系统提示
     STREAM_VOICE_CALL:语音电话
     2）第二个参数和上面那个一样,调大或调小音量的
     3）可选的标志位,比如AudioManager.FLAG_SHOW_UI,显示进度条,AudioManager.PLAY_SOUND:播放声音
     setStreamVolume(int streamType, int index, intflags)：直接设置音量大小
     getMode( )：返回当前的音频模式
     setMode( )：设置声音模式
     有下述几种模式:
     MODE_NORMAL(普通), MODE_RINGTONE(铃声),
     MODE_IN_CALL(打电话)，MODE_IN_COMMUNICATION(通话)
     getRingerMode( )：返回当前的铃声模式
     setRingerMode(int streamType):设置铃声模式
     有下述几种模式:
     如RINGER_MODE_NORMAL（普通）、RINGER_MODE_SILENT（静音）、RINGER_MODE_VIBRATE（震动）
     getStreamVolume(int streamType)：
     获得手机的当前音量,最大值为7,最小值为0,当设置为0的时候,会自动调整为震动模式
     getStreamMaxVolume(int streamType)：获得手机某个声音类型的最大音量值
     setStreamMute(int streamType,boolean state)：将手机某个声音类型设置为静音
     setSpeakerphoneOn(boolean on)：设置是否打开扩音器
     setMicrophoneMute(boolean on)：设置是否让麦克风静音
     isMicrophoneMute()：判断麦克风是否静音或是否打开
     isMusicActive()：判断是否有音乐处于活跃状态
     isWiredHeadsetOn()：判断是否插入了耳机



     abandonAudioFocus(AudioManager.OnAudioFocusChangeListenerl)：放弃音频的焦点
     adjustSuggestedStreamVolume(int,int suggestedStreamType intflags)：
     调整最相关的流的音量，或者给定的回退流
     getParameters(String keys)：给音频硬件设置一个varaible数量的参数值
     getVibrateSetting(int vibrateType)：返回是否该用户的振动设置为振动类型
     isBluetoothA2dpOn()：检查是否A2DP蓝牙耳机音频路由是打开或关闭
     isBluetoothScoAvailableOffCall()：显示当前平台是否支持使用SCO的关闭调用用例
     isBluetoothScoOn()：检查通信是否使用蓝牙SCO
     loadSoundEffects()：加载声音效果
     playSoundEffect((int effectType, float volume)：播放声音效果
     egisterMediaButtonEventReceiver(ComponentName eventReceiver)：
     注册一个组件MEDIA_BUTTON意图的唯一接收机
     requestAudioFocus(AudioManager.OnAudioFocusChangeListener l,int streamType,int durationHint)
     请求音频的焦点
     setBluetoothScoOn(boolean on)：要求使用蓝牙SCO耳机进行通讯
     startBluetoothSco/stopBluetoothSco()()：启动/停止蓝牙SCO音频连接
     unloadSoundEffects()：卸载音效
     */

    /**
     * 听筒、扬声器切换
     *
     * 注释： 敬那些年踩过的坑和那些网上各种千奇百怪坑比方案！！
     *
     * AudioManager设置声音类型有以下几种类型（调节音量用的是这个）:
     *
     * STREAM_ALARM 警报
     * STREAM_MUSIC 音乐回放即媒体音量
     * STREAM_NOTIFICATION 窗口顶部状态栏Notification,
     * STREAM_RING 铃声
     * STREAM_SYSTEM 系统
     * STREAM_VOICE_CALL 通话
     * STREAM_DTMF 双音多频,不是很明白什么东西
     *
     * ------------------------------------------
     *
     * AudioManager设置声音模式有以下几个模式（切换听筒和扬声器时setMode用的是这个）
     *
     * MODE_NORMAL 正常模式，即在没有铃音与电话的情况
     * MODE_RINGTONE 铃响模式
     * MODE_IN_CALL 接通电话模式 5.0以下
     * MODE_IN_COMMUNICATION 通话模式 5.0及其以上
     *
     * @param on
     */
    private void setSpeakerPhoneOn(boolean on) {

        if (on) {
//            使用扩音器
            audioManager.setSpeakerphoneOn(true);
//            设置声音模式 MODE_NORMAL(普通)
            audioManager.setMode(AudioManager.MODE_NORMAL);
            //设置音量，解决有些机型切换后没声音或者声音突然变大的问题
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FX_KEY_CLICK);

        } else {
//            不用扩音器
            audioManager.setSpeakerphoneOn(false);

            //5.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                设置模式   MODE_IN_COMMUNICATION(通话)
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                //设置音量，解决有些机型切换后没声音或者声音突然变大的问题
                audioManager.setStreamVolume(
                        AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.FX_KEY_CLICK);

            } else {
//                 设置模式 MODE_IN_CALL(打电话)
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setStreamVolume(
                        AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.FX_KEY_CLICK);
            }
        }

    }

}
