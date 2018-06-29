简介
====
* 简单的购物，活动等详情界面的进度条;。
* 目前只是第一版本，后续会继续完善


项目链接 [TradeProcessView](https://github.com/fems1888/TradeProcessView)
===
* 如果你感觉到对你有帮助，欢迎star
* 如果你感觉对代码有疑惑，或者需要修改的地方，欢迎issue

主要特性
===
* 简单,轻量。你可以将相关代码直接导入到自己的项目里，不需要导入第三方库（有的库很大，不值得！）。而且在此基础上你可以继续增加代码，做出更加符合你需求的效果
* 下面是TradeProcessView相关的属性，注释很详细，可以根据自己的需要随意修改
```java
<declare-styleable name="TradeProcessView">
        <!--进度条控件的圆角 大于0就是有圆角 等于0就是平角-->
        <attr name="roundRadius" format="dimension"/>

        <!--进度条控件外围是否有描边-->
        <attr name="mNeedStoke" format="boolean"/>

        <!--进度条控件的秒边的颜色-->
        <attr name="mStrokeColor" format="color"/>

        <!--进度条控件的秒边的宽度-->
        <attr name="mStrokeWidth" format="dimension"/>

        <!--进度条控件的进度是直接显示还是动画显示-->
        <attr name="mNeedAnim" format="boolean"/>

        <!--进度条控件的进度(secondaryProcess)是平角的  还是圆角的-->
        <attr name="mIsRoundProcess" format="boolean"/>

        <!--进度条控件的背景颜色-->
        <attr name="mbgColor" format="color"/>

        <!--进度条控件的进度（secondaryProcess）的颜色-->
        <attr name="mProcessColor" format="color"/>

        <!--进度条控件的文字颜色-->
        <attr name="mTextColor" format="color"/>

        <!--进度条控件的文字大小-->
        <attr name="mTextSize" format="dimension"/>

        <!--进度条控件的文字内容-->
        <attr name="mPromptText" format="string"/>


        <!--进度条控件结束的文字内容-->
        <attr name="mPromptTextEnd" format="string"/>

        <!--是否需要百分号-->
        <attr name="mNeedPercent" format="boolean"/>
    </declare-styleable>
```
使用方法
 ===
XML布局
--

* 新建XML布局
```Java

 <com.example.jackieyao.tradeprocessview.TradeProcessView
            android:id="@+id/view"
            android:layout_width="150dp"
            android:layout_height="50dp"
            android:layout_marginTop="10dp"
            android:layout_below="@id/btn"
            app:mNeedStoke="false"
            app:mIsRoundProcess="false"
            app:mNeedAnim="true"
            app:mTextSize="20dp"
            app:mStrokeWidth="1dp"
            app:mTextColor="#FFFFFF"
            app:mNeedPercent="true"
            app:mPromptText="剩余"
            app:mPromptTextEnd="已售罄"
            app:mStrokeColor="#F8E71C"
            />
            
```


JAVA代码
=====
--
* activity里
上面XML里的属性，在activity界面都可以直接用代码设置。setter/getter方法

修改不同的属性大致可以得到以下的效果
====
* ![](http://g.recordit.co/qTbaVoUzYK.gif) 
* ![](http://g.recordit.co/xpvvWxdXln.gif) 
* ![](http://g.recordit.co/dO5SZ9POm9.gif) 
* ![](http://g.recordit.co/ySUKBzCJ4h.gif) 

   
   






