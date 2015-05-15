package com.example.TinyShield;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 3/6/15.
 */

//get basic information of all applications on the system
public class AppScanner extends Activity{

    private final static String SCAN="SCANNER";
    //private ListView listView;
    private ArrayList appList;
    private CoefficientCal coefficientCal = new CoefficientCal();
    SwipeMenuListView listView;
    PackageManager pm;

    private void scanAll() {
        appList = new ArrayList<AppInfo>();
        pm=this.getPackageManager();
        List<PackageInfo> packages= pm.getInstalledPackages(0);

        for(int i=0;i<packages.size();i++){
            PackageInfo packageInfo=packages.get(i);

            //ignore system application
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)!=0)
                continue;

            Log.d(SCAN, packageInfo.packageName);

            AppInfo tmpAppInfo= new AppInfo();
            tmpAppInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
            tmpAppInfo.setAppVersion(packageInfo.versionName);
            tmpAppInfo.setVersionCode(packageInfo.versionCode);
            tmpAppInfo.setAppPackageName(packageInfo.packageName);
            tmpAppInfo.setRisks(permissionCheck(packageInfo.packageName));
            tmpAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));
            tmpAppInfo.setRiskCoefficient();

            appList.add(tmpAppInfo);

        }
    }

    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {

            //create "more" item
            SwipeMenuItem moreItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            moreItem.setBackground(new ColorDrawable(Color.rgb(105, 176, 172))); //青磁
            // set item width
            moreItem.setWidth(90);
            moreItem.setTitleSize(18);
            moreItem.setTitle("分析");
            moreItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(moreItem);

            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0, 170, 144))); //青绿
            // set item width
            openItem.setWidth(90);
            // set item title
            openItem.setTitle("打开");
            openItem.setTitleColor(Color.WHITE);
            // set item title fontsize
            openItem.setTitleSize(18);
            // set item title font color
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(38, 135, 133))); //青碧
            // set item width
            deleteItem.setWidth(90);
            deleteItem.setTitleSize(18);
            deleteItem.setTitle("卸载");
            deleteItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.app_list);

        listView=(SwipeMenuListView)findViewById(R.id.mylist);

        scanAll();

        listView.setAdapter(new TinyAdapter(this));

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }

    public class TinyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public TinyAdapter(Context context){
            this.mInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount(){
            return appList.size();
        }

        @Override
        public Object getItem(int arg0){
            return null;
        }

        @Override
        public long getItemId(int arg0){
            return 0;
        }

        @Override
        public View getView(int position,View convertView, ViewGroup parent){
            AppInfo appInfo =(AppInfo) appList.get(position);
            ViewHolder itemHolder;
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.app_list_item,null);
                itemHolder = new ViewHolder();
                itemHolder.icon=(ImageView)convertView.findViewById(R.id.app);
                itemHolder.name=(TextView)convertView.findViewById(R.id.appName);
                itemHolder.pname=(TextView)convertView.findViewById(R.id.packageName);
                itemHolder.coefficient=(TextView)convertView.findViewById(R.id.coefficient);
                convertView.setTag(itemHolder);
            } else{
                itemHolder=(ViewHolder)convertView.getTag();
            }

            itemHolder.name.setText(appInfo.getAppName());
            itemHolder.pname.setText(appInfo.getAppPackageName());
            itemHolder.icon.setImageDrawable(appInfo.getAppIcon());
            itemHolder.coefficient.setText(appInfo.getRiskCoefficient());
            return convertView;
        }
    }

    class ViewHolder{
        TextView name=null;
        TextView pname=null;
        TextView coefficient=null;
        ImageView icon=null;
    }

    private boolean[] permissionCheck(String packageName){
        boolean []risk;
        risk = new boolean[4];
        risk[0] = isInternet(packageName);
        risk[1] = isReadSMS(packageName) && isSendSMS(packageName);
        risk[2] = isWriteSMS(packageName) && isSendSMS(packageName);
        risk[3] = isInstallPackage(packageName);
        return risk;
    }

    private boolean isInternet(String packageName){
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.INTERNET", packageName);
    }

    private boolean isReadSMS(String packageName){
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_SMS", packageName);
    }

    private boolean isSendSMS(String packageName){
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SEND_SMS", packageName);
    }

    private boolean isWriteSMS(String packageName){
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.WRITE_SMS", packageName);
    }

    private boolean isInstallPackage(String packageName){
        return PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.INSTALL_PACKAGES", packageName);
    }



}
