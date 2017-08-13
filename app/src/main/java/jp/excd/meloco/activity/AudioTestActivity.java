package jp.excd.meloco.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import jp.excd.meloco.R;
import jp.excd.meloco.audio.engine.AudioConfig;
import jp.excd.meloco.sample.AudioTest001;
import jp.excd.meloco.sample.AudioTest002;
import jp.excd.meloco.sample.AudioTest003;
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
        //サイン派をSTREAM発音(16bit)
        final Button buttonPlaySinStream16bit = (Button) this.findViewById(R.id.buttonPlaySinStream16bit);
        buttonPlaySinStream16bit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                WLog.d(this,"サイン派をstream再生(16bit)");
                AudioTest003.play();
            }
        });
        //サイン派をSTREAM停止(16bit)
        final Button buttonStopSinStream16bit = (Button) this.findViewById(R.id.buttonStopSinStream16bit);
        buttonStopSinStream16bit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                WLog.d(this,"サイン派STREAM再生を停止");
                AudioTest003.playStop();
            }
        });
        //VOLUMEのコントローラの設定
        initVolume();
    }
    //--------------------------------------------------------------------------------------------
    // ヴォリュームの初期化
    //--------------------------------------------------------------------------------------------
    private void initVolume() {

        WLog.d(this, "initVolume()");

        //テキストViewの参照の取得
        final TextView textView = (TextView)findViewById(R.id.textViewValueVolume);

        //SeekBarの参照の取得
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarVolume);

        //現状の状態を取得
        int active = AudioTest003.volume;

        //SeekBarの設定値を設定
        seekBar.setProgress(active);

        //テキストも初期表示
        textView.setText("" + active);

        //リスナー登録
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        WLog.d(this, "つまみの操作");

                        int pro = seekBar.getProgress();
                        textView.setText("" + pro);

                        //値の変更
                        WLog.d(this, "VOLUME変更pro=" + pro);
                        AudioTest003.toVolume = pro;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
    }
}
