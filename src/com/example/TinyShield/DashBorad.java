package com.example.TinyShield;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.romainpiel.titanic.library.TitanicTextView;
import com.romainpiel.titanic.library.Typefaces;

import java.util.logging.Handler;

/**
 * Created by tiny on 5/17/15.
 */
public class DashBorad extends Activity {
    private Button appScanButton;
    private Button networkFlowButton;
    private Button processButton;
    private Button apkScanButton;
    private TitanicTextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dash_board);

        textView = (TitanicTextView) findViewById(R.id.top);
        textView.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));

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

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }
//
//                ).start();

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
    }

}
