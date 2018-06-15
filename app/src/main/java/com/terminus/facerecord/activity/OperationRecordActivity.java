package com.terminus.facerecord.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.terminus.facerecord.R;
import com.terminus.facerecord.adapters.OperationRecordAdapter;
import com.terminus.facerecord.beans.OperationRecordBean;
import com.terminus.facerecord.constants.Config;
import com.terminus.facerecord.managers.LoginManager;
import com.terminus.facerecord.utils.LogUtils;
import com.terminus.facerecord.utils.SPUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class OperationRecordActivity extends BaseActivity implements View.OnClickListener{
    private ListView lv_operation_record;
    private TextView tv_record_no_data;
    private View layout_network_error;
    private Handler mHandler;
    private static final int MSG_NETWORK_ERROR = 0;
    private static final int MSG_HAS_NO_DATA = 1;
    private static final int MSG_NETWORK_CORRECT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_record);
        findViewById(R.id.lLayout_title_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_common_title)).setText("操作记录");
        tv_record_no_data = findViewById(R.id.tv_record_no_data);
        lv_operation_record = findViewById(R.id.lv_operation_record);
        layout_network_error = findViewById(R.id.layout_network_error);
        layout_network_error.findViewById(R.id.btn_network_error_retry).setOnClickListener(this);

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

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_NETWORK_ERROR:
                        showNetworkError();
                        break;

                    case MSG_HAS_NO_DATA:
                        hasNoData();
                        break;

                    case MSG_NETWORK_CORRECT:
                        showNetworkCorrect();
                        break;
                }
            }
        };
        requestData();
    }

    private void hasNoData(){
        tv_record_no_data.setVisibility(View.VISIBLE);
        lv_operation_record.setVisibility(View.GONE);
        layout_network_error.setVisibility(View.GONE);
    }

    private void showNetworkError(){
        tv_record_no_data.setVisibility(View.GONE);
        lv_operation_record.setVisibility(View.GONE);
        layout_network_error.setVisibility(View.VISIBLE);
    }

    private void showNetworkCorrect(){
        tv_record_no_data.setVisibility(View.GONE);
        lv_operation_record.setVisibility(View.VISIBLE);
        layout_network_error.setVisibility(View.GONE);
    }

    private void requestData(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                showLoadingDialog(OperationRecordActivity.this);
                try {
                    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                    RequestBody body = new RequestBody() {
                        @Nullable
                        @Override
                        public MediaType contentType() {
                            return null;
                        }

                        @Override
                        public void writeTo(BufferedSink sink) throws IOException {

                        }
                    };
                    Request request = new Request.Builder()
                            .url(Config.BASE_HOST + "time")
//                    .post(body)
                            .get()
                            .build();//创建Request 对象
                    Response response = client.newCall(request).execute();//得到Response 对象
                    LogUtils.d("kwwl", "response.code()==" + response.code());
                    if(response.code() == 200){
                        String json = response.body().string();
                        LogUtils.d("kwwl", "response.message()==" + response.message());
                        LogUtils.d("kwwl", "res==" + json);
                        JSONObject data = new JSONObject(json);
                        Message.obtain(mHandler,MSG_NETWORK_CORRECT).sendToTarget();
                    }else{
                        Message.obtain(mHandler,MSG_NETWORK_ERROR).sendToTarget();
                    }
                    response.close();
                }catch (Exception e){
                    e.printStackTrace();
                    Message.obtain(mHandler,MSG_NETWORK_ERROR).sendToTarget();
                }
                dismissLoadingDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lLayout_title_back:
                finish();
                break;
            case R.id.btn_network_error_retry:
                requestData();
                break;
        }
    }
}
