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

public class BuildingAdapter extends BaseAdapter {

    List<String> data;
    LayoutInflater inflater;
    private int selectItem=0;

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    public BuildingAdapter(Context context, List<String> data) {
        this.data = data;
        inflater=LayoutInflater.from(context);
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

    public int getPosition(String text){
        return data.indexOf(text);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_building,  parent, false);
            holder.tv_building = convertView.findViewById(R.id.tv_building);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.tv_building.setText(data.get(position));

        if (selectItem == position) {
            holder.tv_building.setTextColor(Color.WHITE);
            holder.tv_building.setBackground(inflater.getContext().getResources().getDrawable(R.drawable.theme_btn_circle));
        }else {
            holder.tv_building.setTextColor(Color.parseColor("#666666"));
            holder.tv_building.setBackgroundColor(Color.WHITE);
        }

        return convertView;
    }

    public static class ViewHolder{
        TextView tv_building;
    }
}
