package jp.excd.meloco.sample;

import jp.excd.meloco.audio.engine.AudioConfig;
import jp.excd.meloco.audio.engine.AudioController;
import jp.excd.meloco.constant.SoundSourceType;
import jp.excd.meloco.utility.WLog;

public class AudioThread001 extends Thread {
    public void run() {
        WLog.d("クリック音を４回鳴らす");

        //BPMより、音を鳴らす感覚をミリ秒単位で算出する。
        int interval = 60 * 1000 / AudioConfig.BPM;
        for (int i = 0; i < 16; i++) {
            //クリック音を鳴らす。
            AudioController.noteOn(SoundSourceType.CLICK2, null, (20 + i * 5));

            try {
                Thread.sleep(interval);
            }catch (InterruptedException e) {
                //何もしない。
            }
        }
    }
}
