//--------------------------------------------------------------------------------------------------
// Click音をループで鳴らし続けるサービス
//--------------------------------------------------------------------------------------------------
package jp.excd.meloco.scheduledService;

import jp.excd.meloco.audio.engine.AudioController;
import jp.excd.meloco.constant.SoundSourceType;
import jp.excd.meloco.utility.WLog;

public class ClickLoopService extends Thread {

    //----------------------------------------------------------------------------------------------
    // クリック音を鳴らす。
    //----------------------------------------------------------------------------------------------
    public void run() {
        WLog.d("this", "run()");
        WLog.d("this", "クリック音を鳴らす。");
        String noteNo = AudioController.noteOn(SoundSourceType.CLICK,"", 64);
    }
}
