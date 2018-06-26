package com.example.jackieyao.tradeprocessview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.example.jackieyao.tradeprocessview.util.ComUtil;


/**
 * @author Created by jackieyao on 2018/6/25 下午1:58.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    private SurfaceHolder mSurfaceHolder;
    private Path mPath;
    private Paint mPaint;
    private Paint mTextPaint;
    /***
     * 是否在绘制:用于关闭子线程:true则表示一直循环
     */
    private boolean isDrawing = true;
    private int drawX;
    private int drawY;

    public MySurfaceView(Context context) {
        this(context,null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(30);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.GREEN);
        //连接处更加平滑
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPath = new Path();
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDrawing = false;
    }

    @Override
    public void run() {

        while (isDrawing) {
            drawX++;
            drawY = (int) (100 * Math.sin(drawX * 2 * Math.PI / 180) + 400);
            mPath.lineTo(drawX, drawY);
            mDraw(mPath,drawX,drawY);
        }
    }

    private void mDraw(Path mPath, int drawX, int drawY) {
        Canvas canvas = null;
        canvas = mSurfaceHolder.lockCanvas();
        if (canvas!=null){
            canvas.drawColor(Color.WHITE);
            canvas.drawPath(mPath,mPaint);
            canvas.drawText("X:"+drawX+" Y:"+drawY,drawX,drawY,mTextPaint);
//            canvas.drawCircle(drawX,drawY,30,mPaint);
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            if (drawX>ComUtil.getScreenWidth(getContext())-200){
                isDrawing = false;
            }
        }
    }
}
