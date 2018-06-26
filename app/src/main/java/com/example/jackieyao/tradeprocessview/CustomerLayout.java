package com.example.jackieyao.tradeprocessview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * @author Created by jackieyao on 2018/6/26 下午5:07.
 */

public class CustomerLayout extends RelativeLayout {
    private Button button;
    TradeProcessView tradeProcessView;

    public CustomerLayout(Context context) {
        super(context);
    }

    public CustomerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        button = findViewById(R.id.btn);
        tradeProcessView = findViewById(R.id.view);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                tradeProcessView.anim(0.7f);
                ObjectAnimator animator = ObjectAnimator.ofFloat(tradeProcessView, "process", 0, 0.7f*tradeProcessView.getWidth());
                animator.setDuration(1000L);
                animator.setInterpolator(new FastOutSlowInInterpolator());
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setRepeatMode(ValueAnimator.RESTART);
                animator.start();
            }
        });
    }
}
