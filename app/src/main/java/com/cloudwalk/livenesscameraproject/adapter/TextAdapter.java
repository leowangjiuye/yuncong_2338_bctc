package com.cloudwalk.livenesscameraproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudwalk.livenesscameraproject.R;

public class TextAdapter extends BaseAdapter {

    private final String[] mTypes;
    private Context mContext;

    public TextAdapter(String[] types, Context context) {
        this.mContext = context;
        this.mTypes = types;
    }

    @Override
    public int getCount() {
        return mTypes.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_spinner_item, null);
            viewHolder.tvItem = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvItem.setText(mTypes[position]);
        return convertView;
    }

    class ViewHolder {
        TextView tvItem;
    }
}