package jp.excd.meloco.presenter;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import jp.excd.meloco.utility.CommonUtil;
import jp.excd.meloco.sample.AudioTrackSin;

/**
 * Created by w-nishie on 2017/07/14.
 */

public class KeyboardKeyPresenter implements View.OnTouchListener {

    //Viewのid
    private int viewId;
    //音程
    private String note;
    //キーボードの表示段
    private String verticalPosition;
    //キーボードの表示段の定数
    public static final String LOWER = "LOWER";
    public static final String UPPER = "UPPER";
    //TODO ボタンの押下状態
    private boolean buttonOn = false;
    //TODO ボタンの押下回数
    private int buttonTouchCount = 0;
    //-----------------------------------------------------------
    // コンストラクタ
    // 第１引数：ViewのId
    // 第２引数：音程
    // 第３引数：キーボードの表示段数
    //-----------------------------------------------------------
    public KeyboardKeyPresenter(int viewId, String note, String verticalPosition) {

        Log.d(CommonUtil.tag(this), "コンストラクタ実行");

        this.viewId = viewId;
        this.note = note;
        this.verticalPosition = verticalPosition;
    }
    //------------------------------------------------------------------
    // タッチリスナー
    //------------------------------------------------------------------
    public boolean onTouch(View v, MotionEvent event) {

        Log.d(CommonUtil.tag(this), "onTouch");

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //押したときの動作
            //--------------------------------------------------------------------
            // 実験用のコード
            //--------------------------------------------------------------------
            Log.d(CommonUtil.tag(this),"押した");
            if (buttonOn) {
                //TODO サイン波を停止する。
                Log.d(CommonUtil.tag(this),"サイン波を停止する");
                AudioTrackSin.stopPlay();
                this.buttonOn = false;
            } else {
                //TODO サイン波を鳴らす。
                Log.d(CommonUtil.tag(this), "サイン波を鳴らす");
                //偶数会の場合は、ストリームモード
                this.buttonTouchCount = this.buttonTouchCount + 1;
                //ストリームモード
                Log.d(CommonUtil.tag(this), "ストリームモードで鳴らす");
                Log.d(CommonUtil.tag(this), "ノンストップモード");
                AudioTrackSin.play(this.buttonTouchCount, true,true);
                this.buttonOn = true;
            }

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //離したときの動作
            Log.d(CommonUtil.tag(this),"離した");
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
