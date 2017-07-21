//--------------------------------------------------------------------------------------------------
// 発音中のサイン波
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.audio;


import android.util.Log;

import jp.excd.meloco.utility.CommonUtil;

public class SineWave extends ActiveNote {

    //現在のサインは計算状況(位相)
    private double t = 0.0;

    //波形計算のステップ
    private double dt = 1.0 / AudioEngine.SAMPLE_RATE;

    //周波数
    private double freq;

    //音程
    private String pitch = "";

    //終了フラグ
    private boolean isEnd = false;
    //-------------------------------------------------------------------------------
    // コンストラクタ
    //-------------------------------------------------------------------------------
    public SineWave(String pitch, int volume) {
        this.pitch = pitch;
        //---------------------------------------------------------------------------
        // 周波数の計算
        //---------------------------------------------------------------------------
        this.freq = getFreq(pitch);
    }
    //-------------------------------------------------------------------------------
    // 波形情報を返却
    //-------------------------------------------------------------------------------
    @Override
    public byte[] getAndUpdateWaveData() {
        //Log.d(CommonUtil.tag(this),"getAndUpdateWaveData()");
        //Log.d(CommonUtil.tag(this),"getAndUpdateWaveData()loopBuffer=" + AudioEngine.loopBuffer);
        byte[] sinWave = new byte[AudioEngine.loopBuffer];

        //Log.d(CommonUtil.tag(this),"getAndUpdateWaveData()sinWave.length=" + sinWave.length);

        for (int j = 0; j < sinWave.length; j++, t += dt) {
            double d = Math.sin(2.0 * Math.PI * t * freq);
            sinWave[j] = (byte) (Byte.MAX_VALUE * (d / 2));
        }
        /*
        for (byte b : sinWave) {
            Log.d(CommonUtil.tag(this),"getAndUpdateWaveData()bbbb=" + b);
        }
        */
        // TODO：フェードアウト処理
        return sinWave;
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
        this.isEnd = true;
    }
    //-------------------------------------------------------------------------------
    // 周波数の計算
    //  第１引数：音程を表す文字列
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
