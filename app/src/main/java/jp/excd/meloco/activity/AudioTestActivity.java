package jp.excd.meloco.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jp.excd.meloco.R;
import jp.excd.meloco.sample.AudioTest001;
import jp.excd.meloco.sample.AudioTest002;
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
        //１秒サイン派をstatic発音
        final Button buttonPlay1secSinStatic = (Button) this.findViewById(R.id.buttonPlay1secSinStatic);
        buttonPlay1secSinStatic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                WLog.d(this,"１秒サイン派をstatic再生");
                AudioTest001.play();
            }
        });
        //１秒サイン派をstatic発音(16bit)
        final Button buttonPlay1secSinStatic16bit = (Button) this.findViewById(R.id.buttonPlay1secSinStatic16bit);
        buttonPlay1secSinStatic16bit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                WLog.d(this,"１秒サイン派をstatic再生(16bit)");
                AudioTest002.play();
            }
        });
    }
}
