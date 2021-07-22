package com.autohome.main.car.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * @author kane
 * @date 2021/6/10 10:27
 */
public class DynamicLayoutHelper {
    private final static DynamicLayoutHelper helper = new DynamicLayoutHelper();

    private DynamicLayoutHelper() {

    }

    public static DynamicLayoutHelper getInstance() {
        return helper;
    }

    public <T> void notifyDataChangedLayout(LinearLayout layout, List<T> dataList, LayoutAdapter<T> adapter) {
        if (adapter == null) {
            return;
        }
        if (dataList == null) {
            layout.removeAllViews();
            return;
        }
        int childCount = layout.getChildCount();
        int dataSize = dataList.size();
        int offset = childCount - dataSize;
        if (offset > 0) {
            layout.removeViews(dataSize, childCount);
        } else if (offset < 0) {
            for (int i = childCount; i < dataList.size(); i++) {
                layout.addView(adapter.getView(layout.getContext(), layout,dataList.get(i)));
            }
        }
        childCount = layout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            adapter.bindData(layout.getChildAt(i), dataList.get(i), i);
        }
    }

    public interface LayoutAdapter<T> {
        View getView(Context context, ViewGroup parent,T item);

        void bindData(View view, T item, int i);
    }
}
