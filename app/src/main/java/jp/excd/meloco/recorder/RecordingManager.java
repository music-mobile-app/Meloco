//--------------------------------------------------------------------------------------------------
// クラス名：録音管理クラス
// 概要    ：録音機能を提供する。
//           シングルトンで動作する。
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.recorder;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import jp.excd.meloco.audio.engine.AudioController;
import jp.excd.meloco.constant.SoundSourceType;
import jp.excd.meloco.presenter.KeyboardKeyPresenter;
import jp.excd.meloco.presenter.KeyboardPresenter;
import jp.excd.meloco.scheduledService.ClickLoopService;
import jp.excd.meloco.scheduledService.RecordingMain;
import jp.excd.meloco.utility.WLog;

public class RecordingManager extends Thread{

    // 自分自身
    private static RecordingManager me = null;

    // １秒間のループ回数
    // (アニメーションが自然に見えるぎりぎりを設定する。)
    private static final double LOOP_NUM = 24.0;

    // スケジュール管理オブジェクト
    private ScheduledFuture<?> scheduledFuture;

    // 鳴らしているクリック音
    private ClickLoop clickLoop;

    // キーボードのプレゼンター
    private KeyboardPresenter keyboardPresenter;

    //----------------------------------------------------------------------------------------------
    // コンストラクタ
    //----------------------------------------------------------------------------------------------
    private RecordingManager() {
    }
    //----------------------------------------------------------------------------------------------
    // 名称   ：インスタンス生成
    //----------------------------------------------------------------------------------------------
    public static synchronized RecordingManager getInstance() {
        if (me == null) {
            me = new RecordingManager();
        }
        return me;
    }
    //----------------------------------------------------------------------------------------------
    // 名称    ：レコーディング開始
    // 処理概要：レコーディングを開始する。
    // 引数1   ：キーボードプレゼンター
    //----------------------------------------------------------------------------------------------
    public void startRec(KeyboardPresenter keyboardPresenter) {

        WLog.d(this, "startRec()");

        // キーボードのプレゼンターの保持
        this.keyboardPresenter = keyboardPresenter;

        me.start();
    }
    //----------------------------------------------------------------------------------------------
    // 名称   ：レコーディングスレッド
    //----------------------------------------------------------------------------------------------
    @Override
    public void run() {

        // コード楽器の波形化
        // (シーケンサーＯＮ)
        //TODO:実装する。

        // カウントダウンの表示
        //TODO:実装する。

        //------------------------------------------------------------------------------------------
        // クリック音の発音
        //------------------------------------------------------------------------------------------
        //TODO:本来シーケンサーに任せるべき
        WLog.d(this, "クリック音を鳴らす");
        this.clickLoop = new ClickLoop();
        this.clickLoop.start();

        //------------------------------------------------------------------------------------------
        // キーボードにロガーを設定
        //------------------------------------------------------------------------------------------
        KeyboardLogger keyboardLogger = new KeyboardLogger();
        this.keyboardPresenter.setKeyboardLogger(keyboardLogger);

        //------------------------------------------------------------------------------------------
        // ループ処理の呼び出し
        // アニメーションが自然に見えるスピードでループを行う。
        //------------------------------------------------------------------------------------------

        // 逆数(ループ間の秒数)
        double interval = 1.0 / LOOP_NUM;

        //マイクロ秒に変換
        int microsec = (int)(interval * 1000 * 1000);

        //サービスの生成
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Date startDate = new Date();
        RecordingMain recordingMain = new RecordingMain(startDate, keyboardLogger);
        this.scheduledFuture = service.scheduleAtFixedRate(recordingMain, 0, microsec, TimeUnit.MICROSECONDS);

        WLog.d(this, "スケジューリング");
    }
    //----------------------------------------------------------------------------------------------
    // 名称   ：レコーディング終了
    //----------------------------------------------------------------------------------------------
    public void stopRec() {

        WLog.d(this, "stopRec()");

        // クリック音の停止
        this.clickLoop.stop();

        // メインループの停止
        this.scheduledFuture.cancel(true);

        // キーボードロガーの削除
        this.keyboardPresenter.deleteKeyboardLogger();

    }
    //----------------------------------------------------------------------------------------------
    // ■公開メソッド
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    // 名称    ：レコーディング開始
    // 処理概要：レコーディング開始
    // 引数1   ：キーボードプレゼンター
    //----------------------------------------------------------------------------------------------
    public static void startRecording(KeyboardPresenter keyboardPresenter) {
        RecordingManager.getInstance();
        me.startRec(keyboardPresenter);
    }
    //----------------------------------------------------------------------------------------------
    // 名称   ：レコーディング停止
    //----------------------------------------------------------------------------------------------
    public static void stopRecording() {
        // TODO 実装する。
        me.stopRec();
    }
}
