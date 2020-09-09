package com.cloudwalk.livenesscameraproject.adapter;

import android.content.Context;

import com.cloudwalk.livenesscameraproject.R;
import com.cloudwalk.livenesscameraproject.activity.bean.StrategyBean;
import com.cloudwalk.livenesscameraproject.recycleview_adapter.CommonAdapter;
import com.cloudwalk.livenesscameraproject.recycleview_adapter.base.ViewHolder;

import java.util.List;

public class LivenessStrategyAdapter extends CommonAdapter<Object> {

    public LivenessStrategyAdapter(Context context, int layoutId, List<Object> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Object o, int position) {
        StrategyBean bean = (StrategyBean) o;
        holder.setText(R.id.cb_root, bean.strategyName);
        if (bean.isCheck) {
            holder.setChecked(R.id.cb_root, true);
        } else {
            holder.setChecked(R.id.cb_root, false);
        }
        holder.setOnItemChildCheckChangeListener(R.id.cb_root);
    }
}
