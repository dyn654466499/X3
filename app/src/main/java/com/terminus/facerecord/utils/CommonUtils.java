package com.terminus.facerecord.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 邓耀宁 on 2018/4/8.
 */

public class CommonUtils {
    public static boolean isPad(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.0) {
            return true;
        }
        return false;
    }

    public static String getJsonFromAssets(Context context,String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    /**
     * 既然有短信验证，那么放开有效手机号的验证
     * @param mobile
     * @return
     */
    public static boolean isBasePhone(String mobile){
        Pattern p1 = Pattern
                .compile("^(1[0-9])\\d{9}$");
        Matcher m1 = p1.matcher(mobile);
        return m1.matches();
    }

    /**
     * 验证是否是有效手机号
     * 条件：
     * 以+86开头或者是11位有效手机号
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）、177
     *
     * @param mobiles
     * @return
     */
    public static boolean isPhoneNum(String mobiles){
        Pattern p2 = Pattern
                .compile("^(\\+?86)\\d{11}$");
        Matcher m = p2.matcher(mobiles);

        if(mobiles.length()==11){
            return isBasePhone(mobiles);

        }else if(mobiles.length()>11 && m.matches()){
            String mobile=mobiles.substring(3);
            return isBasePhone(mobile);
        }
        return false;

    }
    /**
     * 验证是否是以“+86”开头的手机号码
     * @return
     */
    public static boolean isPhonePre(String phoneNum){
        Pattern p2 = Pattern
                .compile("^(\\+?86)\\d{11}$");
        Matcher m = p2.matcher(phoneNum);

        if(m.matches()){
            String mobile = phoneNum.substring(3);
            return isBasePhone(mobile);
        }
        return false;
    }

    public static boolean checkPassword(String pwd){
        if(!TextUtils.isEmpty(pwd)){
            boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
            boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
            for (int i = 0; i < pwd.length(); i++) {
                if (Character.isDigit(pwd.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                    isDigit = true;
                } else if (Character.isLowerCase(pwd.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                    isLowerCase = true;
                }
            }
            String regex = "^[a-zA-Z0-9]+$";
            boolean isRight = pwd.length() >= 8 && isDigit && isLowerCase && pwd.matches(regex);
            return isRight;
        }
        return false;
    }

    /**
     * 判断是否有SD卡
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式化手机号码(135*****7710)
     * @param phoneNum
     * @return
     */
    public static String formatPhoneNumWithStar(String phoneNum){
        if(TextUtils.isEmpty(phoneNum)) {
            return phoneNum;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phoneNum.length(); i++) {
            char c = phoneNum.charAt(i);
            if (i >= 3 && i <= 6) {
                sb.append('*');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 格式化人名(**宁)
     * @param name
     * @return
     */
    public static String formatNameWithStar(String name){
        if(TextUtils.isEmpty(name)) {
            return name;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (i != name.length() - 1) {
                sb.append('*');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 格式化身份证(**宁)
     * @param cardNum
     * @return
     */
    public static String formatIdentityCardWithStar(String cardNum){
        if(TextUtils.isEmpty(cardNum)) {
            return cardNum;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cardNum.length(); i++) {
            char c = cardNum.charAt(i);
            if (i > 2 && i < cardNum.length() - 3) {
                sb.append('*');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
