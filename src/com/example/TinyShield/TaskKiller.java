package com.example.TinyShield;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.me.drakeet.materialdialog.MaterialDialog;
import com.romainpiel.titanic.library.TitanicTextView;
import com.romainpiel.titanic.library.Typefaces;

import java.util.*;

/**
 * Created by LK on 2015/5/17.
 */
public class TaskKiller extends Activity {

    private final static int REFRESH_TIME = 8000;
    private TitanicTextView textView;

    final static int UPDATE_MEM = 1;
    final static int UPDATE_COMPLETE = 2;
    final static int IF_KILL = 3;
    final static int KILL_FAILED = 4;

    boolean lock = false;
    boolean kill_lock = false;
    int kill_position;

    android.os.Handler handler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.killer);

//        Intent load = new Intent(TaskKiller.this, Loading.class);
//        load.putExtra("delay", 10000);
//        startActivity(load);

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

                final MaterialDialog dialog = new MaterialDialog(TaskKiller.this);
                dialog.setTitle("进程管理").setMessage("确定要结束进程？\n" + task.get("taskname").toString());
                dialog.setPositiveButton("确定", new View.OnClickListener() {

                    ActivityManager activityManager = (ActivityManager) TaskKiller.this
                            .getSystemService(Context.ACTIVITY_SERVICE);

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (!kill_lock) {
                            activityManager.killBackgroundProcesses(task.get("taskname").toString());
                            kill_position = clickpos;
                            Message msg = new Message();
                            msg.what = IF_KILL;
                            handler.sendMessage(msg);
                        } else {
                            Toast.makeText(TaskKiller.this, "Waiting for another task to be done", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });


         handler = new android.os.Handler(){

            public void handleMessage(Message msg){

                switch(msg.what){
                    case UPDATE_MEM:
//                        maplist.removeAll(maplist);
//                        maplist.addAll(getData());
//                        simpleAdapter.notifyDataSetChanged();
                        if(!lock) {
                            lock = true;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    List<Map<String,Object>> newmaplist = getData();

                                    int len = maplist.size();
                                    Map<String, Object> map, newmap;
                                    for(int i = 0; i < len; i++){
                                        map = maplist.get(i);
                                        newmap = newmaplist.get(i);
                                        map.put("memory",newmap.get("memory"));
                                    }

                                    Message msg = new Message();
                                    msg.what = UPDATE_COMPLETE;
                                    handler.sendMessage(msg);
                                }
                            }).start();
                        }
                        break;

                    case UPDATE_COMPLETE:
                        simpleAdapter.notifyDataSetChanged();
                        lock = false;
                        break;

                    case IF_KILL:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                while(lock){}
                                lock = true;
                                List<Map<String, Object>> newmaplist = getData();

                                if(maplist.size() == newmaplist.size() + 1){
                                    while(kill_lock){}
                                    kill_lock = true;
                                    maplist.remove(kill_position);
                                    kill_lock = false;
                                    Message msg = new Message();
                                    msg.what = UPDATE_COMPLETE;
                                    handler.sendMessage(msg);
                                }
                                else{
                                    Message msg = new Message();
                                    msg.what = KILL_FAILED;
                                    handler.sendMessage(msg);
                                }
                            }
                        }).start();
                        break;

                    case KILL_FAILED:
                        Toast.makeText(TaskKiller.this,"System process, Killing failed!",Toast.LENGTH_SHORT).show();
                        lock = false;
                        break;

                }

            }

        };

        TimerTask ttask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                msg.what = UPDATE_MEM;
                handler.sendMessage(msg);
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(ttask,1000, REFRESH_TIME);
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

            int[] pids = new int[1];
            pids[0] = si.pid;

            Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(pids);

            map = new HashMap<String, Object>();
            map.put("taskname", si.processName);
            map.put("pid",si.pid);
            map.put("memory",memoryInfo[0].getTotalPss());

            list.add(map);

        }

        return list;
    }

    // map to string
    // split and fetch data with explicit index.


}
