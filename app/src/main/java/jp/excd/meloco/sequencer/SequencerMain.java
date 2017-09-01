//--------------------------------------------------------------------------------------------------
// シーケンサーメイン機能
// シーケンサー機能の骨格であり、シーケンス機能の外部ＩＦを提供する。
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.sequencer;

import jp.excd.meloco.audio.engine.AudioConfig;
import jp.excd.meloco.settings.SongSettings;
import jp.excd.meloco.utility.WLog;

public class SequencerMain {

    // クリック音の繰り返し数(秒数)
    // 分(10) * 60;
    public static int CLICK_LOOP_SIZE = 10 * 60;
    //----------------------------------------------------------------------------------------------
    // 名称    ：シーケンス波形の生成
    // 処理概要：曲の最後までのクリック音の波形を返す。
    //           曲の最後の判断はつかない為、一般的な曲の小節数（拍数）を十分に上回る
    //           データを返す。
    // 引数１  ：クリック音短音の波形
    // 引数２  ：ボリューム(1～127の数値で設定する。)
    //----------------------------------------------------------------------------------------------
    public static short[] sequencedSoundForClick(short[] clickSingle, int volume) {

        // デフォルトのBPM数を取得する。
        int defaultBPM = SongSettings.getDefaultBPM();

        // テンポチェンジ、編拍子の情報を取得する。
        // TODO実装する。

        // サンプリングレートの取得
        int sampleRate = AudioConfig.SAMPLE_RATE;

        // サンプリングレートより、最終的に返却する音源のサイズを算出する。
        int size = CLICK_LOOP_SIZE * sampleRate;
        WLog.d("sequencedSoundForClick_size=" + size);

        // 最終的に返却する波形
        short[] volumeControlledWaveDatas = new short[size];

        // ボリューム設定より、波形の係数を算出
        double dVolume = (double)volume;
        double rate = dVolume / 127.0;

        // クリック音のポインタ
        int clickNotePointer = 0;
        for (int i =0; i < volumeControlledWaveDatas.length; i++) {
            WLog.dc("sequencedSoundForClick()_i=" + i);
            int amari = i % 10000;
            if (amari == 0) {
                WLog.d("sequencedSoundForClick_i=" + i);
            }
            if (isClickNoteOnTimming(i, defaultBPM)) {
                //ノートオンのタイミングの場合
                clickNotePointer = 0;
            }
            short wave = cutWave(clickSingle, clickNotePointer);
            clickNotePointer = clickNotePointer + 1;
            volumeControlledWaveDatas[i] = (short)(rate * wave);
        }
        return volumeControlledWaveDatas;
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：クリック発音タイミングか？
    // 処理概要：与えられたポインタ、BPMを元にそれがクリック音の発音するタイミングかどうかを
    //           返却する。(true：発音タイミングである。false：発音タイミングでない)
    // 引数１  ：波形の位置
    // 引数２  ：BPM
    //----------------------------------------------------------------------------------------------
    public static boolean isClickNoteOnTimming(int pointer, int BPM) {

        WLog.dc("isClickNoteOnTimming()_pointer=" + pointer);
        WLog.dc("isClickNoteOnTimming()_BPM=" + BPM);

        //返却値
        boolean ret = false;

        //ポインタがゼロの場合は、無条件に発音タイミングであると返却する。
        if (pointer == 0) {
            ret = true;
            return ret;
        }
        // サンプリングレートの取得
        int sampleRate = AudioConfig.SAMPLE_RATE;

        //------------------------------------------------------------------------------------------
        // サンプリングレートにおける正確な発音タイミングの算出
        //------------------------------------------------------------------------------------------
        // BPMより一秒間に発音する回数を取得
        double dBPM = (double)BPM;
        double dByousu = 60.0;
        double kaisu =  dBPM / dByousu;
        WLog.dc("isClickNoteOnTimming()_kaisu=" + kaisu);

        //------------------------------------------------------------------------------------------
        // Justな波形数を算出
        //------------------------------------------------------------------------------------------
        double dSampleRate = (double)sampleRate;
        double justTimming = dSampleRate / kaisu;
        WLog.dc("isClickNoteOnTimming()_justTimming=" + justTimming);

        // クリック音の発音回数
        int noteOnCount = 1;

        // ループ中フラグの設定
        boolean loopFlg = true;
        while (loopFlg) {
            // Justな波形数より、次に到来する波形数を算出
            int nextTimming = (int) (noteOnCount * justTimming);

            WLog.dc("isClickNoteOnTimming()_nextTimming=" + nextTimming);

            if (pointer == nextTimming) {
                // 発音タイミングの場合、trueを返却
                WLog.dc("isClickNoteOnTimming()_ret=true");
                ret = true;
            } else if (pointer < nextTimming) {
                // 現在のポインタが、発音タイミングに満たない場合は
                // 発音タイミングが訪れることはないので、falseを返却する。
                WLog.dc("isClickNoteOnTimming()_ループ終了");
                loopFlg = false;
            }
            //ノートカウンタを進める。
            noteOnCount = noteOnCount + 1;
        }
        WLog.dc("isClickNoteOnTimming()_ret=" + ret);
        return ret;
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：振幅抽出
    // 処理概要：与えられたポインタで、波形データから振幅を取り出す。
    // 引数１  ：波形情報
    // 引数２  ：振幅を取り出す位置
    //----------------------------------------------------------------------------------------------
    public static short cutWave(short[] waves, int pointer) {
        WLog.dc("cutWave_pointer=" + pointer);

        int waveSize = waves.length;
        WLog.dc("cutWave_waveSize=" + waveSize);

        //返却値
        short ret = 0;

        if (waveSize <= pointer) {
            //抽出位置が、波形の全体サイズを超えている場合、
            //ゼロを返却する。
            ret = 0;
        } else {
            ret = waves[pointer];
        }
        WLog.dc("cutWave_ret=" + ret);
        return ret;
    }
}
