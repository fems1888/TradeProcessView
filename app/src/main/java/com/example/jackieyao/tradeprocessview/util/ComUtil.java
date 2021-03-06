package com.example.jackieyao.tradeprocessview.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author Created by jackieyao on 2018/6/25 下午3:55.
 */

public class ComUtil {
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }
    public static String format(Object... objects){
        StringBuilder builder = new StringBuilder();
        for(Object o : objects){
            if (o!= null){
                builder.append(o);
            }
        }
        return builder.toString();
    }
}
