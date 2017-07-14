package jp.excd.meloco.presenter;

import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import jp.excd.meloco.MainActivity;
import jp.excd.meloco.R;
import jp.excd.meloco.constant.KeyboardType;

public class KeyboardPresenter {

    //クラス名(ログ出力用)
    String CLASS = getClass().getSimpleName();

    //キーボードの全キーのPresenterの配列
    private List<KeyboardKeyPresenter> keyboardKeyPresenters;

    //-------------------------------------------
    // コンストラクタ
    // 第１引数：キーボードのタイプ
   //-------------------------------------------
    public KeyboardPresenter(KeyboardType keyboardType) {
        //-------------------------------------------------------------------------
        //管理すべきキーをすべてリスト化する。
        //-------------------------------------------------------------------------
        this.keyboardKeyPresenters = new ArrayList<KeyboardKeyPresenter>();

        if (keyboardType == KeyboardType.DOUBLE) {
            this.setKeyboadListDouble();
        }
    }
    //-------------------------------------------------------------------------
    // キーボードのリスト化（２段構え用)
    //-------------------------------------------------------------------------
    public void setKeyboadListDouble() {

        KeyboardKeyPresenter keyboardKeyPresenter = null;

        //-------------------------------------------------------------------------
        //　下段の白鍵
        //-------------------------------------------------------------------------
        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerC2, "C2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerD2, "D2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerE2, "E2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerF2, "F2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerG2, "G2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerA2, "A2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerB2, "B2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerC3, "C3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerD3, "D3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerE3, "E3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerF3, "F3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerG3, "G3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerA3, "A3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerB3, "B3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerC4, "C4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerD4, "D4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerE4, "E4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerF4, "F4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerG4, "G4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerA4, "A4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerB4, "B4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerC5, "C5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerD5, "D5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerE5, "E5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerF5, "F5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerG5, "G5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerA5, "A5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerB5, "B5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerC6, "C6", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        //-------------------------------------------------------------------------
        //　下段の黒鍵
        //-------------------------------------------------------------------------
        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerCS2, "CS2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerDS2, "DS2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerFS2, "FS2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerGS2, "GS2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerAS2, "AS2", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerCS3, "CS3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerDS3, "DS3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerFS3, "FS3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerGS3, "GS3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerAS3, "AS3", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerCS4, "CS4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerDS4, "DS4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerFS4, "FS4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerGS4, "GS4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerAS4, "AS4", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerCS5, "CS5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerDS5, "DS5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerFS5, "FS5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerGS5, "GS5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonLowerAS5, "AS5", KeyboardKeyPresenter.LOWER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);


        //-------------------------------------------------------------------------
        //　上段の白鍵
        //-------------------------------------------------------------------------
        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperC3, "C3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperD3, "D3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperE3, "E3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperF3, "F3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperG3, "G3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperA3, "A3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperB3, "B3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperC4, "C4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperD4, "D4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperE4, "E4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperF4, "F4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperG4, "G4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperA4, "A4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperB4, "B4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperC5, "C5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperD5, "D5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperE5, "E5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperF5, "F5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperG5, "G5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperA5, "A5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperB5, "B5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperC6, "C6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperD6, "D6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperE6, "E6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperF6, "F6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperG6, "G6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperA6, "A6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperB6, "B6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperC7, "C7", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        //-------------------------------------------------------------------------
        //　上段の黒鍵
        //-------------------------------------------------------------------------
        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperCS3, "CS3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperDS3, "DS3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperFS3, "FS3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperGS3, "GS3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperAS3, "AS3", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperCS4, "CS4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperDS4, "DS4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperFS4, "FS4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperGS4, "GS4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperAS4, "AS4", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperCS5, "CS5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperDS5, "DS5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperFS5, "FS5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperGS5, "GS5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperAS5, "AS5", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperCS6, "CS6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperDS6, "DS6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperFS6, "FS6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperGS6, "GS6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

        keyboardKeyPresenter = new KeyboardKeyPresenter(R.id.imageButtonUpperAS6, "AS6", KeyboardKeyPresenter.UPPER);
        this.keyboardKeyPresenters.add(keyboardKeyPresenter);

    }
    //----------------------------------------------------------------
    // リスナー登録
    //----------------------------------------------------------------
    public void setListner(MainActivity activity) {
        //各キーボードのインスタンスを取得して、リスナー登録していく。
        for (KeyboardKeyPresenter k : this.keyboardKeyPresenters) {
            //idの取得
            int viewId = k.getViewId();
            //ボタンのインスタンスを取得
            ImageButton ib = (ImageButton)activity.findViewById(viewId);
            //リスナー登録
            ib.setOnTouchListener((View.OnTouchListener)k);
        }
    }
}
