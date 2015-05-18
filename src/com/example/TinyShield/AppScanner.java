package com.example.TinyShield;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.cengalabs.flatui.views.FlatRadioButton;
import com.cengalabs.flatui.views.FlatTextView;
import com.me.drakeet.materialdialog.MaterialDialog;
import com.romainpiel.titanic.library.TitanicTextView;
import com.romainpiel.titanic.library.Typefaces;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 3/6/15.
 */

//get basic information of all applications on the system
public class AppScanner extends Activity{

    private final static String ACT ="Scanner";
    private TitanicTextView textView;
    private ArrayList appList;
    private SwipeMenuListView listView;
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

            AppInfo tmpAppInfo= new AppInfo();
            tmpAppInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
            tmpAppInfo.setAppPackageName(packageInfo.packageName);
            tmpAppInfo.setPermissionList(packageInfo.requestedPermissions);
            tmpAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));

            appList.add(tmpAppInfo);

        }
        Log.d(ACT, "Finish scanning.");
    }

    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {

            //create "more" item
            SwipeMenuItem moreItem = new SwipeMenuItem(
                    getApplicationContext());
            moreItem.setBackground(new ColorDrawable(Color.rgb(105, 176, 172))); //青磁
            moreItem.setWidth(90);
            moreItem.setTitleSize(18);
            moreItem.setTitle("分析");
            moreItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(moreItem);

            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            openItem.setBackground(new ColorDrawable(Color.rgb(0, 170, 144))); //青绿
            openItem.setWidth(90);
            openItem.setTitle("打开");
            openItem.setTitleColor(Color.WHITE);
            openItem.setTitleSize(18);
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            deleteItem.setBackground(new ColorDrawable(Color.rgb(38, 135, 133))); //青碧
            deleteItem.setWidth(90);
            deleteItem.setTitleSize(18);
            deleteItem.setTitle("卸载");
            deleteItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(deleteItem);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.app_list);

        textView = (TitanicTextView) findViewById(R.id.top);
        textView.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));

        scanAll();

        listView=(SwipeMenuListView)findViewById(R.id.mylist);
        listView.setAdapter(new InfoAdapter(this));
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        AppInfo app = (AppInfo) appList.get(position);
                        final MaterialDialog analysisDialog = new MaterialDialog(AppScanner.this);
                        analysisDialog.setTitle("分析报告").setMessage(app.analysis())
                                .setPositiveButton("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        analysisDialog.dismiss();
                                    }
                                }).show();

                        break;
                    case 1:
                        Intent intent = new Intent();
                        intent = pm.getLaunchIntentForPackage(((AppInfo)appList.get(position)).getAppPackageName());
                        startActivity(intent);
                        break;
                    case 2:
                        AppInfo tmpApp = (AppInfo) appList.get(position);
                        final MaterialDialog unistallDialog = new MaterialDialog(AppScanner.this);
                        final String target = tmpApp.getAppName();
                        unistallDialog.setTitle("卸载应用").setMessage("确定要卸载"+target+"?")
                                .setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        unistallDialog.dismiss();
                                        Toast.makeText(AppScanner.this, "开始卸载", Toast.LENGTH_SHORT).show();
                                        Uri uri = Uri.parse("package:" + target);
                                    }
                                })
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        unistallDialog.dismiss();
                                    }
                                }) .show();

                        break;
                }
                return false;
            }
        });

        }

        public class InfoAdapter extends BaseAdapter {
            private LayoutInflater mInflater;

            public InfoAdapter(Context context){
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
                    itemHolder.name=(FlatTextView)convertView.findViewById(R.id.appName);
                    itemHolder.pname=(FlatTextView)convertView.findViewById(R.id.packageName);
                    convertView.setTag(itemHolder);
                } else{
                    itemHolder=(ViewHolder)convertView.getTag();
                }

                if(appInfo.isSMS())
                    ((FlatRadioButton)convertView.findViewById(R.id.sms)).setChecked(true);

                if(appInfo.isFile())
                    ((FlatRadioButton)convertView.findViewById(R.id.file)).setChecked(true);

                if(appInfo.isContact())
                    ((FlatRadioButton)convertView.findViewById(R.id.contact)).setChecked(true);


                itemHolder.name.setText(appInfo.getAppName());
                itemHolder.pname.setText(appInfo.getAppPackageName());
                itemHolder.icon.setImageDrawable(appInfo.getAppIcon());
                return convertView;
            }
        }

    private class ViewHolder{
        FlatTextView name=null;
        FlatTextView pname=null;
        ImageView icon=null;
    }

}
