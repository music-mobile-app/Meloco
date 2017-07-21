//--------------------------------------------------------------------------------------------------
// オーディオエンジン
//--------------------------------------------------------------------------------------------------
//  オーディオ出力を担当する。
//  全てのMUSICに関するスピーカ出力はこのクラスを介して行われる。
//  シングルトンとし、状態を保持するクラス変数へのアクセスは、
//  sycronized化され、複数のThreadからの同時更新はないものとする。
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import jp.excd.meloco.constant.SoundSourceType;
import jp.excd.meloco.utility.CommonUtil;

public class AudioEngine extends Thread{
    //----------------------------------------------------------------------------------------------
    //  定数
    //----------------------------------------------------------------------------------------------
    // サンプルレート
    public static final int SAMPLE_RATE = 44100;
    // ストリームタイプ(音楽)
    public static final int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    // チャネル定義(モノラル(TODO：後にステレオ化する。)
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    // オーディオフォーマット(8bit(TODO:後に16bit化する。)
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT;
    // ループバッファ(フレーム数)
    public static int loopBuffer = 128;
    //----------------------------------------------------------------------------------------------
    //  クラス変数(アクセス時には、syncronized化する。)
    //----------------------------------------------------------------------------------------------
    // 自分自身の参照
    private static AudioEngine me;
    //----------------------------------------------------------------------------------------------
    //  メンバ変数(アクセス時には、syncronized化する。)
    //----------------------------------------------------------------------------------------------
    // オーディオトラック
    private AudioTrack track = null;
    // 現在鳴っている音源群
    private Map<String ,ActiveNote> activeNotes;
    // 自分自身を表すキー
    private String myKey;

