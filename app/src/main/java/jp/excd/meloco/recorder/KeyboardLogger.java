//--------------------------------------------------------------------------------------------------
// キーボード操作のロギング
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.recorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.excd.meloco.utility.WLog;

public class KeyboardLogger {

    //キーボードロガーのセマッフォー
    public static Object lock = new Object();
    //キーボード操作のレコード
    private List<KeyboardLoggerRecord> keyboardLoggerRecordList = new ArrayList<KeyboardLoggerRecord>();

    //----------------------------------------------------------------------------------------------
    // 名称    ：ノートオン
    // 処理概要：キーボードの押下を記録する。
    // 引数１  ：音程
    //----------------------------------------------------------------------------------------------
    public void noteOn(String pitch) {
        WLog.d(this, "noteOn");
        WLog.d(this, "pitch=" + pitch);

        // 日付の取得
        Date date = new Date();

        // キーボードレコードの生成
        KeyboardLoggerRecord keyboardLoggerRecord = new KeyboardLoggerRecord(KeyboardLoggerRecord.TYPE_NOTE_ON, pitch);

        //------------------------------------------------------------------------------------------
        // 記録
        // 記録時には、ロックをする。
        //------------------------------------------------------------------------------------------
        synchronized (KeyboardLogger.lock) {
            this.keyboardLoggerRecordList.add(keyboardLoggerRecord);
        }
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：ノートオフ
    // 処理概要：キーボードの押下を記録する。
    // 引数１  ：音程
    //----------------------------------------------------------------------------------------------
    public synchronized void noteOff(String pitch) {
        WLog.d(this, "noteOn");
        WLog.d(this, "pitch=" + pitch);

        // 日付の取得
        Date date = new Date();

        // キーボードレコードの生成
        KeyboardLoggerRecord keyboardLoggerRecord = new KeyboardLoggerRecord(KeyboardLoggerRecord.TYPE_NOTE_OFF, pitch);

        //------------------------------------------------------------------------------------------
        // 記録
        // 記録時には、ロックをする。
        //------------------------------------------------------------------------------------------
        synchronized (KeyboardLogger.lock) {
            this.keyboardLoggerRecordList.add(keyboardLoggerRecord);
        }
    }
}
