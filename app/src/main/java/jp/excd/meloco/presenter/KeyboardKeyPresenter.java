package jp.excd.meloco.presenter;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import jp.excd.meloco.audio.AudioEngine;
import jp.excd.meloco.audio.engine.AudioController;
import jp.excd.meloco.constant.SoundSourceType;
import jp.excd.meloco.sample.AudioTrackSin;
import jp.excd.meloco.utility.CommonUtil;
import jp.excd.meloco.utility.WLog;

public class KeyboardKeyPresenter implements View.OnTouchListener {

    // Viewのid
    private int viewId;
    // 音程
    private String note;
    // キーボードの表示段
    private String verticalPosition;
    // キーボードの表示段の定数
    public static final String LOWER = "LOWER";
    public static final String UPPER = "UPPER";

    //TODO ボタンの押下状態
    private boolean buttonOn = false;
    //TODO ボタンの押下回数
    private int buttonTouchCount = 0;
    // 発音中の音源のキー
    private String activeNoteKey = "";
    //-----------------------------------------------------------
    // コンストラクタ
    // 第１引数：ViewのId
    // 第２引数：音程
    // 第３引数：キーボードの表示段数
    //-----------------------------------------------------------
    public KeyboardKeyPresenter(int viewId, String note, String verticalPosition) {


        WLog.d(this, "コンストラクタ実行");

        this.viewId = viewId;
        this.note = note;
        this.verticalPosition = verticalPosition;
    }
    //------------------------------------------------------------------
    // タッチリスナー
    //------------------------------------------------------------------
    public boolean onTouch(View v, MotionEvent event) {

        WLog.d(this, "onTouch");

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            WLog.d(this,"ACTION_DOWN");
            //押したときの動作
            //--------------------------------------------------------------------
            // 実験用のコード
            //--------------------------------------------------------------------
            WLog.d(this,"note=" + this.note);

            //発音
            this.activeNoteKey = AudioController.noteOn(SoundSourceType.SINE_WAVE, this.note, 100);
            //this.activeNoteKey = AudioEngine.noteOn(SoundSourceType.SINE_WAVE, this.note, 100);
            //AudioTrackSin.play(1, true);
            //AudioTrackSin.play16bit(1);

            //ACTION_UPも受け取る。
            return true;

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            WLog.d(this,"ACTION_UP");
            //離したときの動作
            AudioController.noteOff(this.activeNoteKey);
            //AudioEngine.noteOff(this.activeNoteKey);

        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            WLog.d(this,"ACTION_CANCEL");
            //離したときの動作
            AudioController.noteOff(this.activeNoteKey);
            //AudioEngine.noteOff(this.activeNoteKey);
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
