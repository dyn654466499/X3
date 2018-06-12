package com.terminus.facerecord.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.terminus.facerecord.beans.BuildingBean;
import com.terminus.facerecord.beans.ProvinceBean;
import com.terminus.facerecord.beans.RoomBean;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PickerUtils {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public static void showProvincePicker(Context context, String jsonData, final OnPickListener listener){
        ArrayList<ProvinceBean> provinceBean = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                ProvinceBean entity = gson.fromJson(data.optJSONObject(i).toString(), ProvinceBean.class);
                provinceBean.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ArrayList<ProvinceBean> options1Items;
        final ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
        final ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = provinceBean;
        for (int i = 0; i < provinceBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < provinceBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = provinceBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (provinceBean.get(i).getCityList().get(c).getArea() == null
                        || provinceBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(provinceBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                if(listener != null){
                    listener.onPick(tx);
                }
            }
        })
        .setTitleText("城市选择")
        .setDividerColor(Color.BLACK)
        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
        .setContentTextSize(20)
        .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    public static void showBuildingPicker(Context context, String jsonData, final OnPickListener listener){
        ArrayList<BuildingBean> buildingBeans = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                BuildingBean entity = gson.fromJson(data.optJSONObject(i).toString(), BuildingBean.class);
                buildingBeans.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ArrayList<BuildingBean> options1Items;
        final List<List<String>> options2Items = new ArrayList<>();

        options1Items = buildingBeans;
        for (int i = 0; i < buildingBeans.size(); i++) {//遍历省份
            ArrayList<String> roomList = new ArrayList<>();//该省的城市列表（第二级）
            BuildingBean buildingBean = buildingBeans.get(i);
            for(int j = 0; j < buildingBeans.size(); j++){
                roomList.add(buildingBean.getBuilding().get(j).getName());
            }
            /**
             * 添加城市数据
             */
            options2Items.add(roomList);
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2);
                if(listener != null){
                    listener.onPick(tx);
                }
            }
        })
//        .setTitleText("楼栋选择")
        .setDividerColor(Color.BLACK)
        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
        .setContentTextSize(20)
        .build();

        pvOptions.setPicker(options1Items, options2Items);//三级选择器
        pvOptions.show();
    }


    public static void showRoomPicker(Context context, String jsonData, final OnPickListener listener){
        ArrayList<RoomBean> roomBeans = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                RoomBean entity = gson.fromJson(data.optJSONObject(i).toString(), RoomBean.class);
                roomBeans.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ArrayList<RoomBean> options1Items;
        final List<List<String>> options2Items = new ArrayList<>();

        options1Items = roomBeans;
        for (int i = 0; i < roomBeans.size(); i++) {//遍历省份
            ArrayList<String> roomList = new ArrayList<>();//该省的城市列表（第二级）
            RoomBean roomBean = roomBeans.get(i);
            for(int j = 0; j < roomBeans.size(); j++){
                roomList.add(roomBean.getRoom().get(j).getName());
            }
            /**
             * 添加城市数据
             */
            options2Items.add(roomList);
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2);
                if(listener != null){
                    listener.onPick(tx);
                }
            }
        })
        .setTitleText("房间号选择")
        .setDividerColor(Color.BLACK)
        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
        .setContentTextSize(20)
        .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//三级选择器
        pvOptions.show();
    }

    public static void showUptownPicker(Context context, String jsonData, final OnPickListener listener){
        ArrayList<RoomBean> roomBeans = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                RoomBean entity = gson.fromJson(data.optJSONObject(i).toString(), RoomBean.class);
                roomBeans.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ArrayList<RoomBean> options1Items;
        final List<List<String>> options2Items = new ArrayList<>();

        options1Items = roomBeans;
        for (int i = 0; i < roomBeans.size(); i++) {//遍历省份
            ArrayList<String> roomList = new ArrayList<>();//该省的城市列表（第二级）
            RoomBean roomBean = roomBeans.get(i);
            for(int j = 0; j < roomBeans.size(); j++){
                roomList.add(roomBean.getRoom().get(j).getName());
            }
            /**
             * 添加城市数据
             */
            options2Items.add(roomList);
        }

        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2);
                if(listener != null){
                    listener.onPick(tx);
                }
            }
        })
                .setTitleText("房间号选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//三级选择器
        pvOptions.show();
    }

    public interface OnPickListener{
        void onPick(String result);
    }
}
