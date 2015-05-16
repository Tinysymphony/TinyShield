package com.example.TinyShield;

import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 3/6/15.
 */
public class AppInfo {
    private String appName;
    private String appPackageName;
    private String appVersion;
    private int versionCode;
    private Drawable appIcon=null;
    private List<String>permissionList = new ArrayList<String>();


    public AppInfo(){
    }


    public void setPermissionList(String[] permissions){
        if(permissions != null)
            for (String permission : permissions)
                permissionList.add(permission);
    }

    public void setAppName(String name){
        appName=name;
    }

    public void setAppPackageName(String packageName){
        appPackageName=packageName;
    }

    public void setAppVersion(String version){
        appVersion=version;
    }

    public  void setVersionCode(int code){
        versionCode=code;
    }

    public  void setAppIcon(Drawable icon){
        appIcon=icon;
    }


    public String getAppName(){ return this.appName; }

    public String getAppPackageName(){ return this.appPackageName; }

    public Drawable getAppIcon(){ return this.appIcon; }

    public String analysis(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "应用：" + getAppName() + "\n"
                + "敏感权限：\n"
        );
        if(permissionList.contains("android.permission.INTERNET"))
            stringBuilder.append(" * " + "使用网络" + "\n");
        if(permissionList.contains("android.permission.SEND_SMS"))
            stringBuilder.append(" * " + "发送短信" + "\n");
        if(permissionList.contains("android.permission.WRITE_SMS"))
            stringBuilder.append(" * " + "编写短信" + "\n");
        if(permissionList.contains("android.permission.READ_SMS"))
            stringBuilder.append(" * " + "查看短信" + "\n");
        if(permissionList.contains("android.permission.INSTALL_PACKAGES"))
            stringBuilder.append(" * " + "安装包" + "\n");

        //print all
        //for(String string : permissionList)
        //    stringBuilder.append(string+"\n");
        return stringBuilder.toString();
    }

}

