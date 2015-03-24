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
    private ListView listView;
    private ArrayList appList;

    ////
    //SwipeMenuListView listView;

    private void scanAll() {
        PackageManager pm=this.getPackageManager();
        appList = new ArrayList<AppInfo>();
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
            tmpAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));
            tmpAppInfo.setPermissionInfos(packageInfo.permissions);

            appList.add(tmpAppInfo);

        }
        //return appList;
    }

    ///////////////
//    private SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//        @Override
//        public void create(SwipeMenu menu) {
//            // create "open" item
//            SwipeMenuItem openItem = new SwipeMenuItem(
//                    getApplicationContext());
//            // set item background
//            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                    0xCE)));
//            // set item width
//            openItem.setWidth(90);
//            // set item title
//            openItem.setTitle("Open");
//            // set item title fontsize
//            openItem.setTitleSize(18);
//            // set item title font color
//            openItem.setTitleColor(Color.WHITE);
//            // add to menu
//            menu.addMenuItem(openItem);
//
//            // create "delete" item
//            SwipeMenuItem deleteItem = new SwipeMenuItem(
//                    getApplicationContext());
//            // set item background
//            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                    0x3F, 0x25)));
//            // set item width
//            deleteItem.setWidth(90);
//            openItem.setTitleSize(18);
//            openItem.setTitle("More");
//            // add to menu
//            menu.addMenuItem(deleteItem);
//        }
//    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.app_list);
        //scanAll();

        listView=(ListView)findViewById(R.id.mylist);
        // listView.setAdapter(new ItemAdapter(AppScanner.this,android.R.layout.activity_list_item, scanAll() ) );
        scanAll();
        listView.setAdapter(new TinyAdapter(this) );

        ////////////
//        listView.setMenuCreator(creator);
//
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        // open
//                        break;
//                    case 1:
//                        // delete
//                        break;
//                }
//                // false : close the menu; true : not close the menu
//                return false;
//            }
//        });

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
