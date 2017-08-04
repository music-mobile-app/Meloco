//--------------------------------------------
// 汎用ユーティリティー
//--------------------------------------------
package jp.excd.meloco.utility;

import android.media.AudioFormat;
import android.util.Log;

import jp.excd.meloco.utility.WLog;

import jp.excd.meloco.audio.engine.AudioConfig;

public class CommonUtil {
    //----------------------------------------------------------------
    public static String TAG_FOR_LOG = "アプリ出力ログ";
    //----------------------------------------------------------------
    // ログ出力用のタグ取得
    //   インスタンスメソッドの場合
    //----------------------------------------------------------------
    public static String tag(Object o) {

        String s = TAG_FOR_LOG + " " + o.getClass().getSimpleName();
        return s;
    }
    //----------------------------------------------------------------
    // ログ出力用のタグ取得
    //   スタティックメソッドの場合
    //----------------------------------------------------------------
    public static String tag() {
        return TAG_FOR_LOG;
    }
    //----------------------------------------------------------------------------------------------
    // 16bit⇒8bit変換
    //----------------------------------------------------------------------------------------------
    public static byte[] from16to8(short[] shorts) {

        byte[] ret = new byte[shorts.length];
        for (int i =0; i < ret.length; i++) {
            ret[i] = (byte)shorts[i];
        }
        return ret;
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：波形データの重ね合わせ
    // 処理概要：与えられた２つの波形データを単純加算して返却する。
    //           重ね合わせた結果がintを越える場合は、intの最大値に補正する。
    // 注意点  ：与える２つの配列の数が違う場合、第１引数の数に合わせられる。
    // 引数１　：重ね合わせる対象の配列（元）
    // 引数２　：重ね合わせる対象の配列（加算対象）
    // 戻り値　：加算した波形配列
    //----------------------------------------------------------------------------------------------
    public static int[] addWave(int[] a, int[] b) {

        //返却する配列
        int[] rets = new int[a.length];

        for(int i = 0; i < rets.length; i++) {
            long wave = 0;
            if (i < b.length) {
                wave = a[i] + b[i];
            } else {
                wave = a[i];
            }
            //単純まるめ
            if (wave >= Integer.MAX_VALUE) {
                wave = Integer.MAX_VALUE;
            } else if (wave <= Integer.MIN_VALUE){
                wave = Integer.MIN_VALUE;
            }
            rets[i] = (int)wave;
        }
        return rets;
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：波形データの圧縮
    // 処理概要：与えられた波形データを、AudioTrackが受け取るエンコーディングの範囲まで圧縮する。
    //           戻り値はshortであるが、エンコーディングが8BITの場合は、byteの範囲までが入る。
    // 引数１　：圧縮前のint配列
    // 戻り値　：圧縮後のshort配列
    //----------------------------------------------------------------------------------------------
    public static short[] compressWaveData(int[] is) {

        //返却する配列
        short[] rets = new short[is.length];

        for (int i = 0; i < rets.length; i++) {
            rets[i] = compressWaveData(is[i]);
        }
        return rets;
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：波形データの圧縮
    // 処理概要：与えられた波形データを、AudioTrackが受け取るエンコーディングの範囲まで圧縮する。
    //           戻り値はshortであるが、エンコーディングが8BITの場合は、byteの範囲までが入る。
    // 引数１　：圧縮前のint
    // 戻り値　：圧縮後のshort
    //----------------------------------------------------------------------------------------------
    public static short compressWaveData(int inI) {

        short ret = 0;
        //------------------------------------------------------------------------------------------
        // エンコーディングのフォーマットの判断
        //------------------------------------------------------------------------------------------
        if (AudioConfig.AUDIO_FORMAT == AudioFormat.ENCODING_PCM_8BIT) {
            ret = compressWaveData(inI, Byte.MIN_VALUE, Byte.MAX_VALUE);
        } else if (AudioConfig.AUDIO_FORMAT == AudioFormat.ENCODING_PCM_16BIT) {
            ret = compressWaveData(inI, Short.MIN_VALUE, Short.MAX_VALUE);
        }
        return ret;
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：波形データの圧縮
    // 処理概要：与えられた波形データを16bitに圧縮して返却する。
    // 引数１　：圧縮前のint
    // 引数２　：波形データの最小値
    // 引数３　：波形データの最大値
    // 戻り値　：圧縮後のshort
    //----------------------------------------------------------------------------------------------
    public static short compressWaveData(int inI, int minValue, int maxValue) {

        WLog.d("compressIntToShort");
        WLog.d("inI=" + inI);

        //ローカット要
        boolean lowCutOn = false;
        //ハイカット要
        boolean highCutOn = false;
        //------------------------------------------------------------------------------------------
        // 圧縮のしきい値の取得
        //------------------------------------------------------------------------------------------
        int border = AudioConfig.COMPRESS_BORDER;
        WLog.d("border=" + border);

        double dBorder = (double)border;
        double per = (double)(dBorder / 100.0);

        int lowLimit = (int)(minValue * per);
        WLog.d("lowLimit=" + lowLimit);


        int highLimit = (int)(maxValue * per);
        WLog.d("highLimit=" + highLimit);

        //------------------------------------------------------------------------------------------
        // 圧縮のしきい値チェック(下限)
        //------------------------------------------------------------------------------------------
        if (lowLimit > inI) {
            lowCutOn = true;
            WLog.d("lowCutOn=on");
        }
        //------------------------------------------------------------------------------------------
        // 圧縮のしきい値チェック(上限)
        //------------------------------------------------------------------------------------------
        if (highLimit < inI) {
            highCutOn = true;
            WLog.d("highCutOn=on");
        }
        //------------------------------------------------------------------------------------------
        // 圧縮のしきい値を越えていなければ、何もしない。
        //------------------------------------------------------------------------------------------
        if ((highCutOn == false)&&(lowCutOn == false)) {
            WLog.d("何もせずに返却");
            return (short)inI;
        }
        //------------------------------------------------------------------------------------------
        // 想定する（現実的な）Inputの最大を、本来の振幅の３倍とし、
        // Inputが、しきい値を越え、想定するInputの最大までの割合を、
        // しきい値から振幅の最大までの幅の占める割合に置き換える。
        // もしもInputが想定するInputの最大をそもそも越えている場合は、
        // 振幅の最大に合わせる。
        //------------------------------------------------------------------------------------------
        //圧縮対象の幅の算出
        int compressRage = (maxValue * 3) - highLimit;
        //実データの圧縮対象の幅
        int targetCompressRange = maxValue - highLimit;

        //------------------------------------------------------------------------------------------
        // 上限調整
        //------------------------------------------------------------------------------------------
        if (highCutOn) {
            //差分の取り出し
            int sa = inI - highLimit;
            WLog.d("sa=" + sa);

            //差分の圧縮対象の幅に占める割合を算出①
            double fraction = ((double)sa / (double)compressRage);
            WLog.d("fraction=" + fraction);

            //実データの圧縮対象の幅にかけ合わせる。
            int compressZone = (int)(fraction * targetCompressRange);
            WLog.d("compressZone=" + compressZone);

            //圧縮
            int compressed = highLimit + compressZone;
            WLog.d("compressed=" + compressed);

            //この時点で、振幅の最大値を越えている場合（①で１以上が求まっている場合）
            //最大値に調整
            if (compressed > maxValue) {
                WLog.d("上限最大値に調整");
                return (short)maxValue;
            } else {
                return (short)compressed;
            }
        }
        //------------------------------------------------------------------------------------------
        // 下限調整
        //------------------------------------------------------------------------------------------
        if (lowCutOn) {
            //差分の取り出し
            int sa = inI - lowLimit;
            WLog.d("sa=" + sa);
            //絶対値に直す
            sa = (-1) * sa;

            //差分の圧縮対象の幅に占める割合を算出①
            double fraction = ((double)sa / (double)compressRage);
            WLog.d("fraction=" + fraction);

            //実データの圧縮対象の幅にかけ合わせる。
            int compressZone = (int)(fraction * targetCompressRange);
            WLog.d("compressZone=" + compressZone);

            //圧縮
            int compressed = lowLimit - compressZone;
            WLog.d("compressed=" + compressed);

            //この時点で、振幅の最小値を越えている場合（①で１以上が求まっている場合）
            //最小値に調整
            if (compressed < minValue) {
                WLog.d("下限最小値に調整");
                return (short)minValue;
            } else {
                return (short)compressed;
            }
        }
        WLog.d("下限調整も上限調整もなし");
        return (short)inI;
    }
}
