package tqdream.expandtouch;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:         taoqiang
 * CreateDate:     2021/7/9 18:31
 * Description:    TouchDelegate 使用的工具类
 *                 1.修复了谷歌在android 9 以下存在的bug，详见FixedTouchDelegate；
 *                 2.增加了对一个ViewGroup中设置多个TouchDelegate的支持
 */
public class ExpandTouchDelegateHelper {
    private ViewGroup mParentView;
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
    public void expandTouchDelegate(final View targetView) {
        targetView.post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                ViewGroupUtils.getDescendantRect(mParentView, targetView, bounds);
                bounds.left -= mLeftExpand;
                bounds.top -= mTopExpand;
                bounds.right += mRightExpand;
                bounds.bottom += mBottomExpand;
                FixedTouchDelegate touchDelegate = new FixedTouchDelegate(bounds, targetView);
                mParentView.setTouchDelegate(touchDelegate);
            }
        });
    }

    /**
     * 扩展多个view的响应区域
     * @param views
     */
    public void expandTouchDelegate(final View... views) {
        mParentView.post(new Runnable() {
            @Override
            public void run() {
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
        });
    }
}
