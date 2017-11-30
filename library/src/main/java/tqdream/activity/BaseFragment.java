package tqdream.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidcommonutil.tqdreamlibrary.R;

/**
 * Created by Rolf on 2016/3/11.
 */
public abstract class BaseFragment extends Fragment {
    private FrameLayout mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mRootView) {
            View v = View.inflate(getActivity(), R.layout.fragment_base_layout, null);
            mRootView = (FrameLayout) v.findViewById(R.id.mRootView);
            mRootView.addView(initView(), new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        } else {
            removeSelfFromParent(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        processLogic();
    }

    protected abstract View initView();

    protected abstract void processLogic();

    /**
     * 把自身从父View中移除
     */
    public void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }
}
