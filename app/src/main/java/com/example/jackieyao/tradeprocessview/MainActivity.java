package com.example.jackieyao.tradeprocessview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MySurfaceView mySurfaceView = new MySurfaceView(this);
//        mySurfaceView.setRotation(45.0f);
//        setContentView(mySurfaceView);

        final TradeProcessView processView = findViewById(R.id.view);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processView.anim();
            }
        });
    }
}