package com.example.melik.eventbrite;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.melik.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater userInflater;
    private ArrayList<Events> eventList;

    public CustomAdapter(Activity activity, ArrayList<Events> eventList){
        userInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.eventList = eventList;
    }
    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View lineView;
        lineView = userInflater.inflate(R.layout.event_row, null);
        TextView eventListView = (TextView) lineView.findViewById(R.id.eventListView);
        ImageView logo = (ImageView) lineView.findViewById(R.id.logo);

        Events event = eventList.get(position);
        eventListView.setText(event.getName());
        Picasso.with(logo.getContext()).load(event.getImageURL()).into(logo);

        return lineView;
    }
}
