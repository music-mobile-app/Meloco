package jp.excd.meloco.sample;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import jp.excd.meloco.audio.engine.AudioConfig;
import jp.excd.meloco.utility.WLog;

public class AudioTest003  extends Thread {

    // オーディオトラック
    public static AudioTrack track = null;

    // ストップフラグ
    public static boolean toStop = false;

    // 増分
    double t = 0.0;

    // ボリューム
    public static int volume = 32767;

    // 目標ボリューム
    public static int toVolume = 32767;

    public static void play() {
        WLog.d("play()");
        //スレッドを生成
        AudioTest003 at = new AudioTest003();

        //実行
        at.start();
    }
    public void run() {

        WLog.d("16ビット(STREAM)");

        //変数初期化
        t = 0.0;
        toStop = false;

        //AudioTrackの生成
        //------------------------------------------------------------------------------------------
        // オーディオトラックのインスタンス化
        //------------------------------------------------------------------------------------------
        //バッファの最小値を取得
        int bufferSizeInByte = AudioTrack.getMinBufferSize(
                AudioConfig.SAMPLE_RATE,
                AudioConfig.CHANNEL_CONFIG,
                AudioConfig.AUDIO_FORMAT);
        WLog.d(this,"bufferSizeInByte=" + bufferSizeInByte);

        // AudioTrackをストリームモードで作成
        this.track = new AudioTrack(
                AudioConfig.STREAM_TYPE,
                AudioConfig.SAMPLE_RATE,
                AudioConfig.CHANNEL_CONFIG,
                AudioConfig.AUDIO_FORMAT,
                bufferSizeInByte,    //←バッファサイズを、フォーマット上の最小値に設定
                AudioTrack.MODE_STREAM);

        WLog.d(this,"AudioTrack.play()");
        this.track.play();

        double freq_c3 = 261.6256;
        double dt = 1.0 / 44100;

        while(!toStop) {
            // サイン波（１秒分）
            short[] sinWave = new short[44100];

            //----------------------------------------------------------------------
            //単音版
            //----------------------------------------------------------------------
            for (int i = 0; i < sinWave.length; i++, t += dt) {
                double sum = Math.sin(2.0 * Math.PI * t * freq_c3);
                //sinWave[i] = (short) (Short.MAX_VALUE * (sum / 3));
                sinWave[i] = (short) (volume() * (sum / 3));
            }
            //データの書き込み
            track.write(sinWave, 0, sinWave.length);
        }
    }
    public static void playStop() {

        toStop = true;

        //2秒後に停止
        try {
            Thread.sleep(2000); //2000ミリ秒Sleepする
        } catch (InterruptedException e) {
            playStop();
        }

        WLog.d("AudioTrack開放");
        track.stop();
        track.reloadStaticData();
        track.release();
    }
    public static int volume() {
        //WLog.d("volume取得");
        if (volume > toVolume) {
            volume = volume - 1;
        } else if (volume < toVolume) {
            volume = volume + 1;
        }
        //WLog.d("volume取得volume=" + volume);
        return volume;
    }
}
