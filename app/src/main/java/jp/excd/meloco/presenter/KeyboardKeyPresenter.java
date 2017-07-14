package jp.excd.meloco.presenter;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by w-nishie on 2017/07/14.
 */

public class KeyboardKeyPresenter implements View.OnTouchListener {
    //クラス名(ログ出力用)
    String CLASS = getClass().getSimpleName();

    //Viewのid
    private int viewId;
    //音程
    private String note;
    //キーボードの表示段
    private String verticalPosition;
    //キーボードの表示段の定数
    public static final String LOWER = "LOWER";
    public static final String UPPER = "UPPER";
    //-----------------------------------------------------------
    // コンストラクタ
    // 第１引数：ViewのId
    // 第２引数：音程
    // 第３引数：キーボードの表示段数
    //-----------------------------------------------------------
    public KeyboardKeyPresenter(int viewId, String note, String verticalPosition) {

        Log.d(CLASS, "コンストラクタ実行");

        this.viewId = viewId;
        this.note = note;
        this.verticalPosition = verticalPosition;
    }
    //------------------------------------------------------------------
    // タッチリスナー
    //------------------------------------------------------------------
    public boolean onTouch(View v, MotionEvent event) {

        Log.d(CLASS, "onTouch");

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //押したときの動作
            //logを出力
            Log.d(CLASS,"押した");

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //離したときの動作
            Log.d(CLASS,"離した");
        }
        return false;
    }
    //------------------------------------------------------------------
    // viewIdのゲッター
    //------------------------------------------------------------------
    public int getViewId() {
        return viewId;
    }
}
