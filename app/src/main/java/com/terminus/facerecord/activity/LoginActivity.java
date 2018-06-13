package com.terminus.facerecord.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.terminus.facerecord.R;
import com.terminus.facerecord.constants.Config;
import com.terminus.facerecord.managers.LoginManager;
import com.terminus.facerecord.utils.CommonUtils;
import com.terminus.facerecord.utils.LogUtils;
import com.terminus.facerecord.utils.SPUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText et_login_password,et_login_mobile;
    private Button btn_login;
    private CheckBox ck_password_eye;
    private String curMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(SPUtils.getBoolean(this,"isLogin")){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        et_login_password = findViewById(R.id.et_login_password);
        et_login_mobile = findViewById(R.id.et_login_mobile);

        curMobile = SPUtils.getString(this, "lastMobile");
        if(!TextUtils.isEmpty(curMobile)){
            et_login_mobile.setText(CommonUtils.formatPhoneNumWithStar(curMobile));
        }
        et_login_mobile.setOnClickListener(this);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_login.setEnabled(false);

        ck_password_eye = findViewById(R.id.ck_password_eye);
        ck_password_eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    et_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    et_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        et_login_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_login_mobile.getText().length() == 11 && et_login_password.getText().length() >= 8){
                    btn_login.setEnabled(true);
                }else{
                    btn_login.setEnabled(false);
                }
            }
        });
        et_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et_login_mobile.getText().length() == 11 && et_login_password.getText().length() >= 8){
                    btn_login.setEnabled(true);
                }else{
                    btn_login.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                final String mobile = TextUtils.isEmpty(curMobile) ? et_login_mobile.getText().toString() : curMobile;
                final String password = et_login_password.getText().toString();
                if(CommonUtils.isPhoneNum(mobile)){
                    if(CommonUtils.checkPassword(password)){
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                showLoadingDialog(LoginActivity.this);
                                doLogin(mobile,password);
                            }
                        });
                    }else{
                        showTip("密码格式不正确");
                    }
                }else{
                    showTip("手机号格式不正确");
                }
                break;

            case R.id.et_login_mobile:
                if(!TextUtils.isEmpty(curMobile)){
                    curMobile = "";
                    et_login_mobile.setText("");
                }
                break;
        }
    }

    private void doLogin(String mobile, String password){
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

                startActivity(new Intent(this, HomeActivity.class));
                LoginManager.getInstance().setLogin();
                SPUtils.putString(this, "curMobile", mobile);
                SPUtils.putString(this, "lastMobile", mobile);
                finish();
            }
            response.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        dismissLoadingDialog();
    }
}

