package com.wys.customviewset;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by yas on 2018/7/18.
 * 随机图形验证码view
 */

public class RandomCodeView extends View {
    private final String TAG="RandomCodeView";
    private int mHeight;
    private int mWidth;
    private int mTextSize;
    private Random mRandom;
    private int mBgColor;
    //随机码，默认为4位
    private char[] mCodes=new char[4];
    //随机码颜色
    private int[] mColors=new int[4];
    //字体的y位置
    private float[] mYs=new float[4];
    //是否已经初始化数据的flag
    private boolean flag=false;
    //是否点击刷新随机码
    private boolean mIsOnclickRefresh=false;
    //画笔
    private Paint mPaint;
    private char randomText;

    public RandomCodeView(Context context) {
        this(context,null);
    }

    public RandomCodeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RandomCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint=new Paint();
    }

    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高的size和mode
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);

        //处理wrap_content问题
        if (widthSpecMode==MeasureSpec.AT_MOST&&heightSpecMode==MeasureSpec.AT_MOST){
            //如果宽高都为wrap_content，则设置默认宽高
            setMeasuredDimension(200,100);
        }else if (widthSpecMode==MeasureSpec.AT_MOST){
            //如果只有宽度为wrap_content，则根据高度大小来设置宽度
            setMeasuredDimension(heightSpecSize*2,heightSpecSize);
        }else if (heightSpecMode==MeasureSpec.AT_MOST){
            //反之，如果只有高度为wrap_content，则根据高度大小来设置宽度
            setMeasuredDimension(widthSpecSize,widthSpecSize/2);
        }

        //宽高设为最终测量宽高
        mHeight=getMeasuredHeight();
        mWidth=getMeasuredWidth();
        //根据控件宽度设置字体大小
        mTextSize= (int) (mWidth/4.5);
        //如果高度小于字体大小，则字体高度设置为view高度的一半，防止字体超出view大小
        if (mHeight<mTextSize){
            mTextSize= (int) (0.5*mHeight);
        }
    }

    /**
     * 画View
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //去锯齿
        mPaint.setAntiAlias(true);

        /**先初始化字体大小**/
        //设置字体大小，单位px
        mPaint.setTextSize(mTextSize);
        //设置笔触宽度
        mPaint.setStrokeWidth(3);
        //设置阴影为null
        mPaint.setShader(null);

        Paint.FontMetrics fontMetrics=mPaint.getFontMetrics();
        //获取字体高度
        float textHeight=Math.abs(fontMetrics.ascent)+fontMetrics.descent;

        /**初始化随机位置**/
        if (!flag){
            //初始化各种随机参数，定义flag，防止频繁调用onDraw刷新数据
            init(fontMetrics,mHeight);
        }

        //设置背景色
        setBackgroundColor(mBgColor);

        //起始x位置
        String drawText="A B C D";
        float startX=(getWidth()-mPaint.measureText(drawText))/2;
        //画四个随机字母，每个字母随机颜色
        for (int i=0;i<4;i++){
            mPaint.setColor(mColors[i]);
            float x=startX+i*mPaint.measureText("A ");
            if (i==3){
                canvas.drawText(String.valueOf(mCodes[i]),x,mYs[i],mPaint);
            }else{
                canvas.drawText(mCodes[i]+" ",x,mYs[i],mPaint);
            }
        }

        //画三条干扰线，颜色和位置也可以提前初始化
        for (int i=0;i<3;i++){
            mPaint.setColor(getTextRandomColor());
            canvas.drawLine(0,mRandom.nextInt(mHeight),mWidth,mRandom.nextInt(mHeight),mPaint);
        }

        //画20个干扰点
        mPaint.setStrokeWidth(8);
        for(int i=0;i<20;i++){
            mPaint.setColor(getTextRandomColor());
            canvas.drawPoint(mRandom.nextInt(mWidth),mRandom.nextInt(mHeight),mPaint);
        }
    }

    /**
     * 初始化随机码
     *
     * @param fontMetrics
     * @param mHeight
     */
    private void init(Paint.FontMetrics fontMetrics, int mHeight) {
        mRandom = new Random(System.currentTimeMillis());
        mBgColor = getBgRandomColor();

        //获取随机码Y位置
        for (int i = 0; i < 4; i++) {
            mYs[i] = getRandomY(fontMetrics, mHeight);
            mCodes[i] = getRandomText();
            mColors[i]=getTextRandomColor();
        }

        //点击事件
        if (mIsOnclickRefresh){
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清空数据
                    flag=false;
                    //重绘
                    invalidate();
                }
            });
        }
        flag=true;
    }

    /**
     * 获取随机码的随机颜色
     * @return
     */
    private int getTextRandomColor() {
        int r = mRandom.nextInt(90) + 40;
        int g = mRandom.nextInt(90) + 40;
        int b = mRandom.nextInt(90) + 40;
        return Color.rgb(r, g, b);
    }

    /**
     * 获取随机码Y位置
     * @param fontMetrics
     * @param mHeight
     * @return
     */
    private float getRandomY(Paint.FontMetrics fontMetrics, int mHeight) {
        int min= (int) (mHeight-Math.abs(fontMetrics.ascent)-fontMetrics.descent);
        return mRandom.nextInt(min)+Math.abs(fontMetrics.ascent);
    }

    /**
     * 获取随机背景
     * @return
     */
    private int getBgRandomColor() {
        int r=mRandom.nextInt(140)+115;
        int g=mRandom.nextInt(140)+115;
        int b=mRandom.nextInt(140)+115;
        return Color.rgb(r,g,b);
    }

    /**
     * 获取随机码
     * @return
     */
    public char getRandomText() {
        int i=mRandom.nextInt(42)+48;
        while (i>57&&i<65){
            i=mRandom.nextInt(42)+48;
        }
        char tmp= (char) i;
        return tmp;
    }
}
