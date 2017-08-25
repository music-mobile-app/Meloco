package jp.excd.meloco.activity;

import android.media.AudioFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import jp.excd.meloco.R;
import jp.excd.meloco.audio.engine.AudioConfig;
import jp.excd.meloco.audio.engine.AudioController;
import jp.excd.meloco.utility.CommonUtil;
import jp.excd.meloco.utility.WLog;

public class DevelopConfigSetActivity extends AppCompatActivity {

    //自分自身の参照
    private static DevelopConfigSetActivity me = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_develop_config_set);

        //画面のIFの初期化
        initGui();

    }
    //--------------------------------------------------------------------------------------------
    // GUIの初期化
    //--------------------------------------------------------------------------------------------
    private void initGui() {

        WLog.d(this, "initGui()");
        // 参照をスタティックに保存
        me = this;

        // 閉じるボタンの設定
        initButtonClose();
        // モノ・ステレオ設定ＵＩの設定
        initRadioChannelConfig();
        // サンプルレート設定ＵＩの設定
        initRadioSampleRate();
        // エンコーディングフォーマット設定ＵＩの設定
        initRadioAudioFormat();
        // ループバッファフレーム設定ＵＩの設定
        initLoopBufferSize();
        // フェードインフレーム設定ＵＩの設定
        initFadeFrameSize();
        // フェードアウトフレーム設定ＵＩの設定
        initFadeOutFrameSize();
        // 元音の振幅設定ＵＩの設定
        initSourceSoundRange();
        // リミッティングしきい値の設定ＵＩの設定
        initCompresBorder();
        // ログ出力設定ＵＩの設定
        initSwitchLog();
        // 再起移動ボタンの設定
        initButtonToReStart();
    }
    //--------------------------------------------------------------------------------------------
    // 閉じるボタンの初期化
    //--------------------------------------------------------------------------------------------
    private void initButtonClose() {

        WLog.d(this, "initButtonClose()");

        //閉じるボタンの設定
        final Button closeButton = (Button)this.findViewById(R.id.buttonClose);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WLog.d(this, "閉じるボタン押下");
                //閉じる。
                finish();
            }
        });
    }
    //--------------------------------------------------------------------------------------------
    // ステレオ/モノラルラジオの初期化
    //--------------------------------------------------------------------------------------------
    private void initRadioChannelConfig() {

        WLog.d(this, "initRadioChannelConfig()");

        //ラジオグループの取得
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroupChannelConfig);

        //現状の状態を取得
        int active = AudioConfig.CHANNEL_CONFIG;

        if (active == AudioFormat.CHANNEL_OUT_MONO) {
            //モノの場合、モノを選択
            WLog.d(this, "MONOチェック");
            radioGroup.check(R.id.radiobuttonChannelConfigMono);
        } else if(active == AudioFormat.CHANNEL_OUT_STEREO) {
            //ステレオが選択されている場合は、ステレオを選択
            radioGroup.check(R.id.radiobuttonChannelConfigStereo);
        }
    }
    //--------------------------------------------------------------------------------------------
    // サンプリングレートラジオの初期化
    //--------------------------------------------------------------------------------------------
    private void initRadioSampleRate() {

        WLog.d(this, "initRadioSampleRate()");

        //ラジオグループの取得
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroupSampleRate);

        //現状の状態を取得
        int active = AudioConfig.SAMPLE_RATE;

        if (active == 44100) {
            WLog.d(this, "44100チェック");
            radioGroup.check(R.id.radiobuttonSampleRate44100);
        } else if(active == 22050) {
            WLog.d(this, "22050チェック");
            radioGroup.check(R.id.radiobuttonSampleRate22050);
        }
    }
    //--------------------------------------------------------------------------------------------
    // エンコーディングフォーマットの初期化
    //--------------------------------------------------------------------------------------------
    private void initRadioAudioFormat() {

        WLog.d(this, "initRadioAudioFormat()");

        //ラジオグループの取得
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroupAudioFormat);

        //現状の状態を取得
        int active = AudioConfig.AUDIO_FORMAT;

        if (active == AudioFormat.ENCODING_PCM_8BIT) {
            WLog.d(this, "8bitチェック");
            radioGroup.check(R.id.radiobuttonAudioFormat8);
        } else {
            WLog.d(this, "16bitチェック");
            radioGroup.check(R.id.radiobuttonAudioFormat16);
        }
    }
    //--------------------------------------------------------------------------------------------
    // ループバッファフレーム数の初期化
    //--------------------------------------------------------------------------------------------
    private void initLoopBufferSize() {

        WLog.d(this, "initLoopBufferSize()");

        //テキストViewの参照の取得
        final TextView textView = (TextView)findViewById(R.id.textViewValueLoopBufferSize);

        //SeekBarの参照の取得
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarLoopBufferSize);

        //現状の状態を取得
        int active = AudioConfig.LOOP_BUFFER_SIZE;

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
    //--------------------------------------------------------------------------------------------
    // フェードインフレームサイズの初期化
    //--------------------------------------------------------------------------------------------
    private void initFadeFrameSize() {

        WLog.d(this, "initFadeInFrameSize()");

        //テキストViewの参照の取得
        final TextView textView = (TextView)findViewById(R.id.textViewValueFadeInFrameSize);

        //SeekBarの参照の取得
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarFadeInFrameSize);

        //現状の状態を取得
        int active = AudioConfig.FADEIN_FRAME_SIZE;

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
    //--------------------------------------------------------------------------------------------
    // フェードアウトフレームサイズの初期化
    //--------------------------------------------------------------------------------------------
    private void initFadeOutFrameSize() {

        WLog.d(this, "initFadeOutFrameSize()");

        //テキストViewの参照の取得
        final TextView textView = (TextView)findViewById(R.id.textViewValueFadeOutFrameSize);

        //SeekBarの参照の取得
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarFadeOutFrameSize);

        //現状の状態を取得
        int active = AudioConfig.FADEOUT_FRAME_SIZE;

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
    //--------------------------------------------------------------------------------------------
    // 原音の振幅倍率の初期化
    //--------------------------------------------------------------------------------------------
    private void initSourceSoundRange() {

        WLog.d(this, "initSourceSoundRange()");

        //テキストViewの参照の取得
        final TextView textView = (TextView)findViewById(R.id.textViewValueSourceSoundRange);

        //SeekBarの参照の取得
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarSourceSoundRange);

        //現状の状態を取得
        int active = AudioConfig.SOURCE_SOUND_RANGE;

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
    //--------------------------------------------------------------------------------------------
    // リミッティングのしきい値の初期化
    //--------------------------------------------------------------------------------------------
    private void initCompresBorder() {

        WLog.d(this, "initCompresBorder()");

        //テキストViewの参照の取得
        final TextView textView = (TextView)findViewById(R.id.textViewValueCompresBorder);

        //SeekBarの参照の取得
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarCompresBorder);

        //現状の状態を取得
        int active = AudioConfig.COMPRESS_BORDER;

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
    //--------------------------------------------------------------------------------------------
    // ログ出力設定の初期化
    //--------------------------------------------------------------------------------------------
    private void initSwitchLog() {

        WLog.d(this, "initSwitchLog()");

        //ログ出力設定スイッチの参照
        Switch aSwitch = (Switch) findViewById(R.id.switchLog);

        //現状の状態を取得
        boolean active = WLog.logOnForCritical;

        if (active == true) {
            WLog.d(this, "ON");
            aSwitch.setChecked(true);
        } else {
            WLog.d(this, "OFF");
            aSwitch.setChecked(false);
        }
    }
    //--------------------------------------------------------------------------------------------
    // 再起動ボタンの初期化
    //--------------------------------------------------------------------------------------------
    private void initButtonToReStart() {

        WLog.d(this, "initButtonClose()");

        //閉じるボタンの設定
        final Button closeButton = (Button)this.findViewById(R.id.buttonToReStart);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WLog.d(this, "再起動ボタン押下");

                //再起動
                DevelopConfigSetActivity.buttonToRestart();

                //閉じる。
                finish();
            }
        });
    }
    //--------------------------------------------------------------------------------------------
    // 再起動ボタンの押下
    //--------------------------------------------------------------------------------------------
    public static void buttonToRestart() {
        WLog.d("buttonToRestart()");

        //----------------------------------------------------------------------------------------
        // Audioを止める。
        //----------------------------------------------------------------------------------------
        WLog.d("Audioストップ");
        AudioController.audioStop();

        //----------------------------------------------------------------------------------------
        // 値の取得と設定
        //----------------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------
        // ■ステレオ/モノラル設定
        //----------------------------------------------------------------------------------------
        RadioGroup radioGroup = (RadioGroup)(me.findViewById(R.id.radiogroupChannelConfig));
        //選択されているIDの取得
        int rId = radioGroup.getCheckedRadioButtonId();
        if (rId == R.id.radiobuttonChannelConfigStereo) {
            WLog.d("ステレオ設定");
            AudioConfig.CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_STEREO;
        } else if (rId == R.id.radiobuttonChannelConfigMono) {
            WLog.d("モノラル設定");
            AudioConfig.CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
        }
        //----------------------------------------------------------------------------------------
        // ■サンプリングレート設定
        //----------------------------------------------------------------------------------------
        radioGroup = (RadioGroup)(me.findViewById(R.id.radiogroupSampleRate));
        //選択されているIDの取得
        rId = radioGroup.getCheckedRadioButtonId();
        if (rId == R.id.radiobuttonSampleRate44100) {
            WLog.d("44100設定");
            AudioConfig.SAMPLE_RATE = 44100;
        } else if (rId == R.id.radiobuttonSampleRate22050) {
            WLog.d("22050設定");
            AudioConfig.SAMPLE_RATE = 22050;
        }
        //----------------------------------------------------------------------------------------
        // ■エンコーディングフォーマット
        //----------------------------------------------------------------------------------------
        radioGroup = (RadioGroup)(me.findViewById(R.id.radiogroupAudioFormat));
        //選択されているIDの取得
        rId = radioGroup.getCheckedRadioButtonId();
        if (rId == R.id.radiobuttonAudioFormat8) {
            WLog.d("8BIT設定");
            AudioConfig.AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT;
        } else if (rId == R.id.radiobuttonAudioFormat16) {
            WLog.d("16BIT設定");
            AudioConfig.AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        }
        //----------------------------------------------------------------------------------------
        // ■ループバッファフレーム数
        //----------------------------------------------------------------------------------------
        SeekBar seekBar = (SeekBar)(me.findViewById(R.id.seekBarLoopBufferSize));
        int active = seekBar.getProgress();
        WLog.d("ループバッファフレーム数" + active);
        AudioConfig.LOOP_BUFFER_SIZE = active;

        //----------------------------------------------------------------------------------------
        // ■フェードインフレーム数
        //----------------------------------------------------------------------------------------
        seekBar = (SeekBar)(me.findViewById(R.id.seekBarFadeInFrameSize));
        active = seekBar.getProgress();
        WLog.d("フェードインフレーム数" + active);
        AudioConfig.FADEIN_FRAME_SIZE = active;

        //----------------------------------------------------------------------------------------
        // ■フェードアウトフレーム数
        //----------------------------------------------------------------------------------------
        seekBar = (SeekBar)(me.findViewById(R.id.seekBarFadeOutFrameSize));
        active = seekBar.getProgress();
        WLog.d("フェードアウトフレーム数" + active);
        AudioConfig.FADEOUT_FRAME_SIZE = active;

        //----------------------------------------------------------------------------------------
        // ■ 元音の振幅
        //----------------------------------------------------------------------------------------
        seekBar = (SeekBar)(me.findViewById(R.id.seekBarSourceSoundRange));
        active = seekBar.getProgress();
        WLog.d("元音の振幅" + active);
        AudioConfig.SOURCE_SOUND_RANGE = active;

        //----------------------------------------------------------------------------------------
        // ■ リミッティングのしきい値
        //----------------------------------------------------------------------------------------
        seekBar = (SeekBar)(me.findViewById(R.id.seekBarCompresBorder));
        active = seekBar.getProgress();
        WLog.d("リミッティングのしきい値" + active);
        AudioConfig.COMPRESS_BORDER = active;

        //----------------------------------------------------------------------------------------
        // ■ ログ取得
        //----------------------------------------------------------------------------------------
        //ログ出力設定スイッチの参照
        Switch aSwitch = (Switch)(me.findViewById(R.id.switchLog));

        //現状の状態を取得
        boolean b = aSwitch.isChecked();

        WLog.d("ログ取得設定b=" + b);
        WLog.logOnForCritical = b;

        //----------------------------------------------------------------------------------------
        // Audio起動
        //----------------------------------------------------------------------------------------
        WLog.d("Audio起動");
        AudioController.audioStart();
    }
}
