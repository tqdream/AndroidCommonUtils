package tqdream.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 优化事件拦截的framelayout（loading显示时拦截触摸事件）
 * Created by WuQiubin on 2015/12/8.
 */
public class CustomFramelayout extends FrameLayout {

    private boolean intercept = false;

    public CustomFramelayout(Context context) {
        super(context);
    }

    public CustomFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return intercept;
    }

    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }
}
