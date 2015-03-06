package com.example.TinyShield;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 3/6/15.
 */

//get basic information of all applications on the system
public class AppScanner extends Activity{
    ArrayList appList;
    List<PackageInfo>packages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allapp);
        scanAll();
    }

    private void scanAll() {
        appList = new ArrayList<AppInfo>();
        packages= getPackageManager().getInstalledPackages(0);

        for(int i=0;i<packages.size();i++){
            PackageInfo packageInfo=packages.get(i);

            //ignore system application
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)!=0)
                continue;

            AppInfo tmpAppInfo= new AppInfo();
            tmpAppInfo.appName=packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            tmpAppInfo.appVersion=packageInfo.versionName;
            tmpAppInfo.versionCode=packageInfo.versionCode;
            tmpAppInfo.appPackageName=packageInfo.packageName;
            tmpAppInfo.appIcon=packageInfo.applicationInfo.loadIcon(getPackageManager());
            tmpAppInfo.permissionInfos= packageInfo.permissions;
            appList.add(tmpAppInfo);
        }
    }

}
