package com.example.TinyShield;

import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by tiny on 3/6/15.
 */
public class AppInfo {
    private String appName;
    private String appPackageName;
    private String appVersion;
    private int versionCode;
    private Drawable appIcon=null;
    private int[] rights = new int[20];
    private PermissionInfo[] permissionInfos;

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

    public void setRights(int[] getRights){
        rights=getRights;
    }

    public void setPermissionInfos(PermissionInfo[] getPermissions){
        permissionInfos=getPermissions;
    }

    public String getAppName(){ return this.appName; }

    public String getAppPackageName(){ return this.appPackageName; }

    public Drawable getAppIcon(){ return this.appIcon; }
}

