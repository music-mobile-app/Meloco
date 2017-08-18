//--------------------------------------------------------------------------------------------------
// 録音ボタンのプレゼンター
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.presenter;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import jp.excd.meloco.R;
import jp.excd.meloco.recorder.RecordingManager;
import jp.excd.meloco.sample.AudioThread001;
import jp.excd.meloco.utility.WLog;

public class ButtonRecPresenter implements View.OnClickListener {

    // 呼び出し元のアクティビティー
    private Activity parentActivity = null;

    // 再生ボタンのプレゼンター
    private ButtonPlayPresenter buttonPlayPresenter = null;

    // ボタンの状態
    private int buttonStatus = 1;
    //----------------------------------------------------------------------------------------------
    // ボタンの状態の定数
    //----------------------------------------------------------------------------------------------
    // 通常の状態
    public static final int BUTTON_STATUS_NORMAL = 1;
    // 実行中(停止ボタンとしての振る舞い)
    public static final int BUTTON_STATUS_TO_STOP = 2;
    // 非活性(押下できない状態)
    public static final int BUTTON_STATUS_NOT_AVAILABLE = 3;

    //----------------------------------------------------------------
    // リスナー登録
    //----------------------------------------------------------------
    public void setListner(Activity activity, ButtonPlayPresenter buttonPlayPresenter) {

        WLog.d(this, "setListner実行");

        // 呼び出し元のActivityの保存
        parentActivity = activity;

        // 再生ボタンのプレゼンターを保存
        this.buttonPlayPresenter = buttonPlayPresenter;

        //ボタンのインスタンスを取得
        Button b = (Button)parentActivity.findViewById(R.id.buttonRec);

        //clickのリスナー登録
        b.setOnClickListener(this);
    }
    //----------------------------------------------------------------
    // ボタン押下時の処理
    //----------------------------------------------------------------
    public void onClick(View view) {
        WLog.d(this, "onClick");

        if (this.buttonStatus == BUTTON_STATUS_NORMAL) {
            // 通常状態でボタンを押下された場合は、録音開始
            this.startRec();
        } else if(this.buttonStatus == BUTTON_STATUS_TO_STOP) {
            // 録音状態でボタンを押下された場合は、録音停止
            this.stopRec();
        } else {
            // 非活性の場合は何もしない。
        }
    }
    //----------------------------------------------------------------
    // ボタン押下時の処理（録音開始)
    //----------------------------------------------------------------
    private void startRec() {
        WLog.d(this, "startRec()");

        // 録音ボタンをストップボタンに切り替える。
        Button b = (Button)parentActivity.findViewById(R.id.buttonRec);
        b.setBackgroundResource(R.drawable.button_rec_stop);

        // ボタンの状態を変更
        this.buttonStatus = BUTTON_STATUS_TO_STOP;

        // 再生ボタンを非活性化する。
        this.buttonPlayPresenter.toNotAvailable();

        // レコーディングを開始する。
        RecordingManager.startRecording();

    }
    //----------------------------------------------------------------
    // ボタン押下時の処理（録音停止)
    //----------------------------------------------------------------
    private void stopRec() {
        WLog.d(this, "stopRec()");

        // 録音ボタンを通常に戻す。
        Button b = (Button) parentActivity.findViewById(R.id.buttonRec);
        b.setBackgroundResource(R.drawable.button_rec);

        // 再生ボタンを通常に切り替える。
        this.buttonPlayPresenter.toNormal();

        // 状態を変更
        this.buttonStatus = BUTTON_STATUS_NORMAL;
    }
    //----------------------------------------------------------------
    //■公開メソッド
    //----------------------------------------------------------------
    //----------------------------------------------------------------
    // 非活性化
    //----------------------------------------------------------------
    public void toNotAvailable() {
        WLog.d(this, "toNotAvailable()");

        // 録音ボタンを非活性に切り替える。
        Button b = (Button)parentActivity.findViewById(R.id.buttonRec);
        b.setBackgroundResource(R.drawable.button_rec_not_available);

        // 状態を非活性に更新
        this.buttonStatus = BUTTON_STATUS_NOT_AVAILABLE;
    }
    //----------------------------------------------------------------
    // 元に戻す
    //----------------------------------------------------------------
    public void toNormal() {
        WLog.d(this, "toNormal()");

        // 録音ボタンを通常に戻す。
        Button b = (Button) parentActivity.findViewById(R.id.buttonRec);
        b.setBackgroundResource(R.drawable.button_rec);

        // 状態を元に戻す。
        this.buttonStatus = BUTTON_STATUS_NORMAL;
    }
}
