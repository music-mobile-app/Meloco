package jp.excd.meloco.sample;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import jp.excd.meloco.utility.WLog;

public class AudioTest002 extends Thread {

    public static AudioTrack track = null;

    public static void play() {
        WLog.d("play()");
        //スレッドを生成
        AudioTest002 at = new AudioTest002();

        //実行
        at.start();
    }
    public void run() {

        WLog.d("16ビット");

        track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                44100,
                AudioTrack.MODE_STATIC);

        // サイン波（１秒分）
        short[] sinWave = new short[44100];

        double freq_c3 = 261.6256;
        double freq_e3 = 329.6276;
        double freq_g3 = 391.9954;
        double t = 0.0;
        double dt = 1.0 / 44100;
        //----------------------------------------------------------------------
        //和音版
        //----------------------------------------------------------------------
        /*
        for (int i = 0; i < sinWave.length; i++, t += dt) {
            double sum = Math.sin(2.0 * Math.PI * t * freq_c3)
                    + Math.sin(2.0 * Math.PI * t * freq_e3)
                    + Math.sin(2.0 * Math.PI * t * freq_g3);
            sinWave[i] = (short) (Short.MAX_VALUE * (sum/3));
        }
        */
        //----------------------------------------------------------------------
        //単音版
        //----------------------------------------------------------------------
        for (int i = 0; i < sinWave.length; i++, t += dt) {
            double sum = Math.sin(2.0 * Math.PI * t * freq_c3);
            sinWave[i] = (short) (Short.MAX_VALUE * (sum/3));
        }
        //データの書き込み
        track.write(sinWave, 0, sinWave.length);

        //発音
        track.play();

        //2秒後に停止
        try {
            Thread.sleep(2000); //2000ミリ秒Sleepする
        } catch (InterruptedException e) {
            playStop();
        }
    }
    public static void playStop() {
        WLog.d("AudioTrack開放");
        track.stop();
        track.reloadStaticData();
        track.release();
    }
}
