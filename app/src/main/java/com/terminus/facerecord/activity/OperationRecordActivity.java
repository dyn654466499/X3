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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_record);
        findViewById(R.id.lLayout_title_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_common_title)).setText("操作记录");

        ListView lv_operation_record = findViewById(R.id.lv_operation_record);
        List<OperationRecordBean> data = new ArrayList<>();
        for(int i = 0; i < 10;i++){
            OperationRecordBean bean = new OperationRecordBean();
            bean.uptownName = "动感小区-1栋1单元-2楼204号";
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lLayout_title_back:
                finish();
                break;
        }
    }
}
