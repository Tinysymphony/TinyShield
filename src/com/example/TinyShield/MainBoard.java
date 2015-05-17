package com.example.TinyShield;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
//import android.widget.Button;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;
import com.romainpiel.titanic.library.Typefaces;

public class MainBoard extends Activity {
    /**
     * Called when the activity is first created.
     */
    private final static int DELAY = 4000;
//    private Button appScanButton;
//    private Button networkFlowButton;
    private Titanic titanic;
    private TitanicTextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.opening);

        titanic = new Titanic();
        textView = (TitanicTextView)findViewById(R.id.loading);
        textView.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        titanic.start(textView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                titanic.cancel();
                Intent intent = new Intent(MainBoard.this, DashBorad.class);
                startActivity(intent);
                finish();
            }
        },DELAY);

//        appScanButton = (Button)findViewById(R.id.turnToApp);
//        appScanButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainBoard.this, AppScanner.class);
//                startActivity(intent);
//            }
//        });
//
//        networkFlowButton = (Button)findViewById(R.id.turnToNetwork);
//        networkFlowButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//        public  void onClick(View v){
//                Intent intent = new Intent(MainBoard.this, NetworkFlow.class);
//                startActivity(intent);
//            }
//        });

    }

}
