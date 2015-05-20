package com.example.TinyShield;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.me.drakeet.materialdialog.MaterialDialog;
import com.romainpiel.titanic.library.TitanicTextView;
import com.romainpiel.titanic.library.Typefaces;

/**
 * Created by tiny on 5/17/15.
 */
public class DashBorad extends Activity {

    private Button appScanButton;
    private Button networkFlowButton;
    private Button processButton;
    private Button apkScanButton;
    private Button logButton;
    private Button exitButton;

    private DrawerArrowDrawable drawerArrow;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView menuList;
    //private TitanicTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board);

        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.main);
        menuList = (ListView)findViewById(R.id.menu);

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        String[] values = new String[]{
                " ・Github Page",
                " ・Guides",
                " ・Doc",
                " ・Email",
                " ・About us",
                " ・WyTiny"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        menuList.setAdapter(adapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB));
                        startActivity(browserIntent);
                        break;
                    case 1:
                        final MaterialDialog guides = new MaterialDialog(DashBorad.this);
                        guides.setTitle("Guides").setMessage(GUIDES).show();
                        break;
                    case 2:
                        final MaterialDialog doc = new MaterialDialog(DashBorad.this);
                        doc.setTitle("Doc").setMessage(DOC).show();
                        break;
                    case 3:
                        final MaterialDialog mail = new MaterialDialog(DashBorad.this);
                        mail.setTitle("Send mail").setMessage(EMAIL).show();
                        break;
                    case 4:
                        final MaterialDialog about = new MaterialDialog(DashBorad.this);
                        about.setTitle("About us").setMessage(ABOUTUS).show();
                        break;
                    case 5:
                        final MaterialDialog wytiny= new MaterialDialog(DashBorad.this);
                        wytiny.setTitle("WyTiny").setMessage(WYTINY).show();
                        break;
                }

            }
        });

//        textView = (TitanicTextView) findViewById(R.id.top);
//        textView.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));

        appScanButton = (Button)findViewById(R.id.turnToApp);
        appScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBorad.this, AppScanner.class);
                startActivity(intent);
            }
        });

        networkFlowButton = (Button)findViewById(R.id.turnToNetwork);
        networkFlowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                Intent intent = new Intent(DashBorad.this, NetworkFlow.class);
                startActivity(intent);
            }
        });

        apkScanButton = (Button)findViewById(R.id.turnToApk);
        apkScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DashBorad.this, ApkScanner.class);
                startActivity(intent);

            }
        });

        processButton = (Button)findViewById(R.id.turnToKiller);
        processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBorad.this, TaskKiller.class);
                startActivity(intent);
            }
        });

        logButton = (Button)findViewById(R.id.log);

        exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(menuList)) {
                mDrawerLayout.closeDrawer(menuList);
            } else {
                mDrawerLayout.openDrawer(menuList);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private final static String GITHUB = "https://github.com/Tinysymphony/TinyShield";
    private final static String EMAIL ="Send email to get more support from developers:\n・zjutiny@gmail.com\n・huliangze@yeah.net";
    private final static String GUIDES ="1.At the first time of starting our app, two database tables are to be created and record permission info and md5 info.\n" +
            "2.You can use the app scan module to get a entire analysis of all apps in your phone, you may remove apps that gain bad results.\n" +
            "3.Apk scan helps to detect the apks that are re-packed, which may contains malicious code segments or ads.\n" +
            "4.Network flow and process manager are auxiliary tools to protect your phone.\n" +
            "5.The log service might not be as perfect as designed.";
    private final static String DOC ="For docs please turn to TinySheild github page.";
    private final static String ABOUTUS ="・王艺\nZJU CS 3120101996\n" + "・胡亮泽\n ZJU CS 3120102116";
    private final static String WYTINY ="Blog:\nwww.wytiny.me";
}
