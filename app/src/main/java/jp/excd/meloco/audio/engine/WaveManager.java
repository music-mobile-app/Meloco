//--------------------------------------------------------------------------------------------------
// 波形生成管理クラス
// 全ての波形を、このクラスのインスタンスで生成する。
// アクティブな音源の参照、更新は、同時にひとつのスレッドだけが行うことができる。
// このクラスのインスタンスは、AudioTrackWrapper生成時の引数となるため、
// このオブジェクトの方を先に生成する必要があり、
// このオブジェクトはAudioTrackWrapperの参照を持たないようにする。
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.audio.engine;

import android.media.AudioFormat;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.excd.meloco.audio.engine.ActiveNote;
import jp.excd.meloco.audio.source.Click2;
import jp.excd.meloco.constant.SoundSourceType;
import jp.excd.meloco.utility.CommonUtil;
import jp.excd.meloco.utility.WLog;
import jp.excd.meloco.audio.source.SineWave;

public class WaveManager extends Thread {
    //----------------------------------------------------------------------------------------------
    // 排他用オブジェクト
    //----------------------------------------------------------------------------------------------
    // 波形アクセス中
    // 同時にひとつのスレッドのみが、波形データへのアクセスを許される。
    // また、アクティブ音源がない場合、このオブジェクトをwait()する。
    // アクティブ音源を設定するスレッドは、アクティブな音源ができたら、notifyAll()する。
    public static Object waveDataAccess = new Object();

    // 次の波形データあり
    // 次の波形データの待ち合わせをするための、ロック用オブジェクト
    // 「AudioTrackWrapper」は、次の波形データがない場合、このオブジェクトをwait（）する。
    // 次の波形データが存在している場合は、notifyAllする。
    // 「WaveManager」は、次の波形データを更新できないとき（AudioTrackWrapperが処理中のとき、このオブジェクトをwait()する。
    // 次の波形データが更新できたとき、notifyAllする。
    public static Object nextDataLock = new Object();

    // 自分自身の参照
    private static WaveManager me = null;

    //----------------------------------------------------------------------------------------------
    // 発音中のデータ群
    // このデータ（内部に抱えているインスタンスを含む）にアクセスする場合には、
    // waveDataAccessをロックする。
    //----------------------------------------------------------------------------------------------
    // 現在鳴っている音源群
    private static Map<String, ActiveNote> activeNotes;

    // 現在発行済みのキー(このキーの初番は、このクラスをロックして行う。
    private static long currentKey = 0;

    //----------------------------------------------------------------------------------------------
    // 次に返却するデータ
    // このデータを更新する場合は「nextDataLock」をロックした状態で行う。
    //----------------------------------------------------------------------------------------------
    //次に返却するデータ(16bitの場合)
    private short[] nextData16bit;

    //次に返却するデータ(8bitの場合)
    private byte[] nextData8bit;

    //次に返却するデータの状態
    // このフラグがtrueの場合は、AudioWrapper側から取得可能な状態であることをあらわす。
    // 逆にこのflgがtrueの状態の場合は、AudioWrapper側が未処理であることを意味するため、
    // このオブジェクトは次のデータを設定できない。
    // 初期値はfalseで、WaveManger側から動作し、このフラグをtrueにする。
    private boolean allready = false;

    //----------------------------------------------------------------------------------------------
    // その他の状況
    //----------------------------------------------------------------------------------------------
    //終了フラグ（外部から強制終了させる場合にtrue)
    public boolean stopFlg = false;

    //AudioTrack停止フラグ
    public boolean audioTrackToStop = false;

    //----------------------------------------------------------------------------------------------
    // コンストラクタ
    //  プライベート化して、外部からインスタンス化できないようにする。
    //----------------------------------------------------------------------------------------------
    private WaveManager() {
        WLog.d("WaveManagerコンストラクタ");

        synchronized (waveDataAccess) {

            //発音中のデータの初期化
            activeNotes = new HashMap<String, ActiveNote>();
        }
    }
    //----------------------------------------------------------------------------------------------
    // インスタンス化
    //----------------------------------------------------------------------------------------------
    public static synchronized WaveManager getInstance() {
        if (me == null) {
            //最初の１度だけインスタンスを生成する。
            me = new WaveManager();

            WLog.d("waveManagerPriority=" + AudioConfig.WAVE_MANAGER_PRIORITY);
            me.setPriority(AudioConfig.WAVE_MANAGER_PRIORITY);

        }
        return me;
    }

    //----------------------------------------------------------------------------------------------
    // スレッド開始
    //----------------------------------------------------------------------------------------------
    public void run() {

        //WLog.d(this, "priority=" + AudioConfig.WAVE_MANAGER_PRIORITY);
        //android.os.Process.setThreadPriority(AudioConfig.WAVE_MANAGER_PRIORITY);

        WLog.d(this, "スレッド実行");

        //強制終了フラグたつまで、ループし続ける。
        while (this.stopFlg == false) {
            mainLoop();
        }
        WLog.d(this, "スレッド終了");

        WLog.d(this, "AudioWrapperにも、音源生成が終了したことを伝える。");
        this.audioTrackToStop = true;

        //WaveManagerからの通知待ちの可能性があるので、待ちの解消を行う。
        synchronized (nextDataLock) {
            WLog.d(this, "AudioTrackWrapperの待ちの解消");
            nextDataLock.notifyAll();
        }
        //自分自身を初期化
        me = null;
    }
    //----------------------------------------------------------------------------------------------
    // メイン処理
    // 処理概要：「activeNotes」より、波形データを生成し、AudioTrackWrapperが、取得できる状態にして、
    //           処理を終了する。
    //           「activeNotes」から有効な波形データが得られない場合は「activeNotes」の更新を待つ。
    //----------------------------------------------------------------------------------------------
    private void mainLoop() {

        WLog.d(this,"mainLoop()");

        //波形データの入れ物
        short[] shorts = null;

        synchronized (waveDataAccess) {

            //波形データの取得
            shorts = getNextData();

            //-------------------------------------------------------------------------------------
            // 波形データが取得できない場合は、待ち合わせ
            // この時点で「activeNotes」に問い合わせた結果、
            // 波形データが得られない場合は、「activeNotes」に、更新があるまで、
            // 待ち合わせを行う。
            // ただし強制終了フラグが立っている場合は、待ち合わせしない。
            // (永久に「activeNotes」が更新されるタイミングはこないから)
            //-------------------------------------------------------------------------------------
            if (shorts == null) {
                if (this.stopFlg == false) {
                    try {
                        WLog.d(this, "波形データが取得できないのでwait");
                        waveDataAccess.wait();
                    } catch (InterruptedException e) {
                        WLog.d(this, "AudioTrackWrapperより通知あり");
                    }
                }
            }
        }
        if ((shorts == null) || (shorts.length == 0)) {
            WLog.d(this, "波形データが取得できていないので、更新しない。");
        } else {
            WLog.d(this, "波形データをAudioTrackWrapperに伝える。");
            nextDataUpdate(shorts);
        }
    }

    //----------------------------------------------------------------------------------------------
    // 波形データの更新
    // 第１引数：更新する波形データ
    //           型は、short[]であるが、8bitエンコーディングの設定の場合には、
    //           byteの幅を越えないデータが設定されている。
    //----------------------------------------------------------------------------------------------
    private void nextDataUpdate(short[] shorts) {

        WLog.d(this, "nextDataUpdate()");

        boolean loopOn = true;

        while (loopOn) {
            //------------------------------------------------------------------------------------------
            // 波形データの更新
            //------------------------------------------------------------------------------------------
            synchronized (nextDataLock) {
                //--------------------------------------------------------------------------------------
                //次の波形データを受け入れ可能な状態かどうかを確認
                //--------------------------------------------------------------------------------------
                if (this.allready) {
                    //未処理の場合は、処理済みになるのを待つ。
                    try {
                        WLog.d(this, "未処理のため、処理済みになるのを待つ");
                        nextDataLock.wait();
                    } catch (InterruptedException e) {
                        WLog.d(this, "AudioTrackWrapperより通知あり");
                    }
                } else {
                    //受け入れ可能な場合は、更新
                    //8bitの場合、byteで返却
                    if (AudioConfig.AUDIO_FORMAT == AudioFormat.ENCODING_PCM_8BIT) {
                        this.nextData8bit = CommonUtil.from16to8(shorts);
                    } else {
                        this.nextData16bit = shorts;
                    }
                    //---------------------------------------------------------------------------------
                    // 状態の更新
                    // AudioWrapper側から取得可能な状態であることをあらわす。
                    // このflgがtrueの状態の場合は、このオブジェクトは次のデータを設定できない
                    //---------------------------------------------------------------------------------
                    this.allready = true;

                    //状態が更新されたことをAudioTrackWrapperに通知
                    nextDataLock.notifyAll();

                    //ループフラグのクリア
                    loopOn = false;
                }
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // 名称    ：次の波形データの取得
    // 処理概要：アクティブな音源から波形データを編集して返却する。
    // 注意点　：このメソッドは、waveDataAccessに対するロックを取得している状態で、
    //           呼び出されることを前提としている。
    //           ロックを獲得せずに呼び出すと、音源データの不整合で異常終了する。
    // 戻り値　：１ループバッファ分の波形データ
    //         　モノラル形式の場合は、配列数　＝　ループバッファ数
    //         　ステレオ形式の場合は、配列数　＝　ループバッファ数の２倍（左右のデータ交互に入る）
    //         　アクティブなデータが存在しない場合はnullを返却する。
    //----------------------------------------------------------------------------------------------
    private short[] getNextData() {

        WLog.d(this, "getNextData()");

        //------------------------------------------------------------------------------------------
        // アクティブな音源の存在確認
        //------------------------------------------------------------------------------------------
        if ((activeNotes == null) || (activeNotes.size() == 0)) {
            WLog.d(this, "アクティブな音源が存在しない");
            return null;
        }
        //------------------------------------------------------------------------------------------
        // 配列数の計算
        //------------------------------------------------------------------------------------------
        int size = AudioConfig.LOOP_BUFFER_SIZE;
        if (AudioConfig.CHANNEL_CONFIG == AudioFormat.CHANNEL_OUT_STEREO) {
            //２倍する。
            size = size * 2;
        }
        //------------------------------------------------------------------------------------------
        // 計算した波形の入れ物
        //------------------------------------------------------------------------------------------
        int[] waves = new int[size];

        //------------------------------------------------------------------------------------------
        // すべてのアクティブな音源から波形データを獲得して足し合わせる。
        //------------------------------------------------------------------------------------------
        // ループ内で、ノードを削除する（キーの増減がおこる）ので、
        // ループの前にキーの配列をコピーしておく。
        List<String> itemIdList = new ArrayList<String>(activeNotes.keySet());

        // キー配列をループ
        for(String key : itemIdList) {

            //実体
            ActiveNote activeNote = activeNotes.get(key);
            //波形の取得
            int[] targetWave = activeNote.getAndUpdateWaveData();

            //波形の加算処理
            waves = CommonUtil.addWave(waves, targetWave);

            //状況の確認
            boolean toEnd = activeNote.isEnd();
            if (toEnd) {
                //アクティブノードから削除
                activeNotes.remove(key);
            }
        }
        //------------------------------------------------------------------------------------------
        // int配列をshort配列の範囲まで圧縮する。
        //------------------------------------------------------------------------------------------
        short[] rets = CommonUtil.compressWaveData(waves);

        return rets;
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：キー発番号
    // 処理概要：音源管理キーの発番処理を行う。
    // 戻り値　：発番された音源管理番号
    //----------------------------------------------------------------------------------------------
    private static synchronized String getKey() {
        currentKey = currentKey + 1;
        String key = "" + currentKey;
        return key;
    }
    //---------------------------------------------------------------------------------------------
    // ■AudioTrackWrapper用の公開メソッド
    //---------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    // 名称    ：次の波形データの取得
    // 処理概要：アクティブな音源から波形データを編集して返却する。
    //           AudioTrackWrapperとのIFとなる。
    // 注意点  ：このメソッド呼び出し時には、「nextDataLock」のロックを取得しておく必要がある。
    // 戻り値　：１ループバッファ分の波形データ
    //----------------------------------------------------------------------------------------------
    public byte[] getNextData8bit() {

        WLog.d(this,"getNextData8bit()");

        if ((this.nextData8bit == null)||(this.nextData8bit.length == 0)) {
            return null;
        }
        //AudioTrackWrapperと処理を分離するため、コピーを渡す。
        byte[] ret = new byte[this.nextData8bit.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = this.nextData8bit[i];
        }
        return ret;
    }

    //----------------------------------------------------------------------------------------------
    // 名称    ：次の波形データの取得
    // 処理概要：アクティブな音源から波形データを編集して返却する。
    //           AudioTrackWrapperとのIFとなる。
    // 注意点  ：このメソッド呼び出し時には、「nextDataLock」のロックを取得しておく必要がある。
    // 戻り値　：１ループバッファ分の波形データ
    //----------------------------------------------------------------------------------------------
    public short[] getNextData16bit() {

        WLog.d(this,"getNextData16bit()");

        if ((this.nextData16bit == null)||(this.nextData16bit.length == 0)) {
            return null;
        }
        //AudioTrackWrapperと処理を分離するため、コピーを渡す。
        short[] ret = new short[this.nextData16bit.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = this.nextData16bit[i];
        }
        return ret;
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：波形準備済みフラグOFF
    // 処理概要：波形準備済みフラグをOFFにする。
    //           AudioTrackWrapperとのIFとなる。
    // 注意点  ：このメソッド呼び出し時には、「nextDataLock」のロックを取得しておく必要がある。
    //----------------------------------------------------------------------------------------------
    public void setAllreadyOff() {

        WLog.d(this,"setAllreadyOff()");

        this.allready = false;
        this.nextData8bit = null;
        this.nextData16bit = null;
    }
    //---------------------------------------------------------------------------------------------
    // ■音源操作用の公開メソッド
    //---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------
    // 音源追加
    // 処理概要：activeNotesに音源を追加して、notifyAllする。
    //           処理中は「waveDataAccess」をロックする。
    // 引数１  ：soundSourceType 音源の分類
    // 引数２　：pitch 音程
    // 引数３　：volume 音量
    // 戻り値  ：割り振られた音源管理番号
    //----------------------------------------------------------------------------------------------
    public String addSoundSource(SoundSourceType soundSourceType,
                                                String pitch,
                                                int volume) {
        WLog.d(this,"addSoundSource()");

        //キー情報
        String noteKey = getKey();

        //「waveDataAccess」のロック
        synchronized (this.waveDataAccess) {
            if (soundSourceType == SoundSourceType.SINE_WAVE) {
                //----------------------------------------------------------------------------------
                // サイン波
                //----------------------------------------------------------------------------------
                SineWave sineWave = new SineWave(pitch, volume);
                //ノート追加
                this.activeNotes.put(noteKey, sineWave);
            } else if (soundSourceType == SoundSourceType.CLICK2) {
                //----------------------------------------------------------------------------------
                // クリック音２
                //----------------------------------------------------------------------------------
                Click2 click2 = new Click2(volume);
                //ノート追加
                this.activeNotes.put(noteKey, click2);
            }
            //通知
            this.waveDataAccess.notifyAll();;
        }
        return noteKey;
    }
    //---------------------------------------------------------------------------------------------
    // 音源削除
    // 処理概要：activeNotesに音源を削除状態にして、notifyAllする。
    //           処理中は「waveDataAccess」をロックする。
    // 引数１  ：key 割り振られた音源管理番号
    //----------------------------------------------------------------------------------------------
    public void deleteSoundSource(String key) {

        WLog.d(this,"deleteSoundSource()");

        synchronized (this.waveDataAccess) {
            ActiveNote activeNote = this.activeNotes.get(key);
            if (activeNote != null) {
                activeNote.toEnd();
            }
            this.waveDataAccess.notifyAll();
        }
    }
}
