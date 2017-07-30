//--------------------------------------------------------------------------------------------------
// AudioTrackのラッパークラス
// 原則として、一度スレッド起動されたら走行し続ける。
// 波形データを作成するWaveManagerとは、別スレッドで動作する。
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.audio.engine;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import jp.excd.meloco.utility.CommonUtil;
import jp.excd.meloco.utility.WLog;

public class AudioTrackWrapper extends Thread{

    // 自分自身の参照
    private static AudioTrackWrapper me = null;

    // オーディオトラック
    private AudioTrack track = null;

    // 波形管理
    private WaveManager waveManager = null

    //----------------------------------------------------------------------------------------------
    // コンストラクタ
    //   private化し、外部から生成できないようにする。
    //----------------------------------------------------------------------------------------------
    private AudioTrackWrapper(WaveManager waveManager) {

        WLog.d("AudioTrackWrapper()コンストラクタ");

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
                AudioManager.STREAM_MUSIC,
                AudioConfig.SAMPLE_RATE,
                AudioConfig.CHANNEL_CONFIG,
                AudioConfig.AUDIO_FORMAT,
                bufferSizeInByte,    //←バッファサイズを、フォーマット上の最小値に設定
                AudioTrack.MODE_STREAM);
    }
    //----------------------------------------------------------------------------------------------
    // インスタンス化
    //----------------------------------------------------------------------------------------------
    public static synchronized AudioTrackWrapper getInstance(WaveManager waveManager) {
        if (me == null) {
            //最初の１度だけインスタンスを生成する。
            me = new AudioTrackWrapper(waveManager);
        }
        me.setWaveManager(waveManager);
        return me;
    }
    //----------------------------------------------------------------------------------------------
    // インスタンス破棄
    //----------------------------------------------------------------------------------------------
    public static synchronized void destractor() {
        me = null;
    }
    //----------------------------------------------------------------------------------------------
    // 音源管理オブジェクトの設定
    //----------------------------------------------------------------------------------------------
    private synchronized void setWaveManager(WaveManager waveManager) {
        this.waveManager = waveManager;
    }
    //----------------------------------------------------------------------------------------------
    // スレッド処理
    //  強制終了フラグが、設定されるまで無限ループする。
    //----------------------------------------------------------------------------------------------
    public void run() {

        boolean loopFlg = true;
        while(loopFlg) {

            // 発音処理
            mainLoop();

            //--------------------------------------------------------------------------------------
            // 強制終了フラグによるループフラグの更新
            //--------------------------------------------------------------------------------------
            boolean audioTrackToStop = waveManager.audioTrackToStop;
            if (audioTrackToStop) {
                loopFlg = false;
            }
        }
        //------------------------------------------------------------------------------------------
        // トラック開放
        //------------------------------------------------------------------------------------------
        track.stop();
        track.reloadStaticData();
        track.release();
        //------------------------------------------------------------------------------------------
        // 自分自身の初期化
        //------------------------------------------------------------------------------------------
        destractor();
    }
    //----------------------------------------------------------------------------------------------
    // メイン処理
    //----------------------------------------------------------------------------------------------
    public void mainLoop() {

        //次の波形データ
        byte[] nextData8bit = null;
        short[] nextData16bit = null;

        //ループフラグ
        boolean loopFlg = true;

        //データ存在フラグ
        boolean dataIs = false;

        //------------------------------------------------------------------------------
        // 波形データが取得できるまで繰り返す。
        //------------------------------------------------------------------------------
        while (loopFlg) {

            //次の波形データのロックを取得した状態で処理を行う。
            synchronized(WaveManager.nextDataLock) {

                boolean waitOn = false;
                //波形データの取得
                if (AudioConfig.AUDIO_FORMAT == AudioFormat.ENCODING_PCM_8BIT) {
                    //8ビットの場合バイト配列を取得
                    nextData8bit = this.waveManager.getNextData8bit();
                    //もし取得できなければ、待ち合わせ
                    if (nextData8bit == null) {
                        dataIs = true;
                    }
                } else {
                    nextData16bit = this.waveManager.getNextData16bit();
                    if (nextData16bit == null) {
                        dataIs = true;
                    }
                }
                if (dataIs) {
                    //------------------------------------------------------------------------------
                    // 強制終了の場合は、待ち合わせしない。
                    //------------------------------------------------------------------------------
                    if (waveManager.audioTrackToStop) {
                        //ループ終了
                        loopFlg = false;
                    } else {
                        //------------------------------------------------------------------------------
                        // 待ち合わせ
                        //------------------------------------------------------------------------------
                        WLog.d(this, "波形データが取得できないのでwait");
                        try {
                            //この時点でロックは開放されている。
                            wait();
                        } catch (InterruptedException e) {
                            WLog.d(this, "WaveManagerより波形データ更新連絡あり");
                        }
                    }
                } else {
                    //波形データが取得できている場合はループ終了
                    loopFlg = false;
                }
            }
        }
        //------------------------------------------------------------------------------
        // 波形データを使って、スピーカーへ送信
        //------------------------------------------------------------------------------
        if (dataIs) {
            if (AudioConfig.AUDIO_FORMAT == AudioFormat.ENCODING_PCM_8BIT) {
                track.write(nextData8bit, 0, nextData8bit.length);
            } else {
                track.write(nextData16bit, 0, nextData16bit.length);
            }
        }
    }
}
