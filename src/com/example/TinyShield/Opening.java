package com.example.TinyShield;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;
import com.romainpiel.titanic.library.Typefaces;

public class Opening extends Activity {
    /**
     * Called when the activity is first created.
     */
    private final static int DELAY = 4000;
    private Titanic titanic;
    private TitanicTextView title;
    private TitanicTextView click;
    private boolean isLoad = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.opening);

        titanic = new Titanic();
        title = (TitanicTextView)findViewById(R.id.loading);
        title.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));
        titanic.start(title);

        click = (TitanicTextView)findViewById(R.id.click);
        click.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                title.setTextColor(Color.WHITE);
                click.setTextColor(Color.WHITE);
                click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Opening.this, DashBorad.class);
                        startActivity(intent);
                        finish();
                    }
                });
                titanic.cancel();
                isLoad = true;
            }
        },DELAY);

    }



}
