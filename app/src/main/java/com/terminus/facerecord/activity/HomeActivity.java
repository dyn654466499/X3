package com.terminus.facerecord.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.terminus.facerecord.R;
import com.terminus.facerecord.adapters.FloorAdapter;
import com.terminus.facerecord.adapters.MembersAdapter;
import com.terminus.facerecord.adapters.RoomAdapter;
import com.terminus.facerecord.adapters.UnitAdapter;
import com.terminus.facerecord.beans.BuildingBean;
import com.terminus.facerecord.beans.RoomBean;
import com.terminus.facerecord.events.LoginEvent;
import com.terminus.facerecord.managers.LoginManager;
import com.terminus.facerecord.utils.CommonUtils;
import com.terminus.facerecord.utils.DialogUtils;
import com.terminus.facerecord.utils.SPUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener{
    private ListView lv_floor,lv_room,lv_house_members;
    private int uptownPosition = -1,
            buildingPosition,
            unitPosition,
            floorPosition,
            roomPosition;
    private RelativeLayout rLayout_select_building;
    private LinearLayout lLayout_info, lLayout_info_title, lLayout_uptown;
    private TextView tv_building_unit, tv_home_select_uptown;
    private Drawer homeDrawer;
    private ImageView iv_home_drawer;
    private Context mContext;
    private FloorAdapter buildingAdapter;
    private UnitAdapter unitAdapter, uptownAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;
        lv_floor = findViewById(R.id.lv_floor);
        lv_room = findViewById(R.id.lv_room);
        lv_house_members = findViewById(R.id.lv_house_members);
        lLayout_info = findViewById(R.id.lLayout_info);
        lLayout_info_title = findViewById(R.id.lLayout_info_title);
        lLayout_uptown = findViewById(R.id.lLayout_uptown);
        lLayout_uptown.setOnClickListener(this);
        tv_home_select_uptown = findViewById(R.id.tv_home_select_uptown);
        iv_home_drawer = findViewById(R.id.iv_home_drawer);
        iv_home_drawer.setOnClickListener(this);

        tv_building_unit = findViewById(R.id.tv_building_unit);
        rLayout_select_building = findViewById(R.id.rLayout_select_building);
        rLayout_select_building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String jsonData = CommonUtils.getJsonFromAssets(HomeActivity.this, "building.json");
//                PickerUtils.showBuildingPicker(HomeActivity.this, jsonData, new PickerUtils.OnPickListener() {
//                    @Override
//                    public void onPick(String result) {
//                        tv_building_unit.setText(result);
//                    }
//                });
                showBuildingPicker();
            }
        });
