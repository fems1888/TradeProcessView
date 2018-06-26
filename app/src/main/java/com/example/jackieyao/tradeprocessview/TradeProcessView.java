package com.example.jackieyao.tradeprocessview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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


    private int mStrokeColor;
    private int mbgColor;
    private int mProcessColor;
    private int mTextColor;
    private float mTextSize;
    private float mStrokeWidth;
    private String mPromptText;

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

    public String getmPromptText() {
        return mPromptText;
    }

    public void setmPromptText(String mPromptText) {
        this.mPromptText = mPromptText;
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TradeProcessView, defStyleAttr, 0);
        roundRadius = ta.getDimension(R.styleable.TradeProcessView_roundRadius, 40);
        mNeedStoke = ta.getBoolean(R.styleable.TradeProcessView_mNeedStoke, false);
        mNeedAnim = ta.getBoolean(R.styleable.TradeProcessView_mNeedAnim, false);
        //进度条的终端是圆弧的还是平角的
        boolean mIsRoundProcess = ta.getBoolean(R.styleable.TradeProcessView_mIsRoundProcess, false);
        mStrokeColor = ta.getColor(R.styleable.TradeProcessView_mStrokeColor, Color.parseColor("#FF5C8B"));
        mbgColor = ta.getColor(R.styleable.TradeProcessView_mbgColor, Color.parseColor("#40FF5C8B"));
        mProcessColor = ta.getColor(R.styleable.TradeProcessView_mProcessColor, Color.parseColor("#FF5C8B"));
        mTextColor = ta.getColor(R.styleable.TradeProcessView_mTextColor, Color.WHITE);
        mTextSize = ta.getDimension(R.styleable.TradeProcessView_mTextSize, 40);
        mStrokeWidth = ta.getDimension(R.styleable.TradeProcessView_mStrokeWidth, 8);
        mPromptText = ta.getString(R.styleable.TradeProcessView_mPromptText);
        ta.recycle();
        if (mIsRoundProcess) {
            radii = new float[]{roundRadius, roundRadius, roundRadius, roundRadius, roundRadius, roundRadius, roundRadius, roundRadius};
        } else {
            radii = new float[]{roundRadius, roundRadius, 0, 0, 0, 0, roundRadius, roundRadius};
        }

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
        float right;
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
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);

        String format = mNeedAnim ? ComUtil.format(mPromptText, new BigDecimal(((int) process * 100 / getWidth())).stripTrailingZeros().toPlainString(), "%") : ComUtil.format(mPromptText, new BigDecimal(process * 100).intValue(), "%");
        float textWidth = mPaint.measureText(format);
        float ascent = mPaint.ascent();
        float descent = mPaint.descent();
        float textHeight = ascent + descent;

        float x = (getWidth() - textWidth) / 2;
        float y = (getHeight() - textHeight) / 2;
        canvas.drawText(format, x, y, mPaint);


    }

    private float process;

    public float getProcess() {
        return process;
    }

    public void setProcess(float process) {
        this.process = process;
        postInvalidate();
    }


    public void anim(float process) {


        if (!mNeedAnim) {
            setProcess(process);
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "process", 0, getWidth() * process);
        animator.setDuration(1000L);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
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
}
