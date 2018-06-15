package com.terminus.facerecord.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.terminus.facerecord.R;

import java.util.List;

public class ResidentHeadAdapter extends BaseAdapter {
    LayoutInflater inflater;

    public ResidentHeadAdapter(Context context) {
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_resident_head, null);
            holder.iv_member_head = convertView.findViewById(R.id.iv_member_head);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        Glide.with(inflater.getContext()).load(R.drawable.hp_icon_head).into(holder.iv_member_head);
        return convertView;
    }

    public static class ViewHolder{
        ImageView iv_member_head;
    }
}
