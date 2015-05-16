package com.example.TinyShield;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 5/16/15.
 */
public class NetworkFlow extends Activity {
    private final static String ACT = "Network Flow";
    private final static String NET = "android.permission.INTERNET";

    private PackageManager pm;
    private List<AppNetwork> appList = new ArrayList<AppNetwork>();

    //LineChart chart = (LineChart)findViewById(R.id.chart);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.network);

        pm = getPackageManager();
        scanNetworkFlow();

        //chart.setBackgroundColor(0x451023);

    }

    private void scanNetworkFlow(){
        List<PackageInfo> infos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        for(PackageInfo info : infos) {
            String[] permissions = info.requestedPermissions;
            if(permissions !=null && permissions.length>0){
                for(String permission : permissions)
                    if(NET.equals(permission)){
                        int userId = info.applicationInfo.uid;
                        AppNetwork app = new AppNetwork();
                        long rx = TrafficStats.getUidRxBytes(userId);
                        long tx = TrafficStats.getUidTxBytes(userId);
                        if( rx < 0 || tx < 0)
                            continue;
                        app.name = info.applicationInfo.loadLabel(pm).toString();
                        app.receivedMessage = rx;
                        app.sentMessage = tx;
                        appList.add(app);
                    }
            }
        }
    }

    class AppNetwork{
        public String name=null;
        public long receivedMessage=0;
        public long sentMessage=0;
    }



}
