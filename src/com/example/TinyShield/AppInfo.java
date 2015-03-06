package com.example.TinyShield;

import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by tiny on 3/6/15.
 */
public class AppInfo {
    public String appName;
    public String appPackageName;
    public String appVersion;
    public int versionCode;
    public Drawable appIcon=null;
    public int[] rights = new int[20];
    public PermissionInfo[] permissionInfos;
}
