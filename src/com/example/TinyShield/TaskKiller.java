package com.example.TinyShield;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.romainpiel.titanic.library.TitanicTextView;
import com.romainpiel.titanic.library.Typefaces;

import java.util.*;

/**
 * Created by LK on 2015/5/17.
 */
public class TaskKiller extends Activity {

    private final static int UPDATE_MEM = 1;
    private final static int REFRESH_TIME = 8000;
    private TitanicTextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.killer);

        textView = (TitanicTextView) findViewById(R.id.top);
        textView.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));

        ListView taskList = (ListView) findViewById(R.id.task_list);

        final List<Map<String, Object>> maplist = getData();
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, maplist, R.layout.killer_item,
                                                            new String[]{"taskname", "pid","memory"},
                                                            new int[]{R.id.processName, R.id.pidvalue, R.id.memoryvalue});
        taskList.setAdapter(simpleAdapter);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                final Map<String, Object> task = maplist.get(position);
                final int clickpos = position;

                AlertDialog.Builder dialog = new AlertDialog.Builder(TaskKiller.this);
                dialog.setTitle(task.get("taskname").toString());
                dialog.setMessage("Continue to kill this task?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {

                    ActivityManager activityManager = (ActivityManager) TaskKiller.this
                            .getSystemService(Context.ACTIVITY_SERVICE);

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activityManager.killBackgroundProcesses(task.get("taskname").toString());

                        List<Map<String, Object>> newmaplist = getData();
                        if(maplist.size() == newmaplist.size() + 1){

                            maplist.remove(clickpos);
                            simpleAdapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(TaskKiller.this,"System process, Killing failed!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

            }
        });


        final android.os.Handler handler = new android.os.Handler(){

            public void handleMessage(Message msg){

                switch(msg.what){

                    case UPDATE_MEM:
                        maplist.removeAll(maplist);
                        maplist.addAll(getData());
                        simpleAdapter.notifyDataSetChanged();
                        break;
                }

            }

        };

//        TimerTask ttask = new TimerTask() {
//
//            @Override
//            public void run() {
//                Message msg = new Message();
//                msg.what = UPDATE_MEM;
//                handler.sendMessage(msg);
//            }
//        };
//        Timer timer = new Timer(true);
//        timer.schedule(ttask,1000, REFRESH_TIME);

    }




    private List<Map<String, Object>> getData(){

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;

        final ActivityManager activityManager = (ActivityManager) TaskKiller.this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();

        Iterator<ActivityManager.RunningAppProcessInfo> appProcessList = services.iterator();

        while (appProcessList.hasNext()) {
            ActivityManager.RunningAppProcessInfo si =  appProcessList.next();

//            ApplicationInfo appInfo = null;
//            PackageManager pm = this.getPackageManager();
//
//            ImageView icon = null;
//            try {
//                appInfo = pm.getApplicationInfo(si.pkgList[0], PackageManager.GET_META_DATA);
//            }catch (Exception e){
//
//                System.out.println("Can not load appInfo");
//
//            }

           // icon.setImageDrawable(appInfo.loadIcon(pm));

            int[] pids = new int[1];
            pids[0] = si.pid;

            Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(pids);

            map = new HashMap<String, Object>();
            map.put("taskname", si.processName);
            map.put("pid",si.pid);
            map.put("memory",memoryInfo[0].getTotalPss());
            //map.put("icon", icon);

            list.add(map);

        }

        return list;
    }




}
