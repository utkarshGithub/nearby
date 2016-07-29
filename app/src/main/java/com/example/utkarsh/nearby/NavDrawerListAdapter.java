package com.example.utkarsh.nearby;

/**
 * Created by utkarsh on 23/7/16.
 */

import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null ) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);

            holder = new ViewHolder();
            holder.iview = (ImageView) convertView.findViewById(R.id.icon);
            holder.txtview = (TextView) convertView.findViewById(R.id.title);
            holder.chbx = (CheckBox) convertView.findViewById(R.id.checkbox);


            convertView.setTag(holder);


            holder.chbx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    NavDrawerItem mnavDrawerItem = (NavDrawerItem) v.getTag();
                    Toast.makeText(context,
                            "Clicked on Checkbox: " + cb.getId() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();
                  //  mnavDrawerItem.setcheckboxselectivity(cb.isChecked());
                    navDrawerItems.get(position).setcheckboxselectivity(cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NavDrawerItem mnavDrawerItem = navDrawerItems.get(position);
        holder.txtview.setText(navDrawerItems.get(position).getTitle());
        holder.iview.setImageResource(navDrawerItems.get(position).getIcon());
        holder.chbx.setChecked(mnavDrawerItem.getifcheckboxselected());
        holder.txtview.setTag(mnavDrawerItem);
        // displaying count
        // check whether it set visible or not


        return convertView;
    }

    private class ViewHolder {
        TextView txtview;
        CheckBox chbx;
        ImageView iview;
    }

}