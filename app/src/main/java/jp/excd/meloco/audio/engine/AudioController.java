//------------------------------------------------------------------------------------
// オーディオコントローラー
// オーディオのIFを提供する。
//------------------------------------------------------------------------------------
package jp.excd.meloco.audio.engine;


import jp.excd.meloco.constant.SoundSourceType;
import jp.excd.meloco.utility.WLog;

public class AudioController {

    //波形管理オブジェクト
    private static WaveManager waveManager = null;

    //オーディオトラックラッパー
    private static AudioTrackWrapper audioTrackWrapper = null;

    //--------------------------------------------------------------------------------
    // オーディオ開始
    //--------------------------------------------------------------------------------
    public static void audioStart() {
        WLog.d("audioStart()");

        // 波形管理オブジェクトの生成
        waveManager = WaveManager.getInstance();

        // 波形管理オブジェクトのスレッドスタート
        waveManager.start();

        // オーディオトラックラッパー生成
        audioTrackWrapper = AudioTrackWrapper.getInstance(waveManager);

        // オーディオトラックラッパーのスレッドスタート
        audioTrackWrapper.start();

    }
    //--------------------------------------------------------------------------------
    // オーディオ再開
    //--------------------------------------------------------------------------------
    public static void audioReStart() {

        WLog.d("audioReStart()");

        // オーディオの停止
        waveManager.stopFlg = true;

        //---------------------------------------------------------------------------
        // きちんと停止したかどうかのチェック
        //---------------------------------------------------------------------------
        boolean flg = true;
        while(flg) {
            WLog.d("停止チェックループ");
            if (audioTrackWrapper.isInitial()) {
                //初期化完了
                flg = false;
            }
            //１秒待つ
            try {
                WLog.d("１秒待つ");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                WLog.d("１秒経過");
            }
        }
        //再起動
        audioStart();
    }
    //--------------------------------------------------------------------------------
    //   発音開始
    //   引数１　： soundSourceType 音源の分類
    //   引数２　： pitch 音程
    //   引数３　： volume 音量
    //   戻り値　：   割り振られた音源管理番号
    //----------------------------------------------------------------------------------------------
    public static synchronized String noteOn(SoundSourceType soundSourceType,
                                                String pitch,
                                                int volume) {

        WLog.d("noteOn");
        WLog.d("pitch=" + pitch);

        //----------------------------------------------------------------------------------------------
        // WaveManagerがインスタンス化されていなければ、インスタンス化する。
        //----------------------------------------------------------------------------------------------
        if (waveManager == null) {
            WLog.d("AudioStart");
            audioStart();
        }
        //アクティブな音源を追加
        String key = waveManager.addSoundSource(soundSourceType, pitch, volume);
        WLog.d("key=" + key);
        //追加された音源のキーを返却
        return key;
    }
    //----------------------------------------------------------------------------------------------
    // 発音終了
    // 処理概要：引数で与えられたキーに対応する音源の停止を設定する。
    //           すでに波形管理オブジェクトが存在しない場合、演奏は完了しているので、何もしない。
    // 引数１　： key 割り振られた音源管理番号
    //----------------------------------------------------------------------------------------------
    public static synchronized void noteOff(String key){
        WLog.d("noteOff");
        WLog.d("key=" + key);
        if (waveManager == null) {
            // 波形管理オブジェクトが存在しない場合、すでに停止状態なので、
            // 何もしない。
            return;
        }
        waveManager.deleteSoundSource(key);
    }
}
