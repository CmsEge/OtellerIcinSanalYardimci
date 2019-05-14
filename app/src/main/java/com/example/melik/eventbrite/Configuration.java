package com.example.melik.eventbrite;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.EventLog;
import android.util.Log;


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
import android.widget.TextView;
import android.widget.Toast;

import com.example.melik.myapplication.R;
import com.example.melik.places.GooglePlace;
import com.example.melik.places.PlaceMain;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.melik.myapplication.R.drawable.ic_launcher_background;
import static com.example.melik.places.PlaceMain.makeCall;

public class Configuration extends ListActivity {
    ArrayList<Events> eventList;
    final String EVENT_KEY = "LCJHM625NWALIR3PZJVG";
    final String latitude = "41.0805174";
    final String longtitude = "29.0082785";
    ArrayAdapter<String> myAdapter;
    private ListView listView;
    private ImageView image;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_brite);
        View inflatedView = getLayoutInflater().inflate(R.layout.event_row, null);
        //image = (ImageView) inflatedView.findViewById(R.id.logo);
        new eventPlaces().execute();

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

                final List<String> listEvent = new ArrayList<String>();

                for (int i = 0; i < eventList.size(); i++) {
                    /*url = eventList.get(i).getUrlLogo();
                    ImageDownloader imageDownloader = new ImageDownloader();
                    imageDownloader.download(url,image);*/
                    /*new DownloadImageTask(image)
                            .execute(url);*/
                    listEvent.add(i, eventList.get(i).getName() + "\n\nStart Now: " + eventList.get(i).getStartCalendar() + "  " + eventList.get(i).getStart() + "\nFinish Now: " + eventList.get(i).getEndCalendar() + "  " + eventList.get(i).getEnd() + "\n");
                }
                myAdapter = new ArrayAdapter<String>(Configuration.this, R.layout.event_row, R.id.eventListView, listEvent);

                //myAdapter = new ArrayAdapter<String>(Configuration.this, android.R.layout.simple_list_item_1, android.R.id.text1, listEvent);
                //setListAdapter(myAdapter);

                listView = getListView();
                listView.setAdapter(myAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        //url = eventList.get(position).getUrl();
                        //url = url.replaceAll("https://www.eventbrite.com/e/", "");
                        //Toast.makeText(getApplicationContext(), url,Toast.LENGTH_LONG).show();
                        /*Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( url ) );

                        startActivity( browse );*/
                        Toast.makeText(getApplicationContext(), eventList.get(position).getDescription(), Toast.LENGTH_LONG).show();

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
                        /*if (jsonArray.getJSONObject(i).has("url")) {
                                poi.setUrl(jsonArray.getJSONObject(i).getJSONObject("url").toString());
                        }*/
                        /*if(jsonArray.getJSONObject(i).has("logo")){
                            if (jsonArray.getJSONObject(i).getJSONObject("logo").has("url")) {
                                poi.setUrlLogo(jsonArray.getJSONObject(i).getJSONObject("logo").optString("url"));
                            }
                        }*/
                    }
                    temp.add(poi);
                    //Log.i("poi",poi.getUrlLogo());
                    /*Log.i("poi",poi.getName());
                    Log.i("poi",poi.getStart());
                    Log.i("poi",poi.getEnd());
                    Log.i("poi",poi.getDescription());*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Events>();
        }
        return temp;

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
