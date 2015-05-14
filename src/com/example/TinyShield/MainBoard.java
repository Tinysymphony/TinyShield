package com.example.TinyShield;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainBoard extends Activity {
    /**
     * Called when the activity is first created.
     */
    private Button turnButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        turnButton=(Button)findViewById(R.id.turnToApp);
        turnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(MainBoard.this,AppScanner.class);
               startActivity(intent);
                //finish();
            }
        });

    }

}
