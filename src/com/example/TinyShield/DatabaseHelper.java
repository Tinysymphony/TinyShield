package com.example.TinyShield;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    private final String CREATE_TABLE_PERMISSION =
            "create table permission(id varchar(100) primary key ,"
                    + "md5 var(100)"
                    +")";
    public DatabaseHelper(Context context, String name, int version) {
        super(context,name,null,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PERMISSION);
        initDatabase(db);
        Toast.makeText(mContext, "Database create success!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        switch(oldversion){

        }

    }

    public void initDatabase(SQLiteDatabase db){
        // Add data here

    }

}
