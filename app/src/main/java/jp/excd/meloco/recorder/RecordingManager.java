//--------------------------------------------------------------------------------------------------
// クラス名：録音管理クラス
// 概要    ：録音機能を提供する。
//           シングルトンで動作する。
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.recorder;

import jp.excd.meloco.sample.AudioThread001;
import jp.excd.meloco.utility.WLog;

public class RecordingManager {
    //自分自身
    private static RecordingManager me = null;

    //----------------------------------------------------------------------------------------------
    // コンストラクタ
    //----------------------------------------------------------------------------------------------
    private RecordingManager() {
    }
    //----------------------------------------------------------------------------------------------
    // 名称   ：インスタンス生成
    //----------------------------------------------------------------------------------------------
    public static synchronized RecordingManager getInstance() {
        if (me == null) {
            me = new RecordingManager();
        }
        return me;
    }
    //----------------------------------------------------------------------------------------------
    // 名称   ：レコーディング開始
    //----------------------------------------------------------------------------------------------
    public void startRec() {

        // TODO クリック音を鳴らし始める（テストコード)
        WLog.d(this, "click音を4回鳴らす。");
        AudioThread001 audioThread001 = new AudioThread001();
        audioThread001.start();
        //TODO　実装する。
    }
    //----------------------------------------------------------------------------------------------
    // ■公開メソッド
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    // 名称   ：レコーディング開始
    //----------------------------------------------------------------------------------------------
    public static void startRecording() {
        RecordingManager.getInstance();
        me.startRec();
    }
    //----------------------------------------------------------------------------------------------
    // 名称   ：レコーディング停止
    //----------------------------------------------------------------------------------------------
    public static void stopRecording() {
        // TODO 実装する。
    }
}