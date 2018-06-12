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

public class RoomAdapter extends BaseAdapter {

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

    public RoomAdapter(Context context, List<String> data) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_floor, null);
            holder.tv_floor = convertView.findViewById(R.id.tv_floor);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.tv_floor.setText(data.get(position));

        if (selectItem == position) {
            holder.tv_floor.setTextColor(Color.BLUE);
        }else {
            holder.tv_floor.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    public static class ViewHolder{
        TextView tv_floor;
    }
}
