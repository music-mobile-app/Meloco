//--------------------------------------------------------------------------------------------------
// クリックループ音の音源管理
// クリック音を、現状のシーケンス情報に合わせて、
// 曲の終了まで波形生成して返却する。
//---------------------------------------------------------------------------------------------------
package jp.excd.meloco.audio.source;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;

import jp.excd.meloco.R;
import jp.excd.meloco.audio.engine.ActiveNote;
import jp.excd.meloco.audio.engine.AudioConfig;
import jp.excd.meloco.utility.WLog;

public class Click extends ActiveNote {

    // 音源ファイルのID
    // TODO:クリック音の音色の調整
    public static final int sourceId = R.raw.click2;

    // ヘッダサイズ
    public static int HEAD_SIZE = 80;

    // 波形データ（１音）
    public static short[] wavaSingle = null;

    // 波形データ全量
    public static short[] wavaAll = null;

    // 波形データブォリュームコントロール済み
    private short[] volumeControlledWaveDatas = null;

    // 現在、発音中の位置
    private int cursol = 0;

    // 終了フラグ
    private boolean isEnd = false;

    // ヴォリューム
    private int volume = 127;

    //フェードアウト中フラグ
    private boolean onFadeOut = false;

    //フェードアウトカウンタ
    private int fadeOutCount = 0;

    //----------------------------------------------------------------------------------------------
    // 波形データの初期読み込み
    // 処理概要：波形データを読み込んで、発音に備える。
    //           このクラスの使用前い一度だけ行う。
    //----------------------------------------------------------------------------------------------
    public static synchronized void sourceFileLoad(Activity activity) {
        //------------------------------------------------------------------------------------------
        // wavファイルを読み込む
        //------------------------------------------------------------------------------------------
        InputStream input;
        byte[] wavData = null;
        input = activity.getResources().openRawResource(sourceId);
        try {
            wavData = new byte[(int) input.available()];
            input.read(wavData);
            input.close();
        } catch(IOException e) {
            WLog.dc("ファイル読み込み失敗");
        }
        //------------------------------------------------------------------------------------------
        // AudioTrackがshortを受け取るため、shortに直す。
        // Audioファイルは、リトルエイディアンのため、2byte目を上位、1byte目を下位ととして扱う。
        //------------【計算式】---------------
        //  ◆1byte目がプラスの場合
        //     2byte目 * 256 + 1byte目
        //  ◆1byte目がマイナスの場合
        //     2byte目 * 256 + 1byte目 + 256
        //-------------------------------------
        // ※同時に先頭のヘッダ部分を切り落とす。
        //------------------------------------------------------------------------------------------
        int size = (int)(wavData.length - HEAD_SIZE) / 2;
        wavaAll = new short[size];
        for (int i = 0; i < wavaAll.length; i = i + 2) {
            //ターゲットバイトの読み込み
            byte b1 = wavData[i + HEAD_SIZE];
            byte b2 = wavData[i + HEAD_SIZE + 1];
            if (b1 < 0) {
                //マイナスの場合
                wavaAll[i] = (short)((b2 * 256) + (b1 + 256));
            } else {
                //プラスの場合
                wavaAll[i] = (short)((b2 * 256) + b1);
            }
        }
    }
    //-------------------------------------------------------------------------------
    // 名称    ：コンストラクタ
    // 処理概要：クリック音の初期設定を行う。
    // 引数１　：volume 1～127の範囲で、音の大きさを設定する。
    //-------------------------------------------------------------------------------
    public Click(int volume) {
        this.volume = volume;
        //---------------------------------------------------------------------------
        //波形データをヴォリュームに応じて再構成
        //---------------------------------------------------------------------------
        double dVolume = (double)this.volume;
        double rate = dVolume / 127.0;
        int size = wavaAll.length;
        this.volumeControlledWaveDatas = new short[size];
        for (int i =0; i < this.volumeControlledWaveDatas.length; i++) {
            this.volumeControlledWaveDatas[i] = (short)(rate * wavaAll[i]);
        }
    }
    //-------------------------------------------------------------------------------
    // 名称    ：波形情報を返却
    // 処理概要：次の波形データを返却しつつ、音源の情報を次回呼び出しの準備状態に更新する。
    //           戻り値はint[]配列であるが、shortを超えないデータを返却する。
    //           TODO:ステレオの実装を行う。
    //-------------------------------------------------------------------------------
    @Override
    public int[] getAndUpdateWaveData() {
        WLog.dc(this, "getAndUpdateWaveData()");

        // 最終的に返却するデータ
        int[] waves = null;

        // 波形データ全体のサイズ
        int size = volumeControlledWaveDatas.length;

        // 残りデータのサイズ計算
        int nokori = size - this.cursol;
        if (nokori < AudioConfig.LOOP_BUFFER_SIZE) {
            waves = new int[nokori];
            //終了フラグON
            isEnd = true;
        } else {
            waves = new int[AudioConfig.LOOP_BUFFER_SIZE];
        }
        for (int i = 0; i < waves.length; i++) {
            waves[i] = volumeControlledWaveDatas[this.cursol];
            this.cursol = this.cursol + 1;
        }
        //----------------------------------------------------------------------------------------
        //キータッチが終わった時点から、
        //少しずつフェードアウトで、波形を消していく。
        //ノイズ防止のため
        //----------------------------------------------------------------------------------------
        if (this.onFadeOut) {
            waves = fadeOut(waves);
            if (this.fadeOutCount >= AudioConfig.FADEOUT_FRAME_SIZE) {
                //終了フラグの設定
                this.isEnd = true;
            }
        }
        //波形データの返却
        return waves;
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

    @Override
    public void toEnd() {
        //波形切れのノイズを避けるため、フェードアウトしながら波形を終息させる。
        this.onFadeOut = true;
    }
}
