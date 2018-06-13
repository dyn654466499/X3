package com.terminus.facerecord;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.terminus.facerecord.utils.CrashHandler;

public class MainApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        MultiDex.install(this);
    }
}
