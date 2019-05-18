package com.example.melik.places;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.melik.eventbrite.Events;
import com.example.melik.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterPlace extends BaseAdapter {
    private LayoutInflater userInflater;
    private ArrayList<GooglePlace> placeList;

    public CustomAdapterPlace(Activity activity, ArrayList<GooglePlace> placeList){
        userInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.placeList = placeList;
    }
    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View lineView;
        lineView = userInflater.inflate(R.layout.places_row, null);
        TextView eventListView = (TextView) lineView.findViewById(R.id.listText);
        ImageView logo = (ImageView) lineView.findViewById(R.id.placeLogo);

        GooglePlace gPlace = placeList.get(position);
        eventListView.setText(gPlace.getName());
        Picasso.with(logo.getContext()).load(gPlace.getImageUrl()).into(logo);

        return lineView;
    }
}
