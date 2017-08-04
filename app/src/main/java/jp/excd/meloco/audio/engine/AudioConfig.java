//--------------------------------------------------------------------------------------------------
// Audio処理に必要な定数を管理する。
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.audio.engine;


import android.media.AudioFormat;
import android.media.AudioManager;

public class AudioConfig {

    // サンプリングレート
    public static int SAMPLE_RATE = 44100;

    // ストリームタイプ(音楽)
    public static int STREAM_TYPE = AudioManager.STREAM_MUSIC;

    // チャネル定義(モノラル(TODO：後にステレオ化する。)
    public static int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;

    // オーディオフォーマット(8bit(TODO:後に16bit化する。)
    public static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT;

    // ループバッファ(フレーム数)
    public static int LOOP_BUFFER_SIZE = 128;

    // フェードアウトフレーム数
    public static int FADEOUT_FRAME_SIZE = 4410;

    // 元音の振幅倍率(%)
    public static int SOURCE_SOUND_RANGE = 80;

    // リミッティング敷居値
    public static int COMPRESS_BORDER = 80;
}
