package com.example.TinyShield;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import com.me.drakeet.materialdialog.MaterialDialog;
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
    final static String MD5_SUCCESS = "校验成功--非重打包";
    final static String MD5_FAIL = "校验失败--疑似重打包";
    private static final int REFRESH = 1;
    private String SDPath = null;

    android.os.Handler handler = null;

    ArrayList<String> apkPath = new ArrayList<String>();
    ArrayList<String> MD5List = new ArrayList<String>();

    private ArrayList apkList;

    private ArrayList<Boolean> validList;

    private TitanicTextView textView;
    SwipeMenuListView listView;

    DatabaseHelper dbhelper ;
    SQLiteDatabase db ;

    private void packGet(List<PackageInfo> packages, ArrayList<String> apkPath) {

        PackageManager pm = this.getPackageManager();

        for (int i = 0; i < apkPath.size(); i++) {

            String archiveFilePath = apkPath.get(i);
            PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                packages.add(info);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.apk_scan);

        dbhelper = new DatabaseHelper(ApkScanner.this, "apk", 1);
        db = dbhelper.getReadableDatabase();

        SDPath = getSDPath();

        textView = (TitanicTextView) findViewById(R.id.top);
        textView.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));

        listView = (SwipeMenuListView) findViewById(R.id.apk_list);

        scanAll();

        final ApkAdapter apkAdapter = new ApkAdapter(this);

        listView.setAdapter(apkAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                AppInfo appInfo = (AppInfo) apkList.get(position);

                if (isApkInstalled(appInfo.getAppName())) {
                    Toast.makeText(ApkScanner.this, "您已经安装此应用", Toast.LENGTH_SHORT).show();
                } else {
                    String md5 = MD5List.get(position);

                    final String path = apkPath.get(position);
                    String result;

                    if (validList.get(position)) {
                        result = "安装包完好，非重打包，可以安装";
                    } else {
                        result = "安装包有改动，可能为重打包，不建议安装";
                    }

                    String message = "Apk安装包md5校验结果为：\n"
                            + md5 + "\n"
                            + "比对结果：\n"
                            + result;

                    final MaterialDialog installDialog = new MaterialDialog(ApkScanner.this);
                    installDialog.setTitle("安装包校验").setMessage(message)
                            .setPositiveButton("继续安装", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    installDialog.dismiss();
                                    Toast.makeText(ApkScanner.this, "开始安装", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setDataAndType(Uri.parse("file://" + path),
                                            "application/vnd.android.package-archive");
                                    ApkScanner.this.startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    installDialog.dismiss();
                                }
                            }).show();

                }
            }
        });

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        File file = new File(apkPath.get(position));
                        file.delete();
                        Message msg = new Message();
                        msg.what = REFRESH;
                        msg.arg1 = position;
                        handler.sendMessage(msg);
                        break;
                    case 1:
                        Toast.makeText(ApkScanner.this, "已经提交", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        handler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case REFRESH:
                        int position = msg.arg1;
                        apkPath.remove(position);
                        MD5List.remove(position);
                        apkList.remove(position);
                        validList.remove(position);
                        apkAdapter.notifyDataSetChanged();
                        break;
                    default:break;
                }
            }
        };
    }

    private SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {
            // create "open" item
            SwipeMenuItem openItem = new SwipeMenuItem(
                    getApplicationContext());
            openItem.setBackground(new ColorDrawable(Color.rgb(251, 191, 88)));
            openItem.setWidth(90);
            openItem.setTitle("删除");
            openItem.setTitleSize(18);
            openItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(openItem);

            SwipeMenuItem submitItem = new SwipeMenuItem(
                    getApplicationContext());
            submitItem.setBackground(new ColorDrawable(Color.rgb(19, 183, 210)));
            submitItem.setWidth(90);
            submitItem.setTitleSize(18);
            submitItem.setTitle("提交");
            submitItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(submitItem);
        }
    };

    public class ApkAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ApkAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return apkList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo appInfo = (AppInfo) apkList.get(position);
            ViewHolder itemHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.apk_scan_item, null);
                itemHolder = new ViewHolder();
                itemHolder.icon = (ImageView) convertView.findViewById(R.id.sdk);
                itemHolder.name = (TextView) convertView.findViewById(R.id.sdkName);
                itemHolder.pname = (TextView) convertView.findViewById(R.id.sdkPackageName);
                itemHolder.md5 = (TextView) convertView.findViewById(R.id.md5Value);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ViewHolder) convertView.getTag();
            }
            itemHolder.name.setText(appInfo.getAppName());
            itemHolder.pname.setText(appInfo.getAppPackageName());
            itemHolder.md5.setText(MD5List.get(position));
            itemHolder.icon.setImageDrawable(appInfo.getAppIcon());
            return convertView;
        }
    }

    class ViewHolder {
        TextView name = null;
        TextView pname = null;
        TextView md5 = null;
        ImageView icon = null;
    }

    private boolean isApkInstalled(String packagename) {
        PackageManager localPackageManager = getPackageManager();
        try {
            PackageInfo localPackageInfo = localPackageManager.getPackageInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            return false;
        }

    }

    private void scanAll() {
        PackageManager pm = this.getPackageManager();
        apkList = new ArrayList<AppInfo>();
        validList = new ArrayList<Boolean>();

        String itemName;

        List<PackageInfo> packages = new ArrayList<PackageInfo>();

        ApkScan.getFiles(SDPath, apkPath, MD5List);

        packGet(packages, apkPath);

        int itemCount = 0;
        final String testEmpty = "select count (*) from apk";
        Cursor cursor = db.rawQuery(testEmpty, null);
        if(cursor.moveToNext()){
            itemCount = cursor.getInt(0);
        }
        cursor.close();

        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);

            //ignore system application
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                continue;

            Log.d(SCAN, packageInfo.packageName);

            AppInfo tmpAppInfo = new AppInfo();

            tmpAppInfo.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
            tmpAppInfo.setAppPackageName(packageInfo.packageName);
            tmpAppInfo.setVersion(packageInfo.versionName + "/" +packageInfo.versionCode);
            tmpAppInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));

            ApplicationInfo appInfo = packageInfo.applicationInfo;
            appInfo.sourceDir = apkPath.get(i);
            appInfo.publicSourceDir = apkPath.get(i);

            apkList.add(tmpAppInfo);

            itemName = tmpAppInfo.getAppName() + tmpAppInfo.getVersion();

            final String findMD5 = "select md5 from apk where id = ?";
            cursor = db.rawQuery(findMD5, new String[]{itemName});

            if(cursor.moveToNext()){
                String md5value = cursor.getString(0);
                if(MD5List.get(i).equals(md5value)){
                    validList.add(new Boolean(true));
                }
                else{
                    validList.add(new Boolean(false));
                    Log.v(itemName,md5value);
                    Log.v(itemName, "false result");
                }
            }
            else if(itemCount == 0){
                final String insertion = "insert into apk(id,"
                        + "md5) "
                        + "values(?,?)";
                db.execSQL(insertion, new Object[]{itemName, MD5List.get(i)});
                validList.add(true);
                Log.v(itemName,MD5List.get(i));
            }
            else{
                validList.add(false);
            }

            cursor.close();
        }
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return sdDir.toString();
    }

    private String md5Compare(String name, String version, String md5){
        if(MD5get.checkFromServer(name, version.split("/")[0], version.split("/")[1], md5 ))
            return MD5_SUCCESS;
        else
            return MD5_FAIL;
    }

}



