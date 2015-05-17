package com.example.TinyShield;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by tiny on 5/17/15.
 */
public class DashBorad extends Activity {
    private Button appScanButton;
    private Button networkFlowButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dash_board);


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

    }

}
