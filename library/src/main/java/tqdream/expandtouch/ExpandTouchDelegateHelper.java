package com.autohome.main.car.util.expandtouch;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:         taoqiang
 * CreateDate:     2021/7/9 18:31
 * Description:    TouchDelegate 使用的工具类
 */
public class ExpandTouchDelegateHelper {

    private ViewGroup mParentView;
    private View mTargetView;
    private int mLeftExpand;
    private int mRightExpand;
    private int mTopExpand;
    private int mBottomExpand;

    public ExpandTouchDelegateHelper(ViewGroup parentView, int leftExpand, int rightExpand, int topExpand, int bottomExpand) {
        mParentView = parentView;
        mLeftExpand = leftExpand;
        mRightExpand = rightExpand;
        mTopExpand = topExpand;
        mBottomExpand = bottomExpand;
    }

    /**
     * 扩展一个View的响应区域
     * @param targetView
     */
    public void expandTouchDelegate(View targetView) {
        Rect bounds = new Rect();
        ViewGroupUtils.getDescendantRect(mParentView, targetView, bounds);
        bounds.left -= mLeftExpand;
        bounds.top -= mTopExpand;
        bounds.right += mRightExpand;
        bounds.bottom += mBottomExpand;
        FixedTouchDelegate touchDelegate = new FixedTouchDelegate(bounds, targetView);
        mParentView.setTouchDelegate(touchDelegate);
    }

    /**
     * 扩展多个view的响应区域
     * @param views
     */
    public void expandTouchDelegate(View... views) {
        TouchDelegateComposite touchDelegateComposite = new TouchDelegateComposite(mParentView);
        for (View view : views) {
            Rect bounds = new Rect();
            ViewGroupUtils.getDescendantRect(mParentView, view, bounds);
            bounds.left -= mLeftExpand;
            bounds.top -= mTopExpand;
            bounds.right += mRightExpand;
            bounds.bottom += mBottomExpand;
            touchDelegateComposite.addDelegate(new FixedTouchDelegate(bounds, view));
        }
        touchDelegateComposite.build();
    }

    /**
     * 扩展一个ViewGroup的响应区域,子View也会增加相同的响应区域
     *
     * @param targetView
     */
    public static void expandTouchDelegate(ViewGroup parentView, View targetView, int leftExpand, int rightExpand, int topExpand, int bottomExpand) {
        Rect bounds = new Rect();
        ViewGroupUtils.getDescendantRect(parentView, targetView, bounds);
        bounds.left -= leftExpand;
        bounds.top -= topExpand;
        bounds.right += rightExpand;
        bounds.bottom += bottomExpand;
        MultiTouchDelegate touchDelegate = new MultiTouchDelegate(bounds, targetView, parentView, leftExpand, rightExpand, topExpand, bottomExpand);
        parentView.setTouchDelegate(touchDelegate);
    }
}
