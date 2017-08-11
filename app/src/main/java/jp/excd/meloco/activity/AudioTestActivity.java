package jp.excd.meloco.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jp.excd.meloco.R;
import jp.excd.meloco.utility.WLog;

public class AudioTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_test);
        //GUIの初期設定
        initGui();
    }
    //--------------------------------------------------------------------------
    // GUIの初期設定
    //--------------------------------------------------------------------------
    private void initGui() {
        WLog.d(this, "initGui()");
        //閉じるボタンの設定
        final Button closeButton = (Button) this.findViewById(R.id.buttonClose);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                WLog.d(this, "閉じるボタン押下");
                //閉じる。
                finish();
            }
        });
    }
}
