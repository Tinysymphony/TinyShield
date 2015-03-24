package com.example.TinyShield;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tiny on 3/16/15.
 */
public class ItemAdapter extends ArrayAdapter<AppInfo>{
    private int resourceId;

    public ItemAdapter(Context context , int textViewResourceId, List<AppInfo> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        AppInfo appInfo = getItem(position);
        View view;
        ItemHolder itemHolder;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            itemHolder = new ItemHolder();
            itemHolder.icon=(ImageView)view.findViewById(R.id.app);
            itemHolder.name=(TextView)view.findViewById(R.id.appName);
            itemHolder.pname=(TextView)view.findViewById(R.id.packageName);
            view.setTag(itemHolder);
        } else{
            view=convertView;
            itemHolder=(ItemHolder)view.getTag();
        }
        itemHolder.name.setText(appInfo.getAppName());
        itemHolder.pname.setText(appInfo.getAppPackageName());
        itemHolder.icon.setImageDrawable(appInfo.getAppIcon());
        return view;
    }

    class ItemHolder{
        TextView name=null;
        TextView pname=null;
        ImageView icon=null;
    }
}
