package jp.excd.meloco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import jp.excd.meloco.constant.KeyboardType;


import jp.excd.meloco.presenter.KeyboardPresenter;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {

    //クラス名(ログ出力用)
    String CLASS = getClass().getSimpleName();

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
    void initGui() {

        Log.d(CLASS, "initGui()実行");
        //----------------------------------------------------------------------
        // キーボードのリスナー登録
        //----------------------------------------------------------------------
        KeyboardPresenter keyboardPresenter = new KeyboardPresenter(KeyboardType.DOUBLE);
        keyboardPresenter.setListner(this);
    }
}
