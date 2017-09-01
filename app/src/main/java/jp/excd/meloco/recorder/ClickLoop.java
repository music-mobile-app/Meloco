//--------------------------------------------------------------------------------------------------
// クリック音をループで鳴らし続ける為のクラス
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.recorder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import jp.excd.meloco.scheduledService.ClickLoopService;
import jp.excd.meloco.settings.SongSettings;
import jp.excd.meloco.utility.WLog;

public class ClickLoop {

    // スケジュール管理オブジェクト
    private ScheduledFuture<?> scheduledFuture;
    //----------------------------------------------------------------------------------------------
    // 名称    ：クリック音発音スタート
    // 処理概要：クリック音の発音をスタートする。
    //----------------------------------------------------------------------------------------------
    public void start() {
        WLog.d(this, "start()");

        // デフォルトのBPM数を取得する。
        int defaultBPM = SongSettings.getDefaultBPM();

        // TODO: 本来、BPMは曲中で変更されるので、シーケンサーの仕事になる。
        // BPMより一秒間に発音する回数を取得
        double dBPM = (double)defaultBPM;
        double dByousu = 60.0;
        double kaisu =  dBPM / dByousu;
        WLog.d(this, "kaisu=" + kaisu);

        //逆数(発音と発音の間の秒数)
        double interval = 1.0 / kaisu;

        //マイクロ秒に変換
        int microsec = (int)(interval * 1000 * 1000);

        //サービスの生成
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        ClickLoopService clickLoopService = new ClickLoopService();
        this.scheduledFuture = service.scheduleAtFixedRate(clickLoopService, 0, microsec, TimeUnit.MICROSECONDS);

        WLog.d(this, "スケジューリング");
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：クリック音発音停止
    // 処理概要：クリック音の発音を停止する。
    //----------------------------------------------------------------------------------------------
    public void stop() {
        WLog.d(this, "stop()");
        this.scheduledFuture.cancel(true);
    }
}
