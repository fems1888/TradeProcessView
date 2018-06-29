package com.example.jackieyao.tradeprocessview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

/**
 * @author Created by jackieyao on 2018/6/26 下午5:07.
 */

public class CustomerLayout extends LinearLayout {
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
                tradeProcessView.anim(1f);
//                ObjectAnimator animator = ObjectAnimator.ofFloat(tradeProcessView, "process", 0, 0.7f*tradeProcessView.getWidth());
//                animator.setDuration(1000L);
//                animator.setInterpolator(new FastOutSlowInInterpolator());
//                animator.start();
            }
        });

        AppCompatSeekBar seekBar = findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tradeProcessView.seekBar(i/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
