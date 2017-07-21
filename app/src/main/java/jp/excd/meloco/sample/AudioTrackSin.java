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
    //音の長さ(秒数で指定(-1を設定すると、無限ループ)
    public int length = 1;
    //ストリームモードフラグ
    public boolean streamMode = false;

    //-------------------------------------------------------
    // コンストラクタ
    //  第１引数 length 音の長さを秒単位で指定。
    //-------------------------------------------------------
    public AudioTrackSin(int length) {
        Log.d(CommonUtil.tag(this),"length=" + length);
        this.length = length;
    }
    //-------------------------------------------------------
    // コンストラクタ
    //  第１引数 length 音の長さを秒単位で指定。
    //  第２引数 streamMode ストリームモードフラグ。
    //-------------------------------------------------------
    public AudioTrackSin(int length, boolean streamMode) {
        Log.d(CommonUtil.tag(this),"length=" + length);
        this.length = length;
        this.streamMode = streamMode;
    }
    //-------------------------------------------------------
    // 再生メソッド
    //  第１引数 length 音の長さを秒単位で指定。
    //-------------------------------------------------------
    public static void play(int length) {
        //スレッドを生成
        AudioTrackSin ads = new AudioTrackSin(length);
        //起動
        ads.start();
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
        byte[] sinWave = new byte[44100];

        double freq_c3 = 261.6256;
        double freq_e3 = 329.6276;
        double freq_g3 = 391.9954;
        double t = 0.0;
        double dt = 1.0 / 44100;

        for (int i = 0; i < sinWave.length; i++, t += dt) {
            double sum = Math.sin(2.0 * Math.PI * t * freq_c3)
                        + Math.sin(2.0 * Math.PI * t * freq_e3)
                        + Math.sin(2.0 * Math.PI * t * freq_g3);
            sinWave[i] = (byte) (Byte.MAX_VALUE * (sum/3));
        }
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
    }
    //-------------------------------------------------------
    // 停止メソッド
    //-------------------------------------------------------
    public static void stopPlay() {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        String className = Thread.currentThread().getStackTrace()[1].getClassName();

        if(track.getPlayState() == android.media.AudioTrack.PLAYSTATE_PLAYING){
            Log.d(CommonUtil.tag(),"データストップ");
            track.stop();
            track.reloadStaticData();
        }
        Log.d(CommonUtil.tag(),"トラックを削除");
        track = null;
    }
}
