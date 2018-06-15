package com.terminus.facerecord.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import com.terminus.facerecord.R;
import com.terminus.facerecord.adapters.ResidentHeadAdapter;
import com.terminus.facerecord.utils.CommonUtils;

public class ResidentInfoActivity extends BaseActivity implements View.OnClickListener{
    private CheckBox cb_sure_identification;
    private Button btn_resident_record;
    private TextView tv_resident_name,tv_resident_mobile,tv_resident_identification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_info);
        initView();
    }

    public void initView() {
        findViewById(R.id.lLayout_title_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_common_title)).setText("住户信息");

        btn_resident_record = findViewById(R.id.btn_resident_record);
        btn_resident_record.setOnClickListener(this);
        btn_resident_record.setEnabled(false);

        cb_sure_identification = findViewById(R.id.cb_sure_identification);
        cb_sure_identification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_resident_record.setEnabled(isChecked);
            }
        });

        tv_resident_name = findViewById(R.id.tv_resident_name);
        tv_resident_name.setText(CommonUtils.formatNameWithStar("邓耀宁"));

        tv_resident_mobile = findViewById(R.id.tv_resident_mobile);
        tv_resident_mobile.setText(CommonUtils.formatPhoneNumWithStar("15277104415"));

        tv_resident_identification = findViewById(R.id.tv_resident_identification);
        tv_resident_identification.setText(CommonUtils.formatIdentityCardWithStar("452122199001140014"));

        GridView gv_resident_head = findViewById(R.id.gv_resident_head);
        ResidentHeadAdapter adapter = new ResidentHeadAdapter(this);
        gv_resident_head.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lLayout_title_back:
                finish();
                break;

            case R.id.btn_resident_record:
                startActivity(new Intent(this, FaceRecordActivity.class));
                break;
        }
    }
}
