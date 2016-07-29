package com.example.utkarsh.nearby;

/**
 * Created by utkarsh on 12/7/16.
 */

import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import java.util.List;



/**
 * Created by utkarsh on 10/7/16.
 */

import java.util.List;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {

    private List<Place> placeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, address, prob;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            address = (TextView) view.findViewById(R.id.Address);
            prob = (TextView) view.findViewById(R.id.prob);
        }
    }


    public PlacesAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.name.setText(place.getname());
        holder.address.setText(place.getAddress());
        holder.prob.setText(place.getprobability().toString());
    }

    @Override
    public int getItemCount() {
        if(placeList!=null)
        {return placeList.size();}
        else
        {
            return 0;
        }
    }
}
