package com.cloudwalk.livenesscameraproject.recycleview_adapter.base;

/**
 * item接口
 *
 * @param <T>
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
