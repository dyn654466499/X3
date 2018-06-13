package com.terminus.facerecord.managers;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.terminus.facerecord.activity.LoginActivity;
import com.terminus.facerecord.events.LoginEvent;
import com.terminus.facerecord.utils.SPUtils;

import de.greenrobot.event.EventBus;


/**
 *
 */

public class LoginManager {

    private static LoginManager loginManager;
    private Context mContext = AppManager.getInstance().getAppContext();
    private LoginManager() {

    }
    public static LoginManager getInstance() {
        if (loginManager == null) {
            synchronized (LoginManager.class) {
                loginManager = new LoginManager();
            }
        }
        return loginManager;
    }

    /**
     * 保存用户信息
     */
    public void setLogin(){
        SPUtils.putBoolean(mContext, "isLogin", true);
    }

    public boolean isLogin(){
        return SPUtils.getBoolean(mContext, "isLogin");
    }

    public boolean isLoginOrDoLogin(){
        boolean flag = SPUtils.getBoolean(mContext, "isLogin");
        if(!flag){
            login();
        }
        return flag;
    }

    /**
     *  登录
     */
    public void login(Context context){
        Context ctx = context == null ? mContext : context;
        Intent intent = new Intent(ctx, LoginActivity.class);
        if(context == null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(intent);
    }

    /**
     *  登录
     */
    public void login(){
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     *
     */
    public void logout(){
        SPUtils.putString(mContext, "curMobile", "");
        SPUtils.putBoolean(mContext,"isLogin", false);
        EventBus.getDefault().post(Message.obtain(null, LoginEvent.DO_LOGOUT));
        login();
    }

}
