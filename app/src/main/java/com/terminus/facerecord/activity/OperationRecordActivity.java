package com.terminus.facerecord.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.terminus.facerecord.R;
import com.terminus.facerecord.adapters.OperationRecordAdapter;
import com.terminus.facerecord.beans.OperationRecordBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperationRecordActivity extends BaseActivity implements View.OnClickListener{
    private ListView lv_operation_record;
    private TextView tv_record_no_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_record);
        findViewById(R.id.lLayout_title_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_common_title)).setText("操作记录");
        tv_record_no_data = findViewById(R.id.tv_record_no_data);
        lv_operation_record = findViewById(R.id.lv_operation_record);

        List<OperationRecordBean> data = new ArrayList<>();
        for(int i = 0; i < 10;i++){
            OperationRecordBean bean = new OperationRecordBean();
            bean.uptownName = "动感小区-113栋123单元-22楼2204号";
            bean.memberName = "邓耀宁";
            bean.operateType = "更新";
            SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.util.Date dt = new Date(System.currentTimeMillis());
            String sDateTime = sdf.format(dt);
            bean.operateTime = sDateTime;
            data.add(bean);
        }
        OperationRecordAdapter adapter = new OperationRecordAdapter(this, data);
        lv_operation_record.setAdapter(adapter);
    }

    private void hasNoData(){
        tv_record_no_data.setVisibility(View.VISIBLE);
        lv_operation_record.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lLayout_title_back:
                finish();
                break;
        }
    }
}
