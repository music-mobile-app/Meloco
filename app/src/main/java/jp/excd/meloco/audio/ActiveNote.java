//--------------------------------------------------------------------------------------------------
// 発音中の音源データの抽象クラス
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.audio;

public abstract class ActiveNote {

    //ループバッファサイズの波データを返却しかつ、ポインタを進める。
    public abstract byte[] getAndUpdateWaveData();

    //すでに終了しているか確認(すでに完了した音源はtrue)
    public abstract boolean isEnd();

    //終了させる
    public abstract void toEnd();

}
