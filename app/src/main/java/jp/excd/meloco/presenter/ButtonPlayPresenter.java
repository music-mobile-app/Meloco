//--------------------------------------------------------------------------------------------------
// 再生ボタンのプレゼンター
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.presenter;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import jp.excd.meloco.R;
import jp.excd.meloco.utility.CommonUtil;
import jp.excd.meloco.utility.WLog;

public class ButtonPlayPresenter implements View.OnClickListener {

    // 呼び出し元のアクティビティー
    private Activity parentActivity = null;

    // 録音ボタンのプレゼンター
    private ButtonRecPresenter buttonRecPresenter = null;

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
    public void setListner(Activity activity, ButtonRecPresenter buttonRecPresenter) {

        WLog.d(this, "setListner実行");

        // 呼び出し元のActivityの保存
        this.parentActivity = activity;

        // 録音ボタンのプレゼンター設定
        this.buttonRecPresenter = buttonRecPresenter;

        //ボタンのインスタンスを取得
        Button b = (Button)activity.findViewById(R.id.buttonPlay);

        //clickのリスナー登録
        b.setOnClickListener(this);
    }
    //----------------------------------------------------------------
    // ボタン押下時の処理
    //----------------------------------------------------------------
    public void onClick(View view) {
        WLog.d(CommonUtil.tag(this), "onClick");
        // TODO 画像をストップボタンに切り替える。
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
        Button b = (Button)parentActivity.findViewById(R.id.buttonPlay);
        b.setBackgroundResource(R.drawable.button_play_not_available);

        // 状態を非活性に更新
        this.buttonStatus = BUTTON_STATUS_NOT_AVAILABLE;
    }
    //----------------------------------------------------------------
    // 元に戻す
    //----------------------------------------------------------------
    public void toNormal() {
        WLog.d(this, "toNormal()");

        // 録音ボタンを通常に戻す。
        Button b = (Button) parentActivity.findViewById(R.id.buttonPlay);
        b.setBackgroundResource(R.drawable.button_play);

        // 状態を元に戻す。
        this.buttonStatus = BUTTON_STATUS_NORMAL;
    }
}
