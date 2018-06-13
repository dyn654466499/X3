package com.terminus.facerecord.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.terminus.facerecord.R;
import com.terminus.facerecord.beans.OperationRecordBean;

import java.util.List;

public class OperationRecordAdapter extends BaseAdapter {

    List<OperationRecordBean> data;
    LayoutInflater inflater;
    private int selectItem=0;

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    public OperationRecordAdapter(Context context, List<OperationRecordBean> data) {
        this.data = data;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return data!=null ? data.size() : 0;
    }

    @Override
    public OperationRecordBean getItem(int position) {
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
            convertView = inflater.inflate(R.layout.adapter_item_operation_record, null);
            holder.tv_member_name = convertView.findViewById(R.id.tv_member_name);
            holder.tv_uptown_name = convertView.findViewById(R.id.tv_uptown_name);
            holder.tv_operation_type = convertView.findViewById(R.id.tv_operation_type);
            holder.tv_operation_time = convertView.findViewById(R.id.tv_operation_time);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        OperationRecordBean bean = data.get(position);
        holder.tv_member_name.setText(bean.memberName);
        holder.tv_uptown_name.setText(bean.uptownName);
        holder.tv_operation_type.setText(bean.operateType);
        holder.tv_operation_time.setText(bean.operateTime);
        return convertView;
    }

    public static class ViewHolder{
        TextView tv_member_name;
        TextView tv_uptown_name;
        TextView tv_operation_type;
        TextView tv_operation_time;
    }
}
