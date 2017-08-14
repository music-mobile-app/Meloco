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
    private WaveManager waveManager = null;

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
                AudioConfig.STREAM_TYPE,
                AudioConfig.SAMPLE_RATE,
                AudioConfig.CHANNEL_CONFIG,
                AudioConfig.AUDIO_FORMAT,
                bufferSizeInByte,    //←バッファサイズを、フォーマット上の最小値に設定
                AudioTrack.MODE_STREAM);

        WLog.d(this,"AudioTrack.play()");
        this.track.play();

    }
    //----------------------------------------------------------------------------------------------
    // インスタンス化
    // 引数１ :WaveManager(波形管理) このAudioTrackWrapperは、ここで渡されたWaveManagerのインスタンスと
    //         会話し、次に発音すべき波形を取得する。
    //----------------------------------------------------------------------------------------------
    public static synchronized AudioTrackWrapper getInstance(WaveManager waveManager) {
        if (me == null) {
            //最初の１度だけインスタンスを生成する。
            me = new AudioTrackWrapper(waveManager);

            WLog.d("MAX_PRIORIT");
            me.setPriority(Thread.MAX_PRIORITY);
        }
        //すでにインスタンスがある場合は、WaveManagerの参照だけを入れ替える。
        me.setWaveManager(waveManager);
        return me;
    }
    //----------------------------------------------------------------------------------------------
    // インスタンス破棄
    // 概要  ：自分自身の参照を破棄し、ガベージコレクションの対象とする。
    //         このメソッドは、AudioTrackの停止後に呼び出す。
    //----------------------------------------------------------------------------------------------
    public static synchronized void destractor() {
        me = null;
    }
    //----------------------------------------------------------------------------------------------
    // 初期状態確認
    // 概要  ：このクラスが初期化された状態かどうかを返却する。
    //         AudioTrackWrapperの再起動をする場合は、このメソッドがtrueを返却することを確認してから
    //         起動する。
    //----------------------------------------------------------------------------------------------
    public static synchronized boolean isInitial() {
        if (me == null) {
            return true;
        }
        return false;
    }
    //----------------------------------------------------------------------------------------------
    // 波形管理オブジェクトの設定
    //----------------------------------------------------------------------------------------------
    private synchronized void setWaveManager(WaveManager waveManager) {
        this.waveManager = waveManager;
    }
    //----------------------------------------------------------------------------------------------
    // スレッド処理
    // 概要：強制終了フラグが、設定されるまで無限ループする。
    //       強制終了フラグは波形管理オブジェクトより渡される。
    //----------------------------------------------------------------------------------------------
    public void run() {

        //WLog.d(this, "priority=" + AudioConfig.AUDIO_TRACK_WRAPPER_PRIORITY);
        //android.os.Process.setThreadPriority(AudioConfig.AUDIO_TRACK_WRAPPER_PRIORITY);

        boolean loopFlg = true;
        while(loopFlg) {

            //-------------------------------------------------------------------------------------
            // 発音処理
            // このメソッドは、波形データが取得できてスピーカに送ったか、
            // 強制終了フラグが設定されている場合に返ってくる。
            //-------------------------------------------------------------------------------------
            boolean audioTrackToStop = mainLoop();

            //--------------------------------------------------------------------------------------
            // 強制終了フラグが設定されていたら、ループ終了
            //--------------------------------------------------------------------------------------
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
    // 処理概要:波形管理オブジェクトより、次に発音すべき波形を取得し、スピーカへ送る。
    //          波形管理オブジェクトが、次に発音すべき波形を持たない場合は、
    //          wait()による待ち合わせをする。
    // 戻り値  :波形管理オブジェクトが強制終了を設定している場合は、その値を返却する。
    //----------------------------------------------------------------------------------------------
    public boolean mainLoop() {

        //強制終了フラグ
        boolean ret = false;

        //次の波形データ
        byte[] nextData8bit = null;
        short[] nextData16bit = null;

        //ループフラグ
        boolean loopFlg = true;

        //データ存在フラグ(次のデータが存在している場合,true)
        boolean dataIs = false;

        //------------------------------------------------------------------------------
        // 波形データが取得できるまで繰り返す。
        //------------------------------------------------------------------------------
        while (loopFlg) {

            //次の波形データのロックを取得した状態で処理を行う。
            synchronized(WaveManager.nextDataLock) {

                //--------------------------------------------------------------------------------
                //波形データの取得
                //--------------------------------------------------------------------------------
                if (AudioConfig.AUDIO_FORMAT == AudioFormat.ENCODING_PCM_8BIT) {
                    //8ビットの場合バイト配列を取得
                    nextData8bit = this.waveManager.getNextData8bit();
                    if (nextData8bit != null) {
                        dataIs = true;
                    }
                } else {
                    nextData16bit = this.waveManager.getNextData16bit();
                    if (nextData16bit != null) {
                        dataIs = true;
                    }
                }
                //--------------------------------------------------------------------------------
                //波形データが取得できていない場合
                //--------------------------------------------------------------------------------
                if (dataIs == false) {
                    //------------------------------------------------------------------------------
                    // 強制終了フラグがたっている場合は、待ち合わせしない。
                    //------------------------------------------------------------------------------
                    if (waveManager.audioTrackToStop) {
                        //強制終了フラグを上位へ伝達する。
                        ret = waveManager.audioTrackToStop;
                        //ループ終了
                        loopFlg = false;
                    } else {
                        //------------------------------------------------------------------------------
                        // 待ち合わせ
                        //------------------------------------------------------------------------------
                        WLog.d(this, "波形データが取得できないのでwait");
                        try {
                            //この時点でロックは開放されている。
                            WaveManager.nextDataLock.wait();
                        } catch (InterruptedException e) {
                            WLog.d(this, "WaveManagerより波形データ更新連絡あり");
                        }
                    }
                } else {
                    //--------------------------------------------------------------------------------
                    //波形データが取得できている場合
                    //--------------------------------------------------------------------------------
                    // ループを終了する。
                    loopFlg = false;
                    // 波形データを取得できていることを、WaveManagerに伝える。
                    this.waveManager.setAllreadyOff();
                    // WaveManagerがallready待ちであればそれを解消する。
                    WaveManager.nextDataLock.notifyAll();
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
        return ret;
    }
}
