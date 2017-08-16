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

    // オーディオフォーマット(16bit)
    public static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    //public static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT;

    // ループバッファ(フレーム数)
    public static int LOOP_BUFFER_SIZE = 998;

    // フェードインフレーム数
    public static int FADEIN_FRAME_SIZE = 340;

    // フェードアウトフレーム数
    public static int FADEOUT_FRAME_SIZE = 780;

    // 元音の振幅倍率(%)
    public static int SOURCE_SOUND_RANGE = 25;

    // リミッティング敷居値
    public static int COMPRESS_BORDER = 80;

    // AudioTrakWrapperのプライオリティー
    public static int AUDIO_TRACK_WRAPPER_PRIORITY = Thread.MAX_PRIORITY;

    // WaveManagerのプライオリティー
    public static int WAVE_MANAGER_PRIORITY = Thread.MAX_PRIORITY;
}
