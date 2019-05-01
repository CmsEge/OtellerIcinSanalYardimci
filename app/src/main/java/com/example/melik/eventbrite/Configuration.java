package com.example.melik.eventbrite;


import android.app.ListActivity;
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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.melik.places.PlaceMain.makeCall;

public class Configuration extends ListActivity {
    ArrayList<Events> eventList;
    private Button event;
    private TextView eventText;
    final String EVENT_KEY = "LCJHM625NWALIR3PZJVG";
    final String latitude = "41.0805174";
    final String longtitude = "29.0082785";
    ArrayAdapter<String> myAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_brite);

        new eventPlaces().execute();

    }

    private class eventPlaces extends AsyncTask<View, Void, String> {

        String temp;

        @Override
        protected String doInBackground(View... urls) {
            // make Call to the url
            temp = makeCall("https://www.eventbriteapi.com/v3/events/search/?location.latitude=" + latitude + "&location.longitude=" + longtitude + "&token=" + EVENT_KEY);

            //print the call in the console
            Log.i("call:","https://www.eventbriteapi.com/v3/events/search/?location.latitude=" + latitude + "&location.longitude=" + longtitude + "&token=" + EVENT_KEY);
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

                List<String> listEvent = new ArrayList<String>();

                for (int i = 0; i < eventList.size(); i++) {
                    // make a list of the venus that are loaded in the list.
                    // show the name, the category and the city
                    listEvent.add(i, eventList.get(i).getName() + "\nStart Now: " + eventList.get(i).getStart() + "\nFinish Now: " + eventList.get(i).getEnd() + "\n(" + eventList.get(i).getDescription() + ")");
                }
                myAdapter = new ArrayAdapter<String>(Configuration.this, R.layout.event_row, R.id.eventListView, listEvent);
                setListAdapter(myAdapter);


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
        Log.i("replystring",replyString);

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
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));


                        if (jsonArray.getJSONObject(i).has("description")){
                            poi.setDescription(jsonArray.getJSONObject(i).optString("text", " "));
                        }

                        if (jsonArray.getJSONObject(i).has("start")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("start").has("local")) {
                                poi.setStart(jsonArray.getJSONObject(i).optString("local",""));
                            }
                        }
                        if (jsonArray.getJSONObject(i).has("end")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("end").has("local")) {
                                poi.setStart(jsonArray.getJSONObject(i).optString("local",""));
                            }
                        }
                    }
                    temp.add(poi);
                    //Log.i("poi",poi.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Events>();
        }
        return temp;

    }


}
