package com.terminus.facerecord.managers;

import android.content.Context;

/**
 * Created by dengyaoning on 17/5/10.
 */

public class AppManager {
    private static AppManager appManager;
    private AppManager() {

    }
    public static AppManager getInstance() {
        if (appManager == null) {
            synchronized (AppManager.class) {
                appManager = new AppManager();
            }
        }
        return appManager;
    }

    private Context appContext;

    public void setAppContext(Context context){
        appContext = context.getApplicationContext();
    }

    public Context getAppContext(){
        return appContext;
    }

    public void release(){
        appContext = null;
    }
}
