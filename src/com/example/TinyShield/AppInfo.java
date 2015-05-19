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
    private String version;
    private Drawable appIcon=null;
    private List<String>permissionList = new ArrayList<String>();

    public boolean permissionChanged = false;
    public int permissionCount = 0;
    public AppInfo(){
    }

    public void setPermissionList(String[] permissions){
        if(permissions != null)
            for (String permission : permissions) {
                permissionList.add(permission);
                permissionCount ++;
            }
    }

    public void setAppName(String name){
        appName=name;
    }

    public void setAppPackageName(String packageName){
        appPackageName=packageName;
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
        if(permissionList.contains("android.permission.READ_CONTACTS"))
            stringBuilder.append(" * " + "读取联系人" + "\n");
        if(permissionList.contains("android.permission.ACCESS_FINE_LOCATION"))
            stringBuilder.append(" * " + "GPS定位" + "\n");
        if(permissionList.contains("android.permission.BLUETOOTH_ADMIN"))
            stringBuilder.append(" * " + "管理蓝牙" + "\n");
        if(permissionList.contains("android.permission.CALL_PHONE"))
            stringBuilder.append(" * " + "拨打电话" + "\n");
        if(permissionList.contains("android.permission.MANAGE_ACCOUNTS"))
            stringBuilder.append(" * " + "管理账户" + "\n");
        if(permissionList.contains("android.permission.REBOOT"))
            stringBuilder.append(" * " + "重启手机" + "\n");
        if(permissionList.contains("android.permission.BRICK"))
            stringBuilder.append(" * " + "禁用手机" + "\n");
        if(permissionList.contains("android.permission.PROCESS_OUTGOING_CALLS"))
            stringBuilder.append(" * " + "监听修改或者放弃通话" + "\n");
        if(permissionList.contains("android.permission.READ_LOGS"))
            stringBuilder.append(" * " + "读取系统底层日志" + "\n");
        if(permissionList.contains("android.permission.WRITE_SECURE_SETTINGS"))
            stringBuilder.append(" * " + "修改系统安全设置项" + "\n");
        if(permissionList.contains("android.permission.KILL_BACKGROUND_PROCESSES"))
            stringBuilder.append(" * " + "结束后台进程" + "\n");
        if(permissionList.contains("android.permission.MOUNT_FORMAT_FILESYSTEMS"))
            stringBuilder.append(" * " + "格式化文件系统" + "\n");

        //print all
        //for(String string : permissionList)
        //    stringBuilder.append(string+"\n");

        if(permissionChanged)
            stringBuilder.append("警告：权限提升\n");

        return stringBuilder.toString();
    }

    public boolean isSMS(){
        return permissionList.contains("android.permission.WRITE_SMS")
                && permissionList.contains("android.permission.READ_SMS")
                && permissionList.contains("android.permission.SEND_SMS");
    }

    public boolean isContact(){
        return permissionList.contains("android.permission.READ_CONTACTS")
                && permissionList.contains("android.permission.INTERNET");
    }

    public boolean isFile(){
        return permissionList.contains("android.permission.MOUNT_FORMAT_FILESYSTEMS")
                && permissionList.contains("android.permission.MOUNT_UNMOUNT_FILESYSTEMS");
    }

    public boolean isBlue() {
        return permissionList.contains("android.permission.BLUETOOTH_ADMIN");
    }

    public void setVersion(String appVersion){
        version = appVersion;
    }

    public String getVersion(){
        return version;
    }


}

