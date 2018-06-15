package com.terminus.facerecord.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.terminus.facerecord.managers.AppManager;
import com.terminus.facerecord.utils.PermissionsUtils;
import com.terminus.facerecord.views.LoadingDialog;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BaseActivity extends AppCompatActivity {
    private Toast mToast;
    protected ThreadPoolExecutor executor;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppManager.getInstance().setAppContext(getApplicationContext());
        if (mToast == null)
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        if (executor == null)
            executor = new ThreadPoolExecutor(2, 5, 3000, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(3),
                    new ThreadPoolExecutor.DiscardOldestPolicy());

        PermissionsUtils.
                with(this).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.CALL_PHONE).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                initPermission();
    }

    /**
     * 标准控件——加载对话框
     */
    protected static LoadingDialog mLoadingDialog;

    protected void showLoadingDialog(Context ctx) {
        showLoadingDialog(ctx, null);
    }
    /**
     * 显示加载对话框
     * @param message 默认"正在加载..."
     */
    protected void showLoadingDialog(final Context ctx, final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                    mLoadingDialog = null;
                }
                mLoadingDialog = new LoadingDialog(ctx);
                mLoadingDialog.setCanceledOnTouchOutside(false);
                if (!TextUtils.isEmpty(message))
                    mLoadingDialog.setLoadingText(message);
                mLoadingDialog.show();
            }
        });
    }

    protected void setLoadingText(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(message) && mLoadingDialog != null){
                    mLoadingDialog.setLoadingText(message);
                }
            }
        });
    }

    /**
     * 使加载对话框消失
     */
    protected void dismissLoadingDialog() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                    mLoadingDialog = null;
                }
            }
        });
    }

    /**
     * 提示框
     * @param str
     */
    public void showTip(final String str) {
        if (!TextUtils.isEmpty(str)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToast.setText(str);
                    mToast.setGravity(Gravity.CENTER,0,0);
                    mToast.show();
                }
            });
        }
    }

    /**
     * 取消提示框
     */
    public void cancelShowTip() {
        if (mToast!=null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mToast.cancel();
                }
            });
        }
    }
}
