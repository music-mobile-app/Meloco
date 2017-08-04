package jp.excd.meloco.presenter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import jp.excd.meloco.R;
import jp.excd.meloco.activity.MainActivity;
import jp.excd.meloco.activity.MenuActivity;
import jp.excd.meloco.utility.CommonUtil;

public class ButtonMenuPresenter implements OnClickListener {

    //呼び出し元のアクティビティーの保存Button
    private Activity parentActivity = null;

    //----------------------------------------------------------------
    // リスナー登録
    //----------------------------------------------------------------
    public void setListner(Activity activity) {

        Log.d(CommonUtil.tag(this), "setListner実行");

        //呼び出し元のActivityの保存
        parentActivity = activity;

        //ボタンのインスタンスを取得
        Button b = (Button)activity.findViewById(R.id.buttonMenu);

        //clickのリスナー登録
        b.setOnClickListener(this);
    }
    //----------------------------------------------------------------
    // ボタン押下時の処理
    //----------------------------------------------------------------
    public void onClick(View view) {
        Log.d(CommonUtil.tag(this), "onClick");

        //インテントの作成
        Intent intent = new Intent(parentActivity, MenuActivity.class);

        //----------------------------------------------------------------------------------------------
        //画面遷移
        //アニメーションさせる。(メニューボタンから出てくる感じ)
        //----------------------------------------------------------------------------------------------
        Button b = (Button)parentActivity.findViewById(R.id.buttonMenu);
        parentActivity.startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(parentActivity, b, "image").toBundle());
        //parentActivity.startActivity(intent);
    }

}
