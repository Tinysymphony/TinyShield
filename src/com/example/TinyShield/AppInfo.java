package com.example.TinyShield;

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
    private int[] rights = new int[20];
    private List<String>permissionList = new ArrayList<String>();

    private char[] riskCoefficient = new char[4];
    private boolean[] risks = new boolean[4];

    public AppInfo(){
        for(boolean item : risks)
            item = false;
        for(char item : riskCoefficient)
            item = '1';

    }

    public void setRisks(boolean[] inputRisks){
        risks = inputRisks;
    }

    public void setRiskCoefficient(){
        for(int i=0; i<4; i++){
            if(risks!=null)
                if(risks[i] != false)
                    riskCoefficient[i] = '1';
                else
                    riskCoefficient[i] = '0';
        }
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

    public void setRights(int[] getRights){
        rights=getRights;
    }

    public String getRiskCoefficient() { return String.valueOf(riskCoefficient); }

    public String getAppName(){ return this.appName; }

    public String getAppPackageName(){ return this.appPackageName; }

    public Drawable getAppIcon(){ return this.appIcon; }

    public String[] getPermissionList() { return (String[])permissionList.toArray(); }


}

