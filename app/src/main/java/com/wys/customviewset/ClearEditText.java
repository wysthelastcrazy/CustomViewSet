package com.wys.customviewset;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by yas on 2018/7/18.
 * 带清除功能的EditText
 */

public class ClearEditText extends android.support.v7.widget.AppCompatEditText{
    private Drawable mRightDrawable;
    private boolean isHasFocus;
    public ClearEditText(Context context) {
        this(context,null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        //getCompoundDrawables:
        //returns drawables for the left,top,right,and bottom borders
//        Drawable[] drawables=this.getCompoundDrawables();

        //取得right位置的drawable
        //即我们在布局文件中设置的android：drawableRight
//        mRightDrawable=drawables[2];
        mRightDrawable=getContext().getResources().getDrawable(R.mipmap.search_clear_normal);
        //设置焦点变化的监听
        this.setOnFocusChangeListener(new FocusChangeListenerImpl());

        //设置EditText文字变化的监听
        this.addTextChangedListener(new TextWatcherImp());

        //初始化时让右边clean图标不可见
        setClearDrawableVisible(false);
    }

    /**
     * 当手指抬起的位置在clean的图标区域，我们将此视为进行清除的操作；
     * getWidth():得到控件的宽度
     * event.getX():抬起时的坐标(该坐标是相对于控件本身而言的)
     * getTotalPaddingRight():clean的图标左边缘至控件右边缘的距离
     * getPaddingRight():clean的图标右边缘至控件右边缘的距离
     * 于是：
     * getWidth()-getTotalPaddingRight()表示：控件作弊那到clean的图标左边缘的区域
     * getWidth()-getPaddingRight()表示：控件左边到clean的图标右边缘的区域
     * 所以这两者之间的区域刚好是clean的图标区域
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                boolean isClean=(event.getX()>(getWidth()-getTotalPaddingRight()))&&
                        (event.getX()<(getWidth()-getPaddingRight()));
                if(isClean){
                            setText("");
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);


    }

    /**
     * 文本变化监听
     */
    private class TextWatcherImp implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isVisible=getText().toString().length()>=1;
            setClearDrawableVisible(isVisible);
        }
    }

    /**
     * 焦点变化监听
     */
    private class FocusChangeListenerImpl implements OnFocusChangeListener{
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isHasFocus=hasFocus;
            if (isHasFocus){
                boolean isVisible=getText().toString().length()>=1;
                setClearDrawableVisible(isVisible);
            }else{
                setClearDrawableVisible(false);
            }
        }
    }

    /**
     * 隐藏或者显示右边clean的图标
     * @param isVisible
     */
    private void setClearDrawableVisible(boolean isVisible) {
        Drawable rightDrawable;
        if (isVisible){
            rightDrawable=mRightDrawable;
        }else{
            rightDrawable=null;
        }
        //使用代码设置改控件left，top，right，and bottom处的图标
        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],
                rightDrawable,getCompoundDrawables()[3]);
    }

}
