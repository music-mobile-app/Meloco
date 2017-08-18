//--------------------------------------------------------------------------------------------------
// ログラッパー
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.utility;

import android.util.Log;
import jp.excd.meloco.utility.CommonUtil;

public class WLog {

    //ログの出力有無(true:あり、false:なし)
    public static boolean on = false;
    //public static boolean on = true;

    //----------------------------------------------------------------------------------------------
    // デバッグログのラッパー
    //   ログ出力が、Audio処理の波形欠落を引き起こすため、任意のタイミングでオフにすることが
    //   できるようにしておく。
    //   第１引数：ログ出力を行うインスタンス
    //   第２引数：出力ログ文字列
    //----------------------------------------------------------------------------------------------
    public static void d(Object o, String s) {
        if (on) {
            Log.d(CommonUtil.tag(o), s);
        }
    };
    //----------------------------------------------------------------------------------------------
    // デバッグログのラッパー
    //   第引数：出力ログ文字列
    //----------------------------------------------------------------------------------------------
    public static void d(String s) {
        if (on) {
            Log.d(CommonUtil.tag(), s);
        }
    };

}
