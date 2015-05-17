package com.example.TinyShield;

import android.app.Activity;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.cengalabs.flatui.views.FlatTextView;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiny on 5/16/15.
 */
public class NetworkFlow extends Activity {
    private final static String ACT = "Network Flow";
    private final static String NET = "android.permission.INTERNET";

    private ListView listView;
    private PackageManager pm;
    private List<AppNetwork> appList = new ArrayList<AppNetwork>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.network);

        pm = getPackageManager();

        scanNetworkFlow();

        listView = (ListView)findViewById(R.id.networkList);
        listView.setAdapter(new NetAdapter(this));

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
                        app.icon=info.applicationInfo.loadIcon(pm);
                        appList.add(app);
                    }
            }
        }
        Log.d(ACT, "Finish getting the network flow data");
    }

    private class NetAdapter extends BaseAdapter{
        private LayoutInflater mInflater;

        public NetAdapter(Context context){
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
            AppNetwork app = (AppNetwork) appList.get(position);
            ViewHolder holder;
            if(convertView==null){
                convertView = mInflater.inflate(R.layout.net_list_item,null);
                holder = new ViewHolder();
                holder.icon=(ImageView)convertView.findViewById(R.id.network);
                holder.name=(TextView)convertView.findViewById(R.id.appName);
                holder.received=(TextView)convertView.findViewById(R.id.receivedData);
                holder.sent=(TextView)convertView.findViewById(R.id.sentData);

                convertView.setTag(holder);
            } else{
                holder=(ViewHolder)convertView.getTag();
            }

            holder.name.setText(app.name);
            holder.received.setText(String.valueOf(app.receivedMessage) + " Bytes");
            holder.sent.setText(String.valueOf(app.sentMessage) + " Bytes");
            holder.icon.setImageDrawable(app.icon);

            convertView.setBackgroundColor(getFocus(app.receivedMessage + app.sentMessage));

            return convertView;
        }

    }

    private class AppNetwork{
        public String name=null;
        public long receivedMessage=0;
        public long sentMessage=0;
        public Drawable icon=null;
    }

    private class ViewHolder{
        TextView name=null;
        TextView received=null;
        TextView sent=null;
        ImageView icon=null;
    }

    //using different color according to data
    private int getFocus(long data){
        if(data != 0)
            if(data > 100000)
                return getResources().getColor(R.color.blood_dark);
            else if(data > 20000)
                return getResources().getColor(R.color.orange_dark);
            else
                return getResources().getColor(R.color.sand_dark);
        else
            return getResources().getColor(R.color.snow_light);
    }

}
