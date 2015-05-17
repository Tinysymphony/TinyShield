package com.example.TinyShield;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.romainpiel.titanic.library.TitanicTextView;
import com.romainpiel.titanic.library.Typefaces;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LK on 2015/5/16.
 */
public class ApkScanner extends Activity {

    static final String SCAN = "APK Scan";
    private String SDPath = null;


    ArrayList<String> apkPath = new ArrayList<String>();
    ArrayList<String> MD5List = new ArrayList<String>();

    private ArrayList apkList;

    private TitanicTextView textView;
    SwipeMenuListView listView;

    private void packGet(List<PackageInfo> packages, ArrayList<String> apkPath){

        PackageManager pm = this.getPackageManager();

        for(int i = 0; i < apkPath.size();i++){

            String archiveFilePath = apkPath.get(i);
            PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
            if(info != null) {
                packages.add(info);
            }
        }
    }

    private void scanAll() {
        PackageManager pm=this.getPackageManager();
        apkList = new ArrayList<AppInfo>();
        //List<PackageInfo> packages= pm.getInstalledPackages(0);

        List<PackageInfo> packages = new ArrayList<PackageInfo>();


        APKScan.getFiles(SDPath, apkPath, MD5List);

        packGet(packages, apkPath);

        for(int i=0;i<packages.size();i++){
            PackageInfo packageInfo=packages.get(i);


            //ignore system application
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)!=0)
                continue;

            Log.d(SCAN, packageInfo.packageName);


            AppInfo tmpAppInfo= new AppInfo();

            tmpAppInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
            tmpAppInfo.setAppPackageName(packageInfo.packageName);

            ApplicationInfo appInfo = packageInfo.applicationInfo;
            appInfo.sourceDir = apkPath.get(i);
            appInfo.publicSourceDir = apkPath.get(i);

            tmpAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));

            apkList.add(tmpAppInfo);

        }
    }

    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                    0xCE)));
            // set item width
            openItem.setWidth(90);
            // set item title
            openItem.setTitle("Open");
            // set item title fontsize
            openItem.setTitleSize(18);
            // set item title font color
            openItem.setTitleColor(Color.WHITE);
            // add to menu
            menu.addMenuItem(openItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                    0x3F, 0x25)));
            // set item width
            deleteItem.setWidth(90);
            openItem.setTitleSize(18);
            openItem.setTitle("More");
            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.apk_scan);

        SDPath = getSDPath();

        textView = (TitanicTextView) findViewById(R.id.top);
        textView.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));

        listView=(SwipeMenuListView)findViewById(R.id.apk_list);

        scanAll();
        listView.setAdapter(new ApkAdapter(this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String md5 = MD5List.get(position);
                    AppInfo appInfo = (AppInfo) apkList.get(position);
                    final String path = apkPath.get(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ApkScanner.this);
                dialog.setTitle(appInfo.getAppName());
                dialog.setMessage("The MD5 check value of the APK is:" + md5 + ", please make sure this value is correct before you install the apk file!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Install", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(Uri.parse("file://" + path),
                                "application/vnd.android.package-archive");
                        ApkScanner.this.startActivity(intent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                if(isApkInstalled(appInfo.getAppName())){
                    Toast.makeText(ApkScanner.this, "You have installed this apk!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.show();
                }

            }
        });

        ////////////
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        // delete

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DELETE);
                        AppInfo appInfo = (AppInfo) apkList.get(position);
                        String packageName = appInfo.getAppPackageName();
                        intent.setData(Uri.parse("package:"+packageName));
                        startActivity(intent);

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }


    public class ApkAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ApkAdapter(Context context){
            this.mInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount(){
            return apkList.size();
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
            AppInfo appInfo =(AppInfo) apkList.get(position);
            ViewHolder itemHolder;
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.apk_scan_item,null);
                itemHolder = new ViewHolder();
                itemHolder.icon=(ImageView)convertView.findViewById(R.id.sdk);
                itemHolder.name=(TextView)convertView.findViewById(R.id.sdkName);
                itemHolder.pname=(TextView)convertView.findViewById(R.id.sdkPackageName);
                itemHolder.md5=(TextView)convertView.findViewById(R.id.md5Value);
                convertView.setTag(itemHolder);
            } else{
                itemHolder=(ViewHolder)convertView.getTag();
            }
            itemHolder.name.setText(appInfo.getAppName());
            itemHolder.pname.setText(appInfo.getAppPackageName());
            itemHolder.md5.setText(MD5List.get(position));
            itemHolder.icon.setImageDrawable(appInfo.getAppIcon());
            return convertView;
        }
    }

    class ViewHolder{
        TextView name=null;
        TextView pname=null;
        TextView md5=null;
        ImageView icon=null;
    }

    private boolean isApkInstalled(String packagename)
    {
        PackageManager localPackageManager = getPackageManager();
        try
        {
            PackageInfo localPackageInfo = localPackageManager.getPackageInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
            return false;
        }

    }



    private String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }


}
