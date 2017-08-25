package jp.excd.meloco.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import jp.excd.meloco.utility.WLog;

public class SurfaceViewScore extends SurfaceView implements SurfaceHolder.Callback {

    public SurfaceViewScore(Context context) {
        super(context);
        initial();
    }
    //----------------------------------------------------------------------------------------------
    // 名称     : コンストラクタ
    // 処理概要 : レイアウト用のxmlファイルに記述したタグから自動的にインスタンスを生成する際に、
    //            このコンストラクタが使われる。
    // 引数１   :
    // 引数２   :
    //----------------------------------------------------------------------------------------------
    public SurfaceViewScore(Context context, AttributeSet attrs) {
        super(context,attrs);
        initial();
    }
    public SurfaceViewScore(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initial();
    }
    //----------------------------------------------------------------------------------------------
    // 名称     : サーフェイス初期化
    // 処理概要 : サーフェイスのCallBackに自分自身を設定する。
    //            これにより、サーフェイスの生成、変更、破棄を受け取れる。
    // 引数１   :
    //----------------------------------------------------------------------------------------------
    private void initial() {
        WLog.d(this, "initial()");
        SurfaceHolder holder = this.getHolder();
        holder.addCallback(this);
    }

    @Override
    //----------------------------------------------------------------------------------------------
    // 名称     : サーフェイス生成
    // 処理概要 : サーフェイスが生成された時に呼び出される。
    // 引数１   :
    //----------------------------------------------------------------------------------------------
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        draw(surfaceHolder);
    }

    @Override
    //----------------------------------------------------------------------------------------------
    // 名称     : サーフェイス変更
    // 処理概要 : サーフェイスが変更されるごとに呼び出される。
    // 引数１   :
    // 引数２   :
    // 引数３   :
    // 引数４   :
    //----------------------------------------------------------------------------------------------
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override
    //----------------------------------------------------------------------------------------------
    // 名称     : サーフェイス破棄
    // 処理概要 : サーフェイスが破棄された時に呼び出される。
    // 引数１   :
    //----------------------------------------------------------------------------------------------
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
    //----------------------------------------------------------------------------------------------
    // 名称     : 描画
    // 処理概要 : 描画処理の実装
    // 引数１   : SurfaceHolderのインスタンス
    //----------------------------------------------------------------------------------------------
    public void draw(SurfaceHolder holder) {

        WLog.d(this, "draw()");

        // キャンバスの取得およびロック
        Canvas c = holder.lockCanvas();

        // キャンバスの色設定
        c.drawColor(Color.WHITE);

        // Paintの生成
        Paint p = new Paint();

        // 描画色の設定
        p.setColor(Color.BLACK);

        // 描画
        //float drawX = 50f;
        //float drawY = 50f;
        //c.drawCircle(drawX,drawY, 50, p);

        RectF rectF = new RectF(0,0,100,5);
        c.drawRect(rectF, p);

        rectF = new RectF(0,10,200,15);
        c.drawRect(rectF, p);

        rectF = new RectF(0,20,300,25);
        c.drawRect(rectF, p);

        rectF = new RectF(0,30,400,35);
        c.drawRect(rectF, p);

        rectF = new RectF(0,40,500,45);
        c.drawRect(rectF, p);

        rectF = new RectF(0,50,600,55);
        c.drawRect(rectF, p);

        rectF = new RectF(0,60,700,65);
        c.drawRect(rectF, p);

        rectF = new RectF(0,70,800,75);
        c.drawRect(rectF, p);

        rectF = new RectF(0,80,900,85);
        c.drawRect(rectF, p);

        rectF = new RectF(0,90,1000,95);
        c.drawRect(rectF, p);

        //キャンバスロック解除
        holder.unlockCanvasAndPost(c);

    }
}
