package com.terminus.facerecord.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.terminus.facerecord.R;
import com.terminus.facerecord.adapters.FloorAdapter;
import com.terminus.facerecord.adapters.MembersAdapter;
import com.terminus.facerecord.adapters.RoomAdapter;
import com.terminus.facerecord.beans.RoomBean;
import com.terminus.facerecord.utils.CommonUtils;
import com.terminus.facerecord.utils.PickerUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity{
    private ListView lv_floor,lv_room,lv_house_members;
    private int floorPosition,roomPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lv_floor = findViewById(R.id.lv_floor);
        lv_room = findViewById(R.id.lv_room);
        lv_house_members = findViewById(R.id.lv_house_members);
        final TextView tv_building_unit = findViewById(R.id.tv_building_unit);
        findViewById(R.id.rLayout_select_building).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonData = CommonUtils.getJsonFromAssets(MainActivity.this, "building.json");
                PickerUtils.showBuildingPicker(MainActivity.this, jsonData, new PickerUtils.OnPickListener() {
                    @Override
                    public void onPick(String result) {
                        tv_building_unit.setText(result);
                    }
                });
            }
        });

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withHeader(R.layout.layout_drawer_header)
                .withFooter(R.layout.layout_drawer_footer)
                .withFooterDivider(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.operation_record).withIcon(R.drawable.hp_icon_key).withSelectable(false).withSetSelected(false),
                        new PrimaryDrawerItem().withName(R.string.my_permission).withIcon(R.drawable.hp_icon_key).withSelectable(false).withSetSelected(false),
                        new PrimaryDrawerItem().withName(R.string.app_version).withBadge(CommonUtils.getVersionName(this)).withIcon(R.drawable.hp_icon_key).withSelectable(false).withSetSelected(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        switch (position){
                            case 0:

                                break;
                            case 1:

                                break;
                        }
                        return true;
                    }
                })
                .build();
        initHouseInfo();
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
        final RoomAdapter roomAdapter = new RoomAdapter(this, option2.get(0));
        final MembersAdapter membersAdapter = new MembersAdapter(this, option3.get(0).get(0));

        lv_floor.setAdapter(floorAdapter);
        lv_floor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                floorAdapter.setSelectItem(position);
                floorPosition = position;
                roomAdapter.setData(option2.get(position));
                membersAdapter.setData(option3.get(floorPosition).get(roomPosition));
            }
        });

        lv_room.setAdapter(roomAdapter);
        lv_room.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                roomAdapter.setSelectItem(position);
                roomPosition = position;
                membersAdapter.setData(option3.get(floorPosition).get(roomPosition));
            }
        });

        lv_house_members.setAdapter(membersAdapter);
        lv_house_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
