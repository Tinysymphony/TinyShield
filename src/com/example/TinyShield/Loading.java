package com.example.TinyShield;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;
import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;
import com.romainpiel.titanic.library.Typefaces;

/**
 * Created by tiny on 5/18/15.
 */
public class Loading extends Activity {
    private Titanic titanic;
    private TitanicTextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.opening);

        titanic = new Titanic();
        title = (TitanicTextView)findViewById(R.id.loading);
        title.setTypeface(Typefaces.get(this, "fonts/Satisfy-Regular.ttf"));
        title.setText("Loading\nPlease wait.");
        titanic.start(title);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Loading.this.finish();
                Toast.makeText(getApplicationContext(), "扫描成功", Toast.LENGTH_SHORT).show();
            }
        },Integer.valueOf(getIntent().getIntExtra("delay", 50000)));

    }

}
