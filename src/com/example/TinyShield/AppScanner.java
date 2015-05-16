package com.example.TinyShield;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.preference.DialogPreference;
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
        List<PackageInfo> packages= pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);

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
            tmpAppInfo.setPermissionList(packageInfo.requestedPermissions);
            tmpAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));

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
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Toast.makeText(AppScanner.this, "0", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder analysisBuilder = new AlertDialog.Builder(AppScanner.this);
                        analysisBuilder.setIcon(R.drawable.ic_launcher);  //TODO
                        analysisBuilder.setTitle("分析报告");
                        AppInfo app = (AppInfo) appList.get(position);
                        analysisBuilder.setMessage(app.analysis());
                        analysisBuilder.create().show();
                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent = pm.getLaunchIntentForPackage(((AppInfo)appList.get(position)).getAppPackageName());
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(AppScanner.this, "2", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder unistallBuilder = new AlertDialog.Builder(AppScanner.this);
                        unistallBuilder.setIcon(R.drawable.ic_launcher);  //TODO
                        unistallBuilder.setTitle("卸载警告");
                        unistallBuilder.setMessage("确定卸载该应用吗？");
                        unistallBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AppScanner.this, "开始卸载应用", Toast.LENGTH_SHORT).show();
                                Uri uri = Uri.parse("package:" + ((AppInfo) appList.get(position)).getAppPackageName());
                                Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                                startActivity(intent);
                            }
                        });
                        unistallBuilder.setNegativeButton("取消", null);
                        unistallBuilder.create().show();
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
                convertView.setTag(itemHolder);
            } else{
                itemHolder=(ViewHolder)convertView.getTag();
            }

            itemHolder.name.setText(appInfo.getAppName());
            itemHolder.pname.setText(appInfo.getAppPackageName());
            itemHolder.icon.setImageDrawable(appInfo.getAppIcon());
            return convertView;
        }
    }

    class ViewHolder{
        TextView name=null;
        TextView pname=null;
        ImageView icon=null;
    }

}
