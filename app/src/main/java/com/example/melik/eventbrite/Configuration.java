package com.example.melik.eventbrite;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.EventLog;
import android.util.Log;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melik.myapplication.MainScreen;
import com.example.melik.myapplication.R;
import com.example.melik.places.GooglePlace;
import com.example.melik.places.PlaceMain;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.glassfish.jersey.internal.inject.Custom;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Configuration extends ListActivity implements LocationListener {
    ArrayList<Events> eventList;
    final String EVENT_KEY = "LCJHM625NWALIR3PZJVG";
    public double latitude;
    public double longtitude;
    //ArrayAdapter<String> myAdapter;
    private ListView listView;
    private ImageView image;
    protected LocationManager locationManager;
    //protected LocationListener locationListener;
    //protected Context context;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Location lastLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_brite);
        checkLocationPermission();
        getLocation();
        View inflatedView = getLayoutInflater().inflate(R.layout.event_row, null);
        image = (ImageView) inflatedView.findViewById(R.id.logo);
        new eventPlaces().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getLocation();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(this);
        }
    }

    private class eventPlaces extends AsyncTask<View, Void, String> {

        String temp;
        String url;

        @Override
        protected String doInBackground(View... urls) {

            // make Call to the url
            temp = makeCall("https://www.eventbriteapi.com/v3/events/search/?location.latitude=" + latitude + "&location.longitude=" + longtitude + "&token=" + EVENT_KEY);

            //print the call in the console
            Log.i("call:", "https://www.eventbriteapi.com/v3/events/search/?location.latitude=" + latitude + "&location.longitude=" + longtitude + "&token=" + EVENT_KEY);
            return "";
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }

        @Override
        protected void onPostExecute(String result) {

            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
            } else {

                eventList = (ArrayList<Events>) parseEvent(temp);

                /*final List<String> listEvent = new ArrayList<String>();

                for (int i = 0; i < eventList.size(); i++) {
                    /*imageUrl = eventList.get(i).getImageURL();
                    new DownloadImageTask(image).execute(imageUrl);*/
                   // listEvent.add(i, eventList.get(i).getName() + "\n\nStart Now: " + eventList.get(i).getStartCalendar() + "  " + eventList.get(i).getStart() + "\nFinish Now: " + eventList.get(i).getEndCalendar() + "  " + eventList.get(i).getEnd() + "\n");
                //}
                //myAdapter = new ArrayAdapter<String>(Configuration.this, R.layout.event_row, R.id.eventListView, listEvent);

                //myAdapter = new ArrayAdapter<String>(Configuration.this, android.R.layout.simple_list_item_1, android.R.id.text1, listEvent);
                //setListAdapter(myAdapter);
                listView = getListView();
                ////////////////////////////////////////////
                CustomAdapter adapter = new CustomAdapter(Configuration.this, eventList);/////////////////////////
                listView.setAdapter(adapter);
                //listView.setAdapter(myAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        url = eventList.get(position).getUrl();
                        //url = url.replaceAll("https://www.eventbrite.com/e/", "");
                        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browse);
                        Toast.makeText(getApplicationContext(), "You are redirecting to Eventbrite...", Toast.LENGTH_LONG).show();

                    }
                });

            }
        }
    }

    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        String replyString = "";

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("replystring", replyString);

        // trim the whitespaces
        return replyString.trim();
    }

    private static ArrayList<Events> parseEvent(final String response) {

        ArrayList<Events> temp = new ArrayList<Events>();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("events")) {

                JSONArray jsonArray = jsonObject.getJSONArray("events");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Events poi = new Events();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        if (jsonArray.getJSONObject(i).getJSONObject("name").has("text")) {
                            poi.setName(jsonArray.getJSONObject(i).getJSONObject("name").optString("text"));
                        }

                        if (jsonArray.getJSONObject(i).has("description")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("description").has("text")) {
                                poi.setDescription(jsonArray.getJSONObject(i).getJSONObject("description").optString("text", " "));
                            }
                        }

                        if (jsonArray.getJSONObject(i).has("start")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("start").has("local")) {
                                poi.setStartCalendar(jsonArray.getJSONObject(i).getJSONObject("start").optString("local", "").substring(0, 10));
                                poi.setStart(jsonArray.getJSONObject(i).getJSONObject("start").optString("local", "").substring(11, 16));
                            }
                        }
                        if (jsonArray.getJSONObject(i).has("end")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("end").has("local")) {
                                poi.setEndCalendar(jsonArray.getJSONObject(i).getJSONObject("end").optString("local", "").substring(0, 10));
                                poi.setEnd(jsonArray.getJSONObject(i).getJSONObject("end").optString("local", "").substring(11, 16));
                            }
                        }
                        if (jsonArray.getJSONObject(i).has("url")) {
                            poi.setUrl(jsonArray.getJSONObject(i).optString("url"));
                        }
                        if(jsonArray.getJSONObject(i).has("logo") && !jsonArray.getJSONObject(i).isNull("logo")){
                            if (jsonArray.getJSONObject(i).getJSONObject("logo").has("url")) {
                                poi.setImageURL(jsonArray.getJSONObject(i).getJSONObject("logo").optString("url",""));
                                Log.i("logourl",jsonArray.getJSONObject(i).getJSONObject("logo").optString("url",""));
                            }
                        }
                    }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Events>();
        }
        return temp;

    }


    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longtitude = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Configuration.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            latitude = lastLocation.getLatitude();
            longtitude = lastLocation.getLongitude();

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Configuration.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        getLocation();

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


}