    //----------------------------------------------------------------------------------------------
    //   発音開始
    //----------------------------------------------------------------------------------------------
    //   第１引数 soundSourceType 音源の分類
    //   第２引数 pitch 音程
    //   第３引数 volume 音量
    //----------------------------------------------------------------------------------------------
    //   戻り値   割り振られた音源管理番号
    //----------------------------------------------------------------------------------------------
    public static synchronized  String noteOn(SoundSourceType soundSourceType,
                                                 String pitch,
                                                 int volume) {

        Log.d(CommonUtil.tag(),"noteOn()");
        Log.d(CommonUtil.tag(),"pitch=" + pitch);

        //----------------------------------------------------------------------------------------------
        //自分自身がインスタンス化されていなければ、
        //インスタンス化する。
        //----------------------------------------------------------------------------------------------
        boolean startFlg = false;
        if (me == null) {
            me = new AudioEngine();
            startFlg = true;
        }
        //アクティブな音源を追加
        String key = me.addSoundSource(soundSourceType, pitch, volume);
        Log.d(CommonUtil.tag(),"key=" + key);

        //---------------------------------------------------------------------------------------------
        //　初回の場合（インスタンスを生成した場合は、スレッドスタートする。)
        //---------------------------------------------------------------------------------------------
        if (startFlg) {
            me.start();
        }
        //追加された音源のキーを返却
        return key;
    }
    //----------------------------------------------------------------------------------------------
    //   発音終了
    //----------------------------------------------------------------------------------------------
    //   第１引数 key 割り振られた音源管理番号
    //      引数で与えられたキーに対応する音源の停止を設定する。
    //      すでにAudioEnginが存在しない場合、演奏は完了しているので、何もしない。
    //----------------------------------------------------------------------------------------------
    public static synchronized void noteOff(String key){
        if (me == null) {
            return;
        }
        me.deleteSoundSource(key);
    }
    //----------------------------------------------------------------------------------------------
    // コンストラクタ
    //  プライベート化して、外部からインスタンス化できないようにする。
    //----------------------------------------------------------------------------------------------
    private AudioEngine() {
        //------------------------------------------------------------------------------------------
        // キーの発行
        //   キーは現在時刻(ミリ秒単位)の文字列表現とする。
        //------------------------------------------------------------------------------------------
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        this.myKey = sdf.format(c.getTime());
        //------------------------------------------------------------------------------------------
        // オーディオトラックのインスタンス化
        //------------------------------------------------------------------------------------------
        //バッファの最小値を取得
        int bufferSizeInByte = AudioTrack.getMinBufferSize(
                                            SAMPLE_RATE,
                                            CHANNEL_CONFIG,
                                            AUDIO_FORMAT);
        Log.d(CommonUtil.tag(this),"bufferSizeInByte=" + bufferSizeInByte);

        // AudioTrackをストリームモードで作成
        this.track = new AudioTrack(
                            STREAM_TYPE,
                            SAMPLE_RATE,
                            CHANNEL_CONFIG,
                            AudioFormat.ENCODING_DEFAULT,
                            bufferSizeInByte,    //←バッファサイズを、フォーマット上の最小値に設定
                            AudioTrack.MODE_STREAM);

        //------------------------------------------------------------------------------------------
        // 音源ファイルの初期化
        //------------------------------------------------------------------------------------------
        this.activeNotes = new HashMap<String, ActiveNote>();
        //Audio起動
        this.track.play();
        Log.d(CommonUtil.tag(this),"track.play()");
    }
    //----------------------------------------------------------------------------------------------
    // スレッド開始
    //----------------------------------------------------------------------------------------------
    public void run() {
        Log.d(CommonUtil.tag(this),"run()");
        //------------------------------------------------------------------------------------------
        // 現在設定されている音源があれば、それらを合成してAudioStreamに流し込む。
        // 設定されている音源がなくなったら、Audioを停止し、自分自身の参照も削除して、
        // ガベージコレクション対象とする。
        //------------------------------------------------------------------------------------------
        //ループ中フラグ
        boolean loopOn = true;
        while(loopOn) {
            //メイン処理
            loopOn = this.mainLoop();
        }
        //自分自身の参照を削除
        AudioEngine.me = null;

        //オーディオトラックのリリース
        Log.d(CommonUtil.tag(), "トラックを解放");
        track.stop();
        track.reloadStaticData();
        track.release();
    }
    //----------------------------------------------------------------------------------------------
    // メインループ処理
    //----------------------------------------------------------------------------------------------
    private boolean mainLoop() {
        //Log.d(CommonUtil.tag(this),"mainLoop()");
        //波形データの取得
        byte[] sinWave = this.getAndUpDateSoundSource();

        //波形データが空の場合、終了
        if ((sinWave == null)||(sinWave.length == 0)) {
            Log.d(CommonUtil.tag(this),"ループ終了");
            //ループ終了
            return false;
        }
        //Log.d(CommonUtil.tag(this),"sinWave.length=" + sinWave.length);
        //------------------------------------------------------------------------------------------
        //波形データを設定する。
        // ここでブロックがかかる(待たされる)ので、このメソッドは、sycronyzedにしない。
        //------------------------------------------------------------------------------------------
        /*
        for (byte b : sinWave) {
            Log.d(CommonUtil.tag(this),"sinWave.b=" + b);
        }
        */
        track.write(sinWave, 0, sinWave.length);
        return true;
    }
    //----------------------------------------------------------------------------------------------
    // 波形取得および音源状態更新処理
    //----------------------------------------------------------------------------------------------
    private synchronized byte[] getAndUpDateSoundSource() {
        //Log.d(CommonUtil.tag(this),"getAndUpDateSoundSource()");

        //アクティブなノードが存在しない場合は、終了する。
        int count = this.activeNotes.size();
        if (count == 0) {
            return null;
        }
        //計算結果の入れ物
        int[] waves = new int[loopBuffer];
        /*
        for (int i : waves) {
            Log.d(CommonUtil.tag(this),"waves.i1=" + i);
        }
        */

        //全てのアクティブな波形を処理
        for(Map.Entry<String, ActiveNote> entry : this.activeNotes.entrySet()) {
            //キー
            String key = entry.getKey();
            //実体
            ActiveNote activeNote = entry.getValue();
            //波形の取得
            byte[] targetWave = activeNote.getAndUpdateWaveData();

            /*
            for (int i : targetWave) {
                Log.d(CommonUtil.tag(this),"targetWave.i2=" + i);
            }
            */
            //波形の加算処理
            waves = addWave(waves, targetWave);

            /*
            for (int i : waves) {
                Log.d(CommonUtil.tag(this),"waves.i3=" + i);
            }
            */

            //状況の確認
            boolean toEnd = activeNote.isEnd();
            if (toEnd) {
                //アクティブノードから削除
                this.activeNotes.remove(key);
            }
        }
        //------------------------------------------------------------------------------------------
        // 作成された最終的なwaveデータを返却
        //  TODO: 本来、上限超えた波はきちんと圧縮してやる必要があると思うが、
        //         とりあえずやり方が分からないので、上限値に叩いてしまう。
        //------------------------------------------------------------------------------------------
        byte[] rets = new byte[loopBuffer];
        for (int i = 0; i < rets.length; i++) {
            int wave = waves[i];
            if (Byte.MAX_VALUE <= wave) {
                wave = Byte.MAX_VALUE;
            }
            rets[i] = (byte)wave;
        }
        return rets;
    }
    //----------------------------------------------------------------------------------------------
    //   波形加算
    //     第１引数：加算対象配列
    //     第２引数：加算対象配列
    //     戻り値 加算した結果
    //----------------------------------------------------------------------------------------------
    private int[] addWave(int[]waves, byte[]targetWave) {

        int[] rets = new int[loopBuffer];
        for (int i =0; i < waves.length; i++) {

            //Log.d(CommonUtil.tag(this),"waves[i]=" + waves[i]);
            //Log.d(CommonUtil.tag(this),"targetWave[i]=" + targetWave[i]);

            long l = waves[i] + targetWave[i];

            //Log.d(CommonUtil.tag(this),"l=" + l);

            if (Integer.MAX_VALUE < l ) {
                l = Integer.MAX_VALUE;
            }
            rets[i] = (int)l;
            //Log.d(CommonUtil.tag(this),"rets[i]=" + rets[i]);
        }
        return rets;
    }

    //----------------------------------------------------------------------------------------------
    //   音源追加
    //----------------------------------------------------------------------------------------------
    //   第１引数 soundSourceType 音源の分類
    //   第２引数 pitch 音程
    //   第３引数 volume 音量
    //----------------------------------------------------------------------------------------------
    //   戻り値   割り振られた音源管理番号
    //----------------------------------------------------------------------------------------------
    private synchronized  String addSoundSource(SoundSourceType soundSourceType,
                                                  String pitch,
                                                  int volume) {
        //キー情報
        String noteKey = "";
        if (soundSourceType == SoundSourceType.SINE_WAVE) {
            SineWave sineWave = new SineWave(pitch, volume);
            //キーの算出
            int size = this.activeNotes.size();
            noteKey = myKey + "-" + (size + 1);
            //ノート追加
            this.activeNotes.put(noteKey, sineWave);
        }
        return noteKey;

    }
    //----------------------------------------------------------------------------------------------
    //   音源削除
    //----------------------------------------------------------------------------------------------
    //   第１引数 key 割り振られた音源の管理キー
    //----------------------------------------------------------------------------------------------
    private synchronized void deleteSoundSource(String key) {
        ActiveNote activeNote = this.activeNotes.get(key);
        activeNote.toEnd();
    }

}
