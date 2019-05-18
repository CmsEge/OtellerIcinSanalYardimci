package com.example.melik.places;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.melik.eventbrite.Configuration;
import com.example.melik.eventbrite.CustomAdapter;
import com.example.melik.myapplication.R;


public class PlaceMain extends ListActivity implements LocationListener {

    ArrayList<GooglePlace> venuesList;
    private ImageView image;
    private ListView listView;
    final String GOOGLE_KEY = "AIzaSyD1ql2SN_IDO2QVEekNMY-Zj2jai_3Gcrc";
    //final String latitude = "41.0805174";
    //final String longtitude = "29.0082785";
    public double latitude;
    public double longtitude;
    protected LocationManager locationManager;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Location lastLocation;


    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_main);
        checkLocationPermission();
        getLocation();
        // start the AsyncTask that makes the call for the venus search.
        View inflatedView = getLayoutInflater().inflate(R.layout.event_row, null);
        image = (ImageView) inflatedView.findViewById(R.id.logo);
        new googleplaces().execute();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    private class googleplaces extends AsyncTask<View, Void, String> {

        String temp;
        String url;

        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            temp = makeCall("https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longtitude + "&radius=100&sensor=true&key=" + GOOGLE_KEY);

            //print the call in the console
            Log.i("call:", "https://maps.googleapis.com/maps/api/place/search/json?location=" + latitude + "," + longtitude + "&radius=100&sensor=true&key=" + GOOGLE_KEY);
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
                // all things went right

                // parse Google places search result
                venuesList = (ArrayList<GooglePlace>) parseGoogleParse(temp);

                /*List<String> listTitle = new ArrayList<String>();

                for (int i = 0; i < venuesList.size(); i++) {
                    // make a list of the venus that are loaded in the list.
                    // show the name, the category and the city
                    listTitle.add(i, venuesList.get(i).getName() + "\nOpen Now: " + venuesList.get(i).getOpenNow() + "\n(" + venuesList.get(i).getCategory() + ")");
                }

                // set the results to the list
                // and show them in the xml

                myAdapter = new ArrayAdapter<String>(PlaceMain.this, R.layout.places_row, R.id.listText, listTitle);*/
                listView = getListView();
                CustomAdapterPlace adapter = new CustomAdapterPlace(PlaceMain.this, venuesList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        url = venuesList.get(position).getUrl();
                        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
                        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browse);
                        Toast.makeText(getApplicationContext(), "You are redirecting to Google Maps...", Toast.LENGTH_LONG).show();

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

    private static ArrayList<GooglePlace> parseGoogleParse(final String response) {

        ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
        String urlParse;
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    GooglePlace poi = new GooglePlace();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setUrl("https://www.google.com/maps/search/?api=1&query=" + jsonArray.getJSONObject(i).optString("name").replaceAll(" ", "+"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));

                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpenNow("YES");
                                } else {
                                    poi.setOpenNow("NO");
                                }
                            }
                        } else {
                            poi.setOpenNow("Not Known");
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");

                            for (int j = 0; j < typesArray.length(); j++) {
                                poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                            }
                        }
                        if (jsonArray.getJSONObject(i).has("icon") && !jsonArray.getJSONObject(i).isNull("icon")) {
                            poi.setImageUrl(jsonArray.getJSONObject(i).optString("icon"));
                            Log.i("logourl", jsonArray.getJSONObject(i).optString("icon"));

                        }

                    }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<GooglePlace>();
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
        Toast.makeText(PlaceMain.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
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
                                ActivityCompat.requestPermissions(PlaceMain.this,
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
