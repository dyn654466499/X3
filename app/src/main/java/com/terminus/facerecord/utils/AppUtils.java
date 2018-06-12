package com.terminus.facerecord.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

public class AppUtils {
    /**
     * 获取app签名
     * @param ctx
     * @return
     */
    public static String getAppSign(Context ctx) {
        try {
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return sign.toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
