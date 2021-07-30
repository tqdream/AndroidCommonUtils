package tqdream.expandtouch;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Author:         taoqiang
 * CreateDate:     2021/7/9 15:57
 * Description:
 * 重写TouchDelegate，修复谷歌官方TouchDelegate 在android 9 以下的bug
 *
 * TouchDelegate本身有一个很致命的问题: 给一个View设置TouchDelegate会导致该View在一种特殊情况下无法响应点击事件.
 *
 * 如图所示：
 * +------------------------------------------------------+
 * |                                                      |
 * |                                                      |
 * |          +-------------------------+                 |
 * |          |                         |                 |
 * |          |      +---------+        |                 |
 * |          |      |         |        |                 |
 * |          |      |  按钮A   |  区域B |    父控件C      |
 * |          |      |         |        |                 |
 * |          |      +---------+        |                 |
 * |          |                         |                 |
 * |          |                         |                 |
 * |          +-------------------------+                 |
 * |                                                      |
 * |                                                      |
 * +------------------------------------------------------+
 * 假如现在使用TouchDelegate将A的触摸区域扩大到B, 且A, C控件都可以响应点击事件.
 *
 * 如果用户点击A区域, 再点击C区域, 两个点击事件都能触发.
 * 如果用户点击扩展区域B(不包括A), 此后当用户点击C控件, 将无法触发C的点击事件.
 *
 * 根本原因在于TouchDelegate中成员变量mDelegateTargeted没有在收到DOWN事件时重置为false.
 *
 * 源码：8.1.0_r33 TouchDelegate.java
 * public boolean onTouchEvent(MotionEvent event) {
 *     int x = (int)event.getX();
 *     int y = (int)event.getY();
 *     boolean sendToDelegate = false;
 *     boolean hit = true;
 *     boolean handled = false;
 *
 *     switch (event.getAction()) {
 *     case MotionEvent.ACTION_DOWN:
 *         Rect bounds = mBounds;
 *
 *         if (bounds.contains(x, y)) {
 *             mDelegateTargeted = true;
 *             sendToDelegate = true;
 *         }
 *         break;
 *     case MotionEvent.ACTION_UP:
 *     case MotionEvent.ACTION_MOVE:
 *         sendToDelegate = mDelegateTargeted;
 *         if (sendToDelegate) {
 *             Rect slopBounds = mSlopBounds;
 *             if (!slopBounds.contains(x, y)) {
 *                 hit = false;
 *             }
 *         }
 *         break;
 *     case MotionEvent.ACTION_CANCEL:
 *         sendToDelegate = mDelegateTargeted;
 *         mDelegateTargeted = false;
 *         break;
 *     }
 *     if (sendToDelegate) {
 *         final View delegateView = mDelegateView;
 *
 *         if (hit) {
 *             // Offset event coordinates to be inside the target view
 *             event.setLocation(delegateView.getWidth() / 2, delegateView.getHeight() / 2);
 *         } else {
 *             // Offset event coordinates to be outside the target view (in case it does
 *             // something like tracking pressed state)
 *             int slop = mSlop;
 *             event.setLocation(-(slop * 2), -(slop * 2));
 *         }
 *         handled = delegateView.dispatchTouchEvent(event);
 *     }
 *     return handled;
 * }
 *
 * mDelegateTargeted只有在ACTION_CANCEL的时候才置false, 而一般情况下我们不会遇到ACTION_CANCEL, 一旦mDelegateTargeted被置true, 基本就恒为true了. 那么当用户点击一个不在代理区内的坐标时, ACTION_DOWN对应分支的代码不会执行, 因为不在Rect里. 又由于mDelegateTargeted为true, ACTION_MOVE和ACTION_UP会导致sendToDelegate置true, 执行派发事件代码, 并返回结果, 由于可点击的View默认吃全部事件, 因此返回值一定为true.
 *
 * 回到View.java里:
 *
 * // View.java
 * public boolean onTouchEvent(MotionEvent event) {
 *     ......
 *     if (mTouchDelegate != null) {
 *         if (mTouchDelegate.onTouchEvent(event)) {
 *             return true;
 *         }
 *     }
 *     ......
 * }
 * 这就导致设置了TouchDelegate的View自身的onTouchEvent除了ACTION_DOWN能自己处理, 其他事件全被TouchDelegate吃掉了.
 * 也就是说, 如果用户通过扩展区域B触发了A的点击事件, 将会导致C永远无法触发点击事件, 原因就是mDelegateTargeted被置true后基本没有机会重置.
 *
 */
public class FixedTouchDelegate extends TouchDelegate {
    private boolean mDelegateTargeted;
    private final Rect mBounds;
    private final Rect mSlopBounds;
    private final View mDelegateView;
    private final int mSlop;

    public FixedTouchDelegate(Rect bounds, View delegateView) {
        super(bounds, delegateView);
        mBounds = bounds;

        mSlop = ViewConfiguration.get(delegateView.getContext()).getScaledTouchSlop();
        mSlopBounds = new Rect(bounds);
        mSlopBounds.inset(-mSlop, -mSlop);
        mDelegateView = delegateView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        boolean sendToDelegate = false;
        boolean hit = true;
        boolean handled = false;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //主要是修复了这里
                mDelegateTargeted = mBounds.contains(x, y);
                sendToDelegate = mDelegateTargeted;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
                sendToDelegate = mDelegateTargeted;
                if (sendToDelegate) {
                    Rect slopBounds = mSlopBounds;
                    if (!slopBounds.contains(x, y)) {
                        hit = false;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                sendToDelegate = mDelegateTargeted;
                mDelegateTargeted = false;
                break;
        }
        if (sendToDelegate) {
            final View delegateView = mDelegateView;
            if (hit) {
                event.setLocation(delegateView.getWidth() / 2, delegateView.getHeight() / 2);
            } else {
                int slop = mSlop;
                event.setLocation(-(slop * 2), -(slop * 2));
            }
            handled = delegateView.dispatchTouchEvent(event);
        }
        return handled;
    }
}
