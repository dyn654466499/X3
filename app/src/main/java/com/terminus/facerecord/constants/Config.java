package com.terminus.facerecord.constants;

import com.terminus.facerecord.BuildConfig;

public class Config {
    private static final String PRD_HOST = "http://192.168.2.188:8080/v1/pwd/";
    private static final String STG_HOST = "http://192.168.2.188:8080/v1/pwd/";
    public static boolean isPrd = BuildConfig.IS_PRD;
    public static final String BASE_HOST = isPrd ? PRD_HOST : STG_HOST;
}
