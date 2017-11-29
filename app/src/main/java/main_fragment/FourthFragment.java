package main_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidcommonutil.main.R;
import main.BaseFragment;


/**
 * @author taoqiang @create on 2016/10/18 16:34
 * @email tqdream@163.com
 */
public class FourthFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_fourth, container, false);
    }
}
