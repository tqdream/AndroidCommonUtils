package tqdream.ok.adapter;

/**
 * Created by guowei on 2016/6/7.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 这是一个通用的Adapter
 * Created by zsl on 15/5/19.
 */
public abstract class UniversalAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mLists;
    protected LayoutInflater mInflater;
    int layoutId;


    /**
     * 通用的Adapter
     *
     * @param mContext 上下文
     * @param mlists   数据集
     * @param layoutId item  布局视图
     */
    public UniversalAdapter(Context mContext, List<T> mlists, int layoutId) {
        this.mContext = mContext;
        this.mLists = mlists;
        this.layoutId = layoutId;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mLists == null ? 0 : mLists.size();
    }

    @Override
    public T getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, layoutId);
        convert(holder, getItem(position), position);
        return holder.getmConvertView();
    }

    public abstract void convert(ViewHolder holder, T t, int position);


}
