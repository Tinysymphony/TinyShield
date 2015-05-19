package com.example.TinyShield;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by tiny on 5/15/15.
 */
public class SpyService extends Service {

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
