//-----------------------------------------------------------------------
// サイン波のサンプル
//-----------------------------------------------------------------------
package jp.excd.meloco.sample;

import android.util.Log;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.AudioFormat;

import jp.excd.meloco.utility.CommonUtil;

public class AudioTrackSin extends Thread {

    //AnudioTrack
    public static AudioTrack track = null;
    //音の長さ(ループ回数)
    public int length = 1;
    //ストリームモードフラグ
    public boolean streamMode = false;
    //ループバッファ(１回にループさせる数
    public static int loopBuffer = 16;
    //ノンストップモード
    public static boolean nonStopMode = false;
    //追加データ
    public static int addData = 0;
    //ループフラグ
    public boolean onLoop = true;

    //-------------------------------------------------------
    // コンストラクタ
    //  第１引数 length 音の長さを秒単位で指定。
    //-------------------------------------------------------
    public AudioTrackSin(int length) {
        Log.d(CommonUtil.tag(this),"length=" + length);
        Log.d(CommonUtil.tag(this),"loopBuffer=" + loopBuffer);
        //１秒間にループできる回数
        int sizeFor1sec = 44100 / loopBuffer;
        //秒数をループ回数に直す。
        this.length = (sizeFor1sec * length);

    }
    //-------------------------------------------------------
    // コンストラクタ
    //  第１引数 length 音の長さを秒単位で指定。
    //  第２引数 streamMode ストリームモードフラグ。
    //-------------------------------------------------------
    public AudioTrackSin(int length, boolean streamMode) {
        Log.d(CommonUtil.tag(this),"length=" + length);
        Log.d(CommonUtil.tag(this),"loopBuffer=" + loopBuffer);
        //１秒間にループできる回数
        int sizeFor1sec = 44100 / loopBuffer;
        //秒数をループ回数に直す。
        this.length = (sizeFor1sec * length);
        this.streamMode = streamMode;
    }
    //-------------------------------------------------------
    // コンストラクタ
    //  第１引数 length 音の長さを秒単位で指定。
    //  第２引数 streamMode ストリームモードフラグ。
    //  第３引数 nonStopMode ノンストップモードフラグ。
    //-------------------------------------------------------
    public AudioTrackSin(int length, boolean streamMode, boolean nonStopMode) {
        Log.d(CommonUtil.tag(this),"nonStopMode=" + nonStopMode);
        Log.d(CommonUtil.tag(this),"length=" + length);
        Log.d(CommonUtil.tag(this),"loopBuffer=" + loopBuffer);
        //１秒間にループできる回数
        int sizeFor1sec = 44100 / loopBuffer;
        //秒数をループ回数に直す。
        this.length = (sizeFor1sec * length);
        this.streamMode = streamMode;
        nonStopMode = nonStopMode;
    }
    //-------------------------------------------------------
    // 再生メソッド
    //  第１引数 length 音の長さを秒単位で指定。
    //----------------------------------------------------
    public static void play(int length) {

        if(nonStopMode) {
            if (track == null) {
                //スレッドを生成
                AudioTrackSin ads = new AudioTrackSin(length);
                //起動
                ads.start();
            }
            //追加データ
            //１秒間にループできる回数
            int sizeFor1sec = 44100 / loopBuffer;
            //秒数をループ回数に直す。
            addData = (sizeFor1sec * length);
        } else {
            //スレッドを生成
            AudioTrackSin ads = new AudioTrackSin(length);
            //起動
            ads.start();
        }
    }
    //-------------------------------------------------------
    // 再生メソッド
    //  第１引数 length 音の長さを秒単位で指定。
    //  第２引数 streamMode ストリームモードフラグ。
    //-------------------------------------------------------
    public static void play(int length, boolean streamMode) {
        //スレッドを生成
        AudioTrackSin ads = new AudioTrackSin(length, streamMode);
        //起動
        ads.start();
    }
    //-------------------------------------------------------
    // 再生メソッド
    //  第１引数 length 音の長さを秒単位で指定。
    //  第２引数 streamMode ストリームモードフラグ。
    //  第３引数 nonStopMode AudioTrackを再利用するモード
    //-------------------------------------------------------
    public static void play(int length, boolean streamMode, boolean nonStopMode) {
        //スレッドを生成
        AudioTrackSin ads = new AudioTrackSin(length, streamMode, nonStopMode);
        //起動
        ads.start();
    }
    public void run() {
        //音を鳴らす処理
        if(track == null){
            if (this.streamMode) {
                //---------------------------------------------------
                // ストリームモードの場合
                //---------------------------------------------------
                Log.d(CommonUtil.tag(this),"ストリームモード8bit");

                //バッファの最小値を取得
                int bufferSizeInByte = AudioTrack.getMinBufferSize(
                        44100,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_8BIT);
                Log.d(CommonUtil.tag(this),"bufferSizeInByte=" + bufferSizeInByte);

                // AudioTrackをストリームモードで作成
                track = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        44100,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_DEFAULT,
                        //バッファサイズを、フォーマット上の最小値に設定
                        bufferSizeInByte,
                        AudioTrack.MODE_STREAM);
            } else {
                //---------------------------------------------------
                // スタティックモードの場合
                //---------------------------------------------------
                track = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        44100,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_DEFAULT,
                        44100,
                        AudioTrack.MODE_STATIC);
            }
        }

        // サイン波（１秒分）
        //byte[] sinWave = new byte[44100];
        //任意の回数
        byte[] sinWave = new byte[this.loopBuffer];

        double freq_c3 = 261.6256;
        double freq_e3 = 329.6276;
        double freq_g3 = 391.9954;
        double t = 0.0;
        double dt = 1.0 / 44100;
        /*
        for (int i = 0; i < sinWave.length; i++, t += dt) {
            double sum = Math.sin(2.0 * Math.PI * t * freq_c3)
                        + Math.sin(2.0 * Math.PI * t * freq_e3)
                        + Math.sin(2.0 * Math.PI * t * freq_g3);
            sinWave[i] = (byte) (Byte.MAX_VALUE * (sum/3));
        }
        */
        //--------------------------------------------------------------
        // ストリームモードの場合は、まず、起動
        //--------------------------------------------------------------
        if(this.streamMode) {
            Log.d(CommonUtil.tag(this),"起動");
            //PLAY
            track.play();
        }
        //----------------------------------------------
        //データ書き込み
        //  指定された秒数分繰り返す。
        //----------------------------------------------
        for (int i = 0; i < this.length; i++ ) {
            Log.d(CommonUtil.tag(this),"データ書き込み");
            //サイン波の合成
            Log.d(CommonUtil.tag(this),"サイン波の合成(c3のみ)");
            for (int j = 0; j < sinWave.length; j++, t += dt) {
                /*
                double sum = Math.sin(2.0 * Math.PI * t * freq_c3)
                        + Math.sin(2.0 * Math.PI * t * freq_e3)
                        + Math.sin(2.0 * Math.PI * t * freq_g3);
                sinWave[j] = (byte) (Byte.MAX_VALUE * (sum/3));
                */
                double d = Math.sin(2.0 * Math.PI * t * freq_c3);
                sinWave[j] = (byte)(Byte.MAX_VALUE * d);
            }
            //ここでブロック
            track.write(sinWave, 0, sinWave.length);
        }
        //--------------------------------------------------------------
        // ストリームモードでない場合は、データを設定してから起動
        //--------------------------------------------------------------
        if (!this.streamMode) {
            //PLAY
            Log.d(CommonUtil.tag(this),"起動");
            track.play();
        }
        //--------------------------------------------------------------
        // ノンストップモードの場合
        //--------------------------------------------------------------
        while(onLoop) {
            Log.d(CommonUtil.tag(this), "ノンストップモード");
            if (addData > 0) {
                for (int i = 0; i < addData; i++ ) {
                    Log.d(CommonUtil.tag(this),"データ書き込み");
                    //サイン波の合成
                    Log.d(CommonUtil.tag(this),"サイン波の合成(c3のみ)");
                    for (int j = 0; j < sinWave.length; j++, t += dt) {
                        double d = Math.sin(2.0 * Math.PI * t * freq_c3);
                        sinWave[j] = (byte) (Byte.MAX_VALUE * d);
                    }
                    //ここでブロック
                    track.write(sinWave, 0, sinWave.length);
                }
            }
            addData = 0;
            //スリープ
            try{
                Thread.sleep(10); //10ミリ秒Sleepする
            }catch(InterruptedException e){}
        }
    }
    //-------------------------------------------------------
    // 停止メソッド
    //-------------------------------------------------------
    public static void stopPlay() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String className = Thread.currentThread().getStackTrace()[1].getClassName();

        if (nonStopMode) {
           //ノンストップなら何もしない。

        } else {
            if (track.getPlayState() == android.media.AudioTrack.PLAYSTATE_PLAYING) {
                Log.d(CommonUtil.tag(), "データストップ");
                track.stop();
                track.reloadStaticData();
                Log.d(CommonUtil.tag(), "トラックを解放");
                track.release();
            }
            Log.d(CommonUtil.tag(), "トラックの参照を削除");
            track = null;
        }
    }
}