//        toggleBuildingVisible(false);
//        toggleHouseInfoVisible(false);


        BadgeStyle style = new BadgeStyle();
        style.withTextColor(getResources().getColor(R.color.md_red_500));
        homeDrawer = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withHeader(R.layout.layout_drawer_header)
                .withFooter(R.layout.layout_drawer_footer)
                .withFooterDivider(false)
                .withSelectedItemByPosition(-1)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.operation_record).withIcon(R.drawable.hp_icon_key).withSelectable(false).withSetSelected(false),
                        new PrimaryDrawerItem().withName(R.string.app_version).withBadge(CommonUtils.getVersionName(this)).withIcon(R.drawable.hp_icon_key).withSelectable(false).withSetSelected(false).withBadgeStyle(style)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position){
                            case 1:
                                startActivity(new Intent(HomeActivity.this, OperationRecordActivity.class));
                                break;
                            case 2:

                                break;
                        }
                        return true;
                    }
                })
                .build();
        homeDrawer.getFooter().findViewById(R.id.btn_logout).setOnClickListener(this);

        View header = homeDrawer.getHeader();
        String mobile = SPUtils.getString(this,"curMobile");
        ((TextView)header.findViewById(R.id.tv_drawer_mobile)).setText(CommonUtils.formatPhoneNumWithStar(mobile));

        createAllData();
        initHouseInfo();
    }

    private void createAllData(){
        createUptownData();
        createBuildingData();
    }

    private void createUptownData(){
        final List<String> option1 = mockUptown();
        uptownAdapter = new UnitAdapter(this, option1);
        String uptown = SPUtils.getString(this, "uptown");
        uptownPosition = uptownAdapter.getPosition(uptown);
        if(uptownPosition != -1){
            tv_home_select_uptown.setText(uptown);
            toggleBuildingVisible(true);
        }else{
            toggleBuildingVisible(false);
            toggleHouseInfoVisible(false);
        }
    }

    private AdapterView.OnItemClickListener buildingListener;
    private void createBuildingData(){
        String jsonData = CommonUtils.getJsonFromAssets(this,"building.json");
        final List<String> option1 = new ArrayList<>();
        final List<List<String>> option2 = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                BuildingBean entity = gson.fromJson(data.optJSONObject(i).toString(), BuildingBean.class);
                option1.add(entity.getName());
                List<BuildingBean.Building> building = entity.getBuilding();
                List<String> buildings = new ArrayList<>();
                for (int j=0;j < building.size();j++){
                    buildings.add(building.get(j).getName());
                }
                option2.add(buildings);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        buildingAdapter = new FloorAdapter(this, option1);
        String building = SPUtils.getString(this, "building");
        buildingPosition = buildingAdapter.getPosition(building);

        unitAdapter = new UnitAdapter(this, option2.get(buildingPosition == -1 ? 0 : buildingPosition));
        String unit = SPUtils.getString(this, "unit");
        unitPosition = unitAdapter.getPosition(unit);

        if(buildingPosition != -1 && unitPosition != -1){
            tv_building_unit.setText(building + unit);
            toggleHouseInfoVisible(true);
        }else{
            toggleHouseInfoVisible(false);
        }
        buildingListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                buildingAdapter.setSelectItem(position);
                buildingPosition = position;
                unitAdapter.setData(option2.get(position));
            }
        };
    }

    /**
     * 弹出选择小区的popupwindow
     */
    private void showUptownPicker(){
        View layout = getLayoutInflater().inflate(R.layout.layout_popup_uptown, null);
        final PopupWindow pop = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        pop.setAnimationStyle(R.style.popwin_anim_style);

        ListView lv_uptowns = layout.findViewById(R.id.lv_uptowns);
        ImageView lv_close = layout.findViewById(R.id.iv_close);
        lv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        lv_uptowns.setAdapter(uptownAdapter);
        uptownAdapter.setSelectItem(uptownPosition);
        lv_uptowns.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uptownAdapter.setSelectItem(position);
                uptownPosition = position;
                pop.dismiss();
                tv_home_select_uptown.setText(uptownAdapter.getItem(position));
                toggleBuildingVisible(true);
                SPUtils.putString(mContext, "uptown", uptownAdapter.getItem(position));
            }
        });

        pop.showAtLocation(rLayout_select_building, Gravity.NO_GRAVITY, 0, 0);
    }

    /**
     * 弹出选择楼栋的popupwindow
     */
    private void showBuildingPicker(){
        View layout = getLayoutInflater().inflate(R.layout.layout_popup_building, null);
        final PopupWindow pop = new PopupWindow(layout,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
//        pop.setBackgroundDrawable(new ColorDrawable(0xffffff));//支持点击Back虚拟键退出
        pop.setAnimationStyle(R.style.popwin_anim_style);
        ListView lv_buildings = layout.findViewById(R.id.lv_buildings);
        ListView lv_units = layout.findViewById(R.id.lv_units);
        ImageView lv_close = layout.findViewById(R.id.iv_close);
        lv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        lv_buildings.setAdapter(buildingAdapter);
        if(buildingPosition == -1){
            buildingPosition = 0;
        }
        buildingAdapter.setSelectItem(buildingPosition);
        lv_buildings.setOnItemClickListener(buildingListener);

        lv_units.setAdapter(unitAdapter);
        if(unitPosition == -1){
            unitPosition = 0;
        }
        unitAdapter.setSelectItem(unitPosition);
        lv_units.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                unitAdapter.setSelectItem(position);
                unitPosition = position;
                pop.dismiss();
                tv_building_unit.setText(buildingAdapter.getItem(buildingPosition) + unitAdapter.getItem(position));
                SPUtils.putString(mContext, "building", buildingAdapter.getItem(buildingPosition));
                SPUtils.putString(mContext, "unit", unitAdapter.getItem(position));
                toggleHouseInfoVisible(true);
            }
        });

        pop.showAtLocation(rLayout_select_building, Gravity.NO_GRAVITY, 0, 0);
    }

    private void initHouseInfo(){
        String jsonData = CommonUtils.getJsonFromAssets(this,"room.json");
        final List<String> option1 = new ArrayList<>();
        final List<List<String>> option2 = new ArrayList<>();
        final List<List<List<RoomBean.MembersBean>>> option3 = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(jsonData);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                RoomBean entity = gson.fromJson(data.optJSONObject(i).toString(), RoomBean.class);
                option1.add(entity.getName());
                List<RoomBean.Room> room = entity.getRoom();
                List<String> rooms = new ArrayList<>();
                List<List<RoomBean.MembersBean>> membersBeans = new ArrayList<>();
                for (int j=0;j < room.size();j++){
                    rooms.add(room.get(j).getName());
                    membersBeans.add(room.get(j).getMembers());
                }
                option2.add(rooms);
                option3.add(membersBeans);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final FloorAdapter floorAdapter = new FloorAdapter(this, option1);
        lv_floor.setAdapter(floorAdapter);
        floorPosition = floorAdapter.getPosition(SPUtils.getString(mContext,"floor"));
        if(floorPosition == -1){
            toggleHouseInfoVisible(false);
            floorPosition = 0;
        }
        floorAdapter.setSelectItem(floorPosition);


        final RoomAdapter roomAdapter = new RoomAdapter(this, option2.get(floorPosition));
        lv_room.setAdapter(roomAdapter);
        roomPosition = roomAdapter.getPosition(SPUtils.getString(mContext,"room"));
        if(roomPosition == -1){
            toggleHouseInfoVisible(false);
            roomPosition = 0;
        }
        roomAdapter.setSelectItem(roomPosition);

        final MembersAdapter membersAdapter = new MembersAdapter(this, option3.get(floorPosition).get(roomPosition));
        lv_house_members.setAdapter(membersAdapter);

        lv_floor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                floorAdapter.setSelectItem(position);
                floorPosition = position;
                roomAdapter.setData(option2.get(position));
                membersAdapter.setData(option3.get(floorPosition).get(roomPosition));
            }
        });

        lv_room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomAdapter.setSelectItem(position);
                roomPosition = position;
                membersAdapter.setData(option3.get(floorPosition).get(roomPosition));
                SPUtils.putString(mContext, "floor", floorAdapter.getItem(floorPosition));
                SPUtils.putString(mContext, "room", roomAdapter.getItem(roomPosition));
            }
        });

        lv_house_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void toggleBuildingVisible(boolean visible){
        rLayout_select_building.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void toggleHouseInfoVisible(boolean visible){
        lLayout_info_title.setVisibility(visible ? View.VISIBLE : View.GONE);
        lLayout_info.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                DialogUtils.showDialog(this, "", "确定退出当前账号吗", new DialogUtils.DialogCommand() {
                    @Override
                    public void onSure() {
                        LoginManager.getInstance().logout();
                        finish();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                break;

            case R.id.iv_home_drawer:
                if(homeDrawer.isDrawerOpen()){
                    homeDrawer.closeDrawer();
                }else{
                    homeDrawer.openDrawer();
                }
                break;

            case R.id.lLayout_uptown:
                showUptownPicker();
                break;
        }
    }

    private boolean isLogin;
    public void onEventMainThread(Message msg){
        switch (msg.what){
            case LoginEvent.DO_LOGIN:
                isLogin = true;
                break;

            case LoginEvent.DO_LOGOUT:
                isLogin = false;
                break;
        }
    }

    private List<String> mockUptown(){
        List<String> uptowns = new ArrayList<>();
        uptowns.add("保利小区");
        uptowns.add("动感小区");
        uptowns.add("希望小区");
        uptowns.add("社会小区");
        uptowns.add("长运小区");
        uptowns.add("福泽小区");
        uptowns.add("万科小区");
        uptowns.add("恒大小区");
        return uptowns;
    }
}
