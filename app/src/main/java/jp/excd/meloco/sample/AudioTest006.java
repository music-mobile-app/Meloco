//--------------------------------------------------------------------------------------------------
// 16bitの音源ファイルを読み込んで音を鳴らすサンプル
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.sample;


import android.app.Activity;
import android.media.AudioTrack;

import java.io.IOException;
import java.io.InputStream;

import jp.excd.meloco.R;
import jp.excd.meloco.audio.engine.AudioConfig;
import jp.excd.meloco.utility.WLog;

public class AudioTest006  extends Thread {

    // オーディオトラック
    public static AudioTrack track = null;

    // ストップフラグ
    public static boolean toStop = false;

    // ヘッダサイズ
    public static int HEAD_SIZE = 80;

    // 現在、発音中の位置
    private int cursol = 0;

    // ボリューム
    public static int volume = 10000;

    // 目標ボリューム
    public static int toVolume = 10000;

    //サンプリングレート
    public static int sampleRate = 44100;

    //アクティビティーの受け取り
    private Activity activity = null;

    public static void play(Activity activity) {
        WLog.d("play()");
        //スレッドを生成
        AudioTest006 at = new AudioTest006(activity);

        //実行
        at.start();
    }
    public AudioTest006(Activity activity) {
        this.activity = activity;
    }

    public void run() {

        WLog.d("音源ファイルを読み込み音を鳴らす");

        //変数初期化
        this.cursol = 0;
        toStop = false;

        //AudioTrackの生成
        //------------------------------------------------------------------------------------------
        // オーディオトラックのインスタンス化
        //------------------------------------------------------------------------------------------
        //バッファの最小値を取得
        int bufferSizeInByte = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioConfig.CHANNEL_CONFIG,
                AudioConfig.AUDIO_FORMAT);
        WLog.d(this, "bufferSizeInByte=" + bufferSizeInByte);

        // AudioTrackをストリームモードで作成
        this.track = new AudioTrack(
                AudioConfig.STREAM_TYPE,
                sampleRate,
                AudioConfig.CHANNEL_CONFIG,
                AudioConfig.AUDIO_FORMAT,
                bufferSizeInByte,    //←バッファサイズを、フォーマット上の最小値に設定
                AudioTrack.MODE_STREAM);

        WLog.d(this, "AudioTrack.play()");
        this.track.play();

        //------------------------------------------------------------------------------------------
        // wavファイルを読み込む
        //------------------------------------------------------------------------------------------
        InputStream input;
        byte[] wavData = null;
        input = this.activity.getResources().openRawResource(R.raw.click2);
        try {
            wavData = new byte[(int) input.available()];
            input.read(wavData);
            input.close();
        } catch(IOException e) {
            WLog.d(this,"ファイル読み込み失敗");
        }
        //------------------------------------------------------------------------------------------
        // AudioTrackがshortを受け取るため、shortに直す。
        // Audioファイルは、リトルエイディアンのため、2byte目を上位、1byte目を下位ととして扱う。
        //------------【計算式】---------------
        //  ◆1byte目がプラスの場合
        //     2byte目 * 256 + 1byte目
        //  ◆1byte目がマイナスの場合
        //     2byte目 * 256 + 1byte目 + 256
        //-------------------------------------
        // ※同時に先頭のヘッダ部分を切り落とす。
        //------------------------------------------------------------------------------------------
        int size = (int)(wavData.length - HEAD_SIZE) / 2;
        short[] shorts = new short[size];
        for (int i = 0; i < shorts.length; i = i + 2) {
            //ターゲットバイトの読み込み
            byte b1 = wavData[i + HEAD_SIZE];
            byte b2 = wavData[i + HEAD_SIZE + 1];
            WLog.d(this,"b1=" + b1);
            WLog.d(this,"b2=" + b2);
            if (b1 < 0) {
                //マイナスの場合
                shorts[i] = (short)((b2 * 256) + (b1 + 256));
            } else {
                //プラスの場合
                shorts[i] = (short)((b2 * 256) + b1);
            }
            WLog.d(this,"shorts[i]=" + shorts[i]);

        }
        //-----------------------------------------------------------------------------------------
        // 発音ループ
        //-----------------------------------------------------------------------------------------
        while (!toStop) {
            short[] waves = null;
            int nokori = size - this.cursol;
            if (nokori < sampleRate) {
                waves = new short[nokori];
                toStop = true;
            } else {
                waves = new short[sampleRate];
            }
            for (int i = 0; i < waves.length; i++) {
                waves[i] = shorts[this.cursol];
                this.cursol = this.cursol + 1;
            }
            for (int i = 0; i < waves.length; i++) {
                WLog.d(this, "waves[i]=" + waves[i]);
            }
            //データの書き込み
            track.write(waves, 0, waves.length);
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
}
