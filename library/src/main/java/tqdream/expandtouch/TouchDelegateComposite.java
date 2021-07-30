package tqdream.expandtouch;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:         taoqiang
 * CreateDate:     2021/7/9 18:30
 * Description:    TouchDelegate组合，支持一个viewgroup内设置多个TouchDelegate
 */
public class TouchDelegateComposite extends FixedTouchDelegate {
    private final View mParent;
    private static final Rect USELESS_RECT = new Rect();
    private final List<FixedTouchDelegate> mDelegates = new ArrayList<FixedTouchDelegate>(8);


    public TouchDelegateComposite(@NonNull View parent) {
        super(USELESS_RECT, parent);
        mParent = parent;
    }

    public void addDelegate(@NonNull FixedTouchDelegate delegate) {
        mDelegates.add(delegate);
    }

    public void build(){
        mParent.setTouchDelegate(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean res = false;
        float x = event.getX();
        float y = event.getY();
        for (FixedTouchDelegate delegate : mDelegates) {
            event.setLocation(x, y);
            res = delegate.onTouchEvent(event) || res;
        }
        return res;
    }
}
