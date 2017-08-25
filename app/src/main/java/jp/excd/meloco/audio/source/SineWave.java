//--------------------------------------------------------------------------------------------------
// 発音中のサイン波
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.audio.source;

import android.content.Intent;
import android.media.AudioFormat;
import android.util.Log;

import jp.excd.meloco.audio.engine.ActiveNote;
import jp.excd.meloco.audio.engine.AudioConfig;
import jp.excd.meloco.utility.WLog;

public class SineWave extends ActiveNote {

    //最低音の周波数(C2を想定)
    public static double LOWEST_FREQUENCY = 65.4064;

    //フェードイン中フラグ(キータッチの初めからフェードイン）
    private boolean onFadeIn = true;

    //フェードアウト中フラグ
    private boolean onFadeOut = false;

    //フェードインカウンタ
    private int fadeInCount = 0;

    //フェードアウトカウンタ
    private int fadeOutCount = 0;

    //波形計算のステップ(サンプリングレートの逆数
    private double dt = 1.0 / AudioConfig.SAMPLE_RATE;

    //現在の位相
    private double t = 0.0;

    //周波数
    private double freq;

    //振幅の最大幅
    private double maxAmplitude;

    //音程
    private String pitch = "";

    //終了フラグ
    private boolean isEnd = false;
    //-------------------------------------------------------------------------------
    // コンストラクタ
    //-------------------------------------------------------------------------------
    public SineWave(String pitch, int volume) {

        WLog.dc(this, "コンストラクタ");

        this.pitch = pitch;
        WLog.dc(this, "pitch=" + pitch);

        //---------------------------------------------------------------------------
        // 周波数の計算
        //---------------------------------------------------------------------------
        this.freq = getFreq(pitch);
        WLog.dc(this, "this.freq=" + this.freq);

        //---------------------------------------------------------------------------
        // 最大振幅の計算
        //---------------------------------------------------------------------------
        // システム上の最低音を最大振幅とし、その音との比の逆数を、最大振幅とする。
        // 周波数の異なる音を、聴感上の同じ大きさに揃えるため
        // 最低音の最大振幅との比を算出
        double hi = this.freq / LOWEST_FREQUENCY;

        // システム設定上の振幅に比の逆数を、掛け合わせて最大振幅を算出する。
        if (AudioConfig.AUDIO_FORMAT == AudioFormat.ENCODING_PCM_8BIT) {
            //エンコーディングが8bitの場合は、Byteの範囲で設定する。
            this.maxAmplitude = Byte.MAX_VALUE / hi;
        } else {
            this.maxAmplitude = Short.MAX_VALUE / hi;
        }
        WLog.dc(this, "this.maxAmplitude=" + this.maxAmplitude);
    }
    //-------------------------------------------------------------------------------
    // 波形情報を返却
    // 処理概要：次の波形データを返却しつつ、音源の情報を次回呼び出しの準備状態に更新する。
    //           戻り値はint[]配列であるが、エンコーディングが、8bitなら、byteを超えないデータ
    //           エンコーディングが16bitなら、shortを超えないデータを返却する。
    //           TODO:ステレオの実装を行う。
    //-------------------------------------------------------------------------------
    @Override
    public int[] getAndUpdateWaveData() {

        WLog.dc(this,"getAndUpdateWaveData()");

        // 最終的に返却するデータ
        int[] sinWave = new int[AudioConfig.LOOP_BUFFER_SIZE];

        // 波形データの作成
        for (int i =0; i < sinWave.length; i++) {
            //----------------------------------------------------------------------
            //　sinメソッドは、与えれたラジアンに対し、
            //　sin値を返却する。
            //  ラジアンは以下の式で算出する。
            //  サンプルレートの逆数 * 波形算出回数(※) * 周波数 * パイ * 2
            //  (※)サンプルレートの逆数と波形算出回数の乗算は、
            //      足し算で表す。
            //----------------------------------------------------------------------
            // 振幅の算出
            t = t + dt;
            double amplitude = Math.sin(2.0 * Math.PI * t * freq);

            // ボリュームを調整する。
            double volume = (double)((double)AudioConfig.SOURCE_SOUND_RANGE / 100.0);
            amplitude = amplitude * volume;

            //振幅は、その周波数の最大振幅に合わせる。
            if (AudioConfig.AUDIO_FORMAT == AudioFormat.ENCODING_PCM_8BIT) {
                //エンコーディングが8bitの場合は、Byteの範囲で設定する。
                sinWave[i] = (byte)(this.maxAmplitude * amplitude);
            } else {
                sinWave[i] = (short)(this.maxAmplitude * amplitude);
            }
        }
        //----------------------------------------------------------------------------------------
        //キータッチが始まった時点から、
        //少しずつフェードインで、波形を形成していく。
        //ノイズ防止のため
        //----------------------------------------------------------------------------------------
        if (this.onFadeIn) {
            sinWave = fadeIn(sinWave);
            if (this.fadeInCount >= AudioConfig.FADEIN_FRAME_SIZE) {
                //終了フラグの設定
                this.onFadeIn = false;
            }
        }
        //----------------------------------------------------------------------------------------
        //キータッチが終わった時点から、
        //少しずつフェードアウトで、波形を消していく。
        //ノイズ防止のため
        //----------------------------------------------------------------------------------------
        if (this.onFadeOut) {
            sinWave = fadeOut(sinWave);
            if (this.fadeOutCount >= AudioConfig.FADEOUT_FRAME_SIZE) {
                //終了フラグの設定
                this.isEnd = true;
            }
        }
        return sinWave;
    }
    //-------------------------------------------------------------------------------
    // フェードイン処理
    // 処理概要：与えられた振幅配列に対して、少しずつ振幅を大きくして返却する。
    //-------------------------------------------------------------------------------
    private int[] fadeIn(int[] inWave) {

        int[] retWave = new int[inWave.length];
        for (int i = 0; i < retWave.length; i++) {

            //フェードインカウンタのカウントアップ
            this.fadeInCount = this.fadeInCount + 1;

            //この時点で、フェードインのフレームサイズを越えている場合、
            //フェードイン処理をしない。
            if (this.fadeInCount >= AudioConfig.FADEIN_FRAME_SIZE) {
                retWave[i] = inWave[i];
                continue;
            }
            //フェードインのフレーム数からの比率の参集
            double hi = 0.0;
            if (this.fadeInCount < AudioConfig.FADEIN_FRAME_SIZE) {
                double count = (double)this.fadeInCount;
                double frame = (double)AudioConfig.FADEIN_FRAME_SIZE;
                hi = count / frame;
            }
            //比を掛け目とする。
            double filter = hi;

            //元のデータと掛け合わせる。
            double target = inWave[i];
            double ret = target * filter;

            //補正する。
            retWave[i] = (int)ret;
        }
        return retWave;
    }
    //-------------------------------------------------------------------------------
    // フェードアウト処理
    // 処理概要：与えられた振幅配列に対して、少しずつ振幅を小さく返却する。
    //-------------------------------------------------------------------------------
    private int[] fadeOut(int[] inWave) {

        int[] retWave = new int[inWave.length];
        for (int i = 0; i < retWave.length; i++) {

            //フェードアウトカウンタのカウントアップ
            this.fadeOutCount = this.fadeOutCount + 1;

            //この時点でフェードアウトのカウンタを越えている場合、
            //波形はゼロに調整する。
            if (this.fadeOutCount >= AudioConfig.FADEOUT_FRAME_SIZE) {
                retWave[i] = 0;
                continue;
            }
            //フェードアウトのフレーム数からの比率の参集
            double hi = 0.9999999;
            if (this.fadeOutCount < AudioConfig.FADEOUT_FRAME_SIZE) {
                double count = (double)this.fadeOutCount;
                double frame = (double)AudioConfig.FADEOUT_FRAME_SIZE;
                hi = count / frame;
            }
            //逆数を掛目とする。
            double filter = (1.0 - hi);

            //元のデータと掛け合わせる。
            double target = inWave[i];
            double ret = target * filter;

            //補正する。
            retWave[i] = (int)ret;
        }
        return retWave;
    }
    //-------------------------------------------------------------------------------
    // 終了していればtrue
    //-------------------------------------------------------------------------------
    @Override
    public boolean isEnd() {
        return this.isEnd;
    }
    //-------------------------------------------------------------------------------
    // 終了させる。
    //-------------------------------------------------------------------------------
    @Override
    public void toEnd() {
        //波形切れのノイズを避けるため、フェードアウトしながら波形を終息させる。
        this.onFadeOut = true;
    }
    //-------------------------------------------------------------------------------
    // 周波数の計算
    // 処理概要：与えられた音程に対応する周波数を返却する。
    // 引数１　：音程を表す文字列
    // 戻り値　：周波数を表すdouble値
    //-------------------------------------------------------------------------------
    public double getFreq(String pitch) {
        double ret = 0.0;
        if (pitch.equals("C7")){ ret = 2093.0045;}
        else if (pitch.equals("B6")){ ret = 1975.5332;}
        else if (pitch.equals("AS6")){ ret = 1864.6550;}
        else if (pitch.equals("A6")){ ret = 1760.0000;}
        else if (pitch.equals("GS6")){ ret = 1661.2188;}
        else if (pitch.equals("G6")){ ret = 1567.9817;}
        else if (pitch.equals("FS6")){ ret = 1479.9777;}
        else if (pitch.equals("F6")){ ret = 1396.9129;}
        else if (pitch.equals("E6")){ ret = 1318.5102;}
        else if (pitch.equals("DS6")){ ret = 1244.5079;}
        else if (pitch.equals("D6")){ ret = 1174.6591;}
        else if (pitch.equals("CS6")){ ret = 1108.7305;}
        else if (pitch.equals("C6")){ ret = 1046.5023;}
        else if (pitch.equals("B5")){ ret = 987.7666;}
        else if (pitch.equals("AS5")){ ret = 932.3275;}
        else if (pitch.equals("A5")){ ret = 880.0000;}
        else if (pitch.equals("GS5")){ ret = 830.6094;}
        else if (pitch.equals("G5")){ ret = 783.9909;}
        else if (pitch.equals("FS5")){ ret = 739.9888;}
        else if (pitch.equals("F5")){ ret = 698.4565;}
        else if (pitch.equals("E5")){ ret = 659.2551;}
        else if (pitch.equals("DS5")){ ret = 622.2540;}
        else if (pitch.equals("D5")){ ret = 587.3295;}
        else if (pitch.equals("CS5")){ ret = 554.3653;}
        else if (pitch.equals("C5")){ ret = 523.2511;}
        else if (pitch.equals("B4")){ ret = 493.8833;}
        else if (pitch.equals("AS4")){ ret = 466.1638;}
        else if (pitch.equals("A4")){ ret = 440.0000;}
        else if (pitch.equals("GS4")){ ret = 415.3047;}
        else if (pitch.equals("G4")){ ret = 391.9954;}
        else if (pitch.equals("FS4")){ ret = 369.9944;}
        else if (pitch.equals("F4")){ ret = 349.2282;}
        else if (pitch.equals("E4")){ ret = 329.6276;}
        else if (pitch.equals("DS4")){ ret = 311.1270;}
        else if (pitch.equals("D4")){ ret = 293.6648;}
        else if (pitch.equals("CS4")){ ret = 277.1826;}
        else if (pitch.equals("C4")){ ret = 261.6256;}
        else if (pitch.equals("B3")){ ret = 246.9417;}
        else if (pitch.equals("AS3")){ ret = 233.0819;}
        else if (pitch.equals("A3")){ ret = 220.0000;}
        else if (pitch.equals("GS3")){ ret = 207.6523;}
        else if (pitch.equals("G3")){ ret = 195.9977;}
        else if (pitch.equals("FS3")){ ret = 184.9972;}
        else if (pitch.equals("F3")){ ret = 174.6141;}
        else if (pitch.equals("E3")){ ret = 164.8138;}
        else if (pitch.equals("DS3")){ ret = 155.5635;}
        else if (pitch.equals("D3")){ ret = 146.8324;}
        else if (pitch.equals("CS3")){ ret = 138.5913;}
        else if (pitch.equals("C3")){ ret = 130.8128;}
        else if (pitch.equals("B2")){ ret = 123.4708;}
        else if (pitch.equals("AS2")){ ret = 116.5409;}
        else if (pitch.equals("A2")){ ret = 110.0000;}
        else if (pitch.equals("GS2")){ ret = 103.8262;}
        else if (pitch.equals("G2")){ ret = 97.9989;}
        else if (pitch.equals("FS2")){ ret = 92.4986;}
        else if (pitch.equals("F2")){ ret = 87.3071;}
        else if (pitch.equals("E2")){ ret = 82.4069;}
        else if (pitch.equals("DS2")){ ret = 77.7817;}
        else if (pitch.equals("D2")){ ret = 73.4162;}
        else if (pitch.equals("CS2")){ ret = 69.2957;}
        else if (pitch.equals("C2")){ ret = 65.4064;}

        return ret;
    }
}
