//--------------------------------------------------------------------------------------------------
// キーボードロガーのレコード
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.recorder;

import java.util.Date;

public class KeyboardLoggerRecord{

    // キーボードの操作の種類のコード値
    public static final String TYPE_NOTE_ON = "1";
    public static final String TYPE_NOTE_OFF = "0";

    // キーボードの操作の種類
    private String type;

    // 押されたキーボードの音程
    private String pitch;

    // キーボードが押された時間
    private Date time;

    //----------------------------------------------------------------------------------------------
    // 名称    ：コンストラクタ
    // 処理概要：キーボード操作に関する情報を保存する。
    // 引数1   ：操作の種類
    // 引数2   ：音程
    //----------------------------------------------------------------------------------------------
    public KeyboardLoggerRecord(String type, String pitch) {
        this.type = type;
        this.pitch = pitch;
    }

    public String getType() {
        return type;
    }
    public String getPitch() {
        return pitch;
    }
    public Date getTime() {
        return time;
    }
}
