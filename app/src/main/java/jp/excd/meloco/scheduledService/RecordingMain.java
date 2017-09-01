//--------------------------------------------------------------------------------------------------
// レコーディングメインサービス
// 呼び出されるごとに、現在の経過時間を確認し適切な処理を行う。
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.scheduledService;

import java.util.Date;

import jp.excd.meloco.recorder.KeyboardLogger;

public class RecordingMain extends Thread {

    // 処理開始時刻
    private Date startDate;

    // キーボードロガー
    private KeyboardLogger keyboardLogger;

    //----------------------------------------------------------------------------------------------
    // 名称    ：コンストラクタ
    // 処理概要：必要な情報を保持する。
    // 引数１  ：処理開始時刻
    // 引数２  ：キーボードロガー
    //----------------------------------------------------------------------------------------------
    public RecordingMain(Date startDate, KeyboardLogger keyboardLogger) {
        this.startDate = startDate;
        this.keyboardLogger = keyboardLogger;
    }
    public void run() {
    }
}
