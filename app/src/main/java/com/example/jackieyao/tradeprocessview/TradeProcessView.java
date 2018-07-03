package com.example.jackieyao.tradeprocessview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.jackieyao.tradeprocessview.util.ComUtil;

import java.math.BigDecimal;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author Created by jackieyao on 2018/6/21 下午6:23.
 */

public class TradeProcessView extends View {
    private Paint mPaint;
    private Path mPath;
    /**
     * 绘制范围裁切path
     */
    private Path mPathClip;
    private float radii[];
    private float roundRadius;
    /**
     * 是否需要描边
     */
    private boolean mNeedStoke;

    /**
     * 是否需要动画
     */
    private boolean mNeedAnim;
    private boolean mNeedPercent;


    private int mStrokeColor;
    private int mbgColor;
    private int mProcessColor;
    private int mTextColor;
    private float mTextSize;
    private float mStrokeWidth;
    private String mPromptText;
    private String mPromptTextEnd;
    private PorterDuffXfermode xfermode;

    public TradeProcessView(Context context) {
        this(context, null);
    }

    public TradeProcessView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TradeProcessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TradeProcessView, defStyleAttr, 0);
        roundRadius = ta.getDimension(R.styleable.TradeProcessView_roundRadius, 40);
        mNeedStoke = ta.getBoolean(R.styleable.TradeProcessView_mNeedStoke, false);
        mNeedAnim = ta.getBoolean(R.styleable.TradeProcessView_mNeedAnim, false);
        mNeedPercent = ta.getBoolean(R.styleable.TradeProcessView_mNeedPercent, true);
        //进度条的终端是圆弧的还是平角的
        boolean mIsRoundProcess = ta.getBoolean(R.styleable.TradeProcessView_mIsRoundProcess, false);
        mStrokeColor = ta.getColor(R.styleable.TradeProcessView_mStrokeColor, Color.parseColor("#FF5C8B"));
        mbgColor = ta.getColor(R.styleable.TradeProcessView_mbgColor, Color.parseColor("#40FF5C8B"));
        mProcessColor = ta.getColor(R.styleable.TradeProcessView_mProcessColor, Color.parseColor("#FF5C8B"));
        mTextColor = ta.getColor(R.styleable.TradeProcessView_mTextColor, Color.WHITE);
        mTextSize = ta.getDimension(R.styleable.TradeProcessView_mTextSize, 40);
        mStrokeWidth = ta.getDimension(R.styleable.TradeProcessView_mStrokeWidth, 8);
        mPromptText = ta.getString(R.styleable.TradeProcessView_mPromptText);
        mPromptTextEnd = ta.getString(R.styleable.TradeProcessView_mPromptTextEnd);
        ta.recycle();
        if (mIsRoundProcess) {
            radii = new float[]{roundRadius, roundRadius, roundRadius, roundRadius, roundRadius, roundRadius, roundRadius, roundRadius};
        } else {
            radii = new float[]{roundRadius, roundRadius, 0, 0, 0, 0, roundRadius, roundRadius};
        }
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();
        mPathClip = new Path();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mbgColor);
        canvas.drawRoundRect(mStrokeWidth, mStrokeWidth, getWidth() - mStrokeWidth, getHeight() - mStrokeWidth, roundRadius, roundRadius, mPaint);

        canvas.save();
        mPathClip.addRoundRect(mStrokeWidth, mStrokeWidth, getWidth() - mStrokeWidth, getHeight() - mStrokeWidth, roundRadius, roundRadius, Path.Direction.CW);

        canvas.clipPath(mPathClip);
        mPaint.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(mProcessColor);
        float right=0;
        //如果path不设置reset  那么就不能实现重复动画   因为已经有path路劲填充了
        mPath.reset();
        if (mNeedAnim) {
            right = process - mStrokeWidth;
        } else {
            right = process * getWidth() - mStrokeWidth;
        }
        mPath.addRoundRect(mStrokeWidth, mStrokeWidth, right, (getHeight() - mStrokeWidth), radii, Path.Direction.CW);

        canvas.drawPath(mPath, mPaint);
        canvas.restore();


        if (mNeedStoke) {
            mPaint.reset();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            mPaint.setColor(mStrokeColor);
            canvas.drawRoundRect(mStrokeWidth, mStrokeWidth, getWidth() - mStrokeWidth, getHeight() - mStrokeWidth, roundRadius, roundRadius, mPaint);

        }

        mPaint.reset();
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(mTextSize);
        String format;
        if (process == 0 ){
            format = ComUtil.format(mPromptTextEnd);
        }else {
            format = mNeedAnim ? ComUtil.format(mPromptText, new BigDecimal(((int) process * 100 / getWidth())).stripTrailingZeros().toPlainString(), mNeedPercent?"%":"") : ComUtil.format(mPromptText, new BigDecimal(process * 100).intValue(), mNeedPercent?"%":"");
        }

        Bitmap textBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);

        float textWidth = mPaint.measureText(format);
        float ascent = mPaint.ascent();
        float descent = mPaint.descent();
        float textHeight = ascent + descent;

        float x = (getWidth() - textWidth) / 2;
        float y = (getHeight() - textHeight) / 2;

        textCanvas.drawText(format, x, y, mPaint);


        mPaint.setXfermode(xfermode);

        mPaint.setColor(Color.WHITE);
        textCanvas.drawPath(mPath, mPaint);
        //为空时因为textbitmap 已经有Paint画出来了
        canvas.drawBitmap(textBitmap,0,0,null);

        mPaint.setXfermode(null);


    }

    private float process;

    public float getProcess() {
        return process;
    }

    public void setProcess(float process) {
        this.process = process;
        postInvalidate();
    }

    public void seekBar(float process) {
        mNeedAnim = false;
        this.process = process;
        postInvalidate();
    }


    public void anim(float process) {


        if (!mNeedAnim) {
            setProcess(process);
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "process", 0, getWidth() * process);
        animator.setDuration(1600L);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(4);
        animator.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        int width;
        int height;
        if (layoutParams.width == WRAP_CONTENT) {
            width = 200;
        } else {
            width = layoutParams.width;
        }

        if (layoutParams.height == WRAP_CONTENT) {
            height = 100;
        } else {
            height = layoutParams.height;
        }

        width = resolveSize(width, widthMeasureSpec);
        height = resolveSize(height, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public float getRoundRadius() {
        return roundRadius;
    }

    public void setRoundRadius(float roundRadius) {
        this.roundRadius = roundRadius;
    }

    public boolean ismNeedStoke() {
        return mNeedStoke;
    }

    public void setmNeedStoke(boolean mNeedStoke) {
        this.mNeedStoke = mNeedStoke;
    }

    public boolean ismNeedAnim() {
        return mNeedAnim;
    }

    public void setmNeedAnim(boolean mNeedAnim) {
        this.mNeedAnim = mNeedAnim;
    }

    public boolean ismNeedPercent() {
        return mNeedPercent;
    }

    public void setmNeedPercent(boolean mNeedPercent) {
        this.mNeedPercent = mNeedPercent;
    }

    public int getmStrokeColor() {
        return mStrokeColor;
    }

    public void setmStrokeColor(int mStrokeColor) {
        this.mStrokeColor = mStrokeColor;
    }

    public int getMbgColor() {
        return mbgColor;
    }

    public void setMbgColor(int mbgColor) {
        this.mbgColor = mbgColor;
    }

    public int getmProcessColor() {
        return mProcessColor;
    }

    public void setmProcessColor(int mProcessColor) {
        this.mProcessColor = mProcessColor;
    }

    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public float getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public float getmStrokeWidth() {
        return mStrokeWidth;
    }

    public void setmStrokeWidth(float mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    public String getmPromptText() {
        return mPromptText;
    }

    public void setmPromptText(String mPromptText) {
        this.mPromptText = mPromptText;
    }

    public String getmPromptTextEnd() {
        return mPromptTextEnd;
    }

    public void setmPromptTextEnd(String mPromptTextEnd) {
        this.mPromptTextEnd = mPromptTextEnd;
    }

}
