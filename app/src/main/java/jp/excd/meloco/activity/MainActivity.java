package jp.excd.meloco.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import jp.excd.meloco.R;
import jp.excd.meloco.audio.source.Click2;
import jp.excd.meloco.constant.KeyboardType;


import jp.excd.meloco.presenter.ButtonPlayPresenter;
import jp.excd.meloco.presenter.ButtonRecPresenter;
import jp.excd.meloco.presenter.KeyboardPresenter;
import jp.excd.meloco.presenter.ButtonMenuPresenter;
import jp.excd.meloco.utility.CommonUtil;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {

    //--------------------------------------------------------------------------
    // onCreateメソッド
    // Activityが生成されるタイミングで呼び出される。
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();
    }
    //--------------------------------------------------------------------------
    // GUIの初期設定
    //--------------------------------------------------------------------------
    private void initGui() {

        Log.d(CommonUtil.tag(this),"initGui()実行");
        //----------------------------------------------------------------------
        // キーボードのリスナー登録
        //----------------------------------------------------------------------
        KeyboardPresenter keyboardPresenter = new KeyboardPresenter(KeyboardType.DOUBLE);
        keyboardPresenter.setListner(this);
        //----------------------------------------------------------------------
        // メニューのリスナー登録
        //----------------------------------------------------------------------
        ButtonMenuPresenter menuPresenter = new ButtonMenuPresenter();
        menuPresenter.setListner(this);
        //----------------------------------------------------------------------
        // 録音モードの設定
        // (初期表示を録音モードとする。)
        //----------------------------------------------------------------------
        initRecMode();
    }
    //--------------------------------------------------------------------------
    // 録音モードのGUI初期設定
    //--------------------------------------------------------------------------
    private void initRecMode() {
        //----------------------------------------------------------------------
        // クリック音の音源読み込み
        //----------------------------------------------------------------------
        Click2.sourceFileLoad(this);
        //----------------------------------------------------------------------
        // 再生ボタンおよび録音ボタンのリスナー設定
        //----------------------------------------------------------------------
        ButtonPlayPresenter playPresenter = new ButtonPlayPresenter();
        ButtonRecPresenter recPresenter = new ButtonRecPresenter();
        playPresenter.setListner(this,recPresenter);
        recPresenter.setListner(this,playPresenter);
    }
}
