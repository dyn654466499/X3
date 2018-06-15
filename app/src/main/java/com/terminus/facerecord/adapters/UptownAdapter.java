package com.terminus.facerecord.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.terminus.facerecord.R;

import java.util.List;

public class UptownAdapter extends BaseAdapter {

    List<String> data;
    LayoutInflater inflater;
    private int selectItem=0;

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public UptownAdapter(Context context, List<String> data) {
        this.data = data;
        inflater=LayoutInflater.from(context);
    }

    public int getPosition(String text){
        return data.indexOf(text);
    }

    @Override
    public int getCount() {
        return data!=null ? data.size() : 0;
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_uptown, parent, false);
            holder.tv_uptown = convertView.findViewById(R.id.tv_uptown);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.tv_uptown.setText(data.get(position));
        if (selectItem == position) {
            holder.tv_uptown.setTextColor(Color.parseColor("#17AE86"));
            convertView.setBackgroundColor(Color.parseColor("#F8F8F8"));
        }else {
            holder.tv_uptown.setTextColor(Color.parseColor("#666666"));
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return convertView;
    }

    public static class ViewHolder{
        TextView tv_uptown;
    }
}
