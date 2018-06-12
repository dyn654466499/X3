package com.terminus.facerecord.adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.terminus.facerecord.R;
import com.terminus.facerecord.beans.RoomBean;

import java.util.List;

public class MembersAdapter extends BaseAdapter {

    List<RoomBean.MembersBean> data;
    LayoutInflater inflater;
    Context mContext;
    private int selectItem=0;

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    public void setData(List<RoomBean.MembersBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public MembersAdapter(Context context, List<RoomBean.MembersBean> data) {
        this.data = data;
        mContext = context;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return data!=null ? data.size() : 0;
    }

    @Override
    public RoomBean.MembersBean getItem(int position) {
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
            convertView = inflater.inflate(R.layout.adapter_item_member, null);
            holder.tv_member_name = convertView.findViewById(R.id.tv_member_name);
            holder.iv_member_head = convertView.findViewById(R.id.iv_member_head);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.tv_member_name.setText(data.get(position).getName());

        //data.get(position).getHead()
        Glide.with(mContext).load(R.drawable.hp_icon_key).into(holder.iv_member_head);

        return convertView;
    }

    public static class ViewHolder{
        TextView tv_member_name;
        ImageView iv_member_head;
    }
}
