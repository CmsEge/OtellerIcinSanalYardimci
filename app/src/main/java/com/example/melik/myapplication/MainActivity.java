package com.example.melik.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    //implements AIListener yazıyordu extends yanında
    private Button listenButton;
    private TextView resultTextView;
    private EditText queryText;
    private String data;
    private String deneme;

    private static final String url="jdbc:mysql://192.168.1.26:3306/Cms";
    private static final String user="root";
    private static String pass="14.Cms.14";


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenButton = (Button) findViewById(R.id.listenButton);
        queryText = (EditText) findViewById(R.id.queryText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        final AIConfiguration config = new AIConfiguration("65ebee5b7327440e8f265d320ad76e93",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        listenButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (!queryText.getText().toString().isEmpty()) {

                    final AIDataService aiDataService = new AIDataService(config);
                    data = queryText.getText().toString();
                    resultTextView.append("You: " + data + "\n");
                    final AIRequest aiRequest = new AIRequest();
                    aiRequest.setQuery(data);

                    new AsyncTask<AIRequest, Void, AIResponse>() {

                        @SuppressLint("WrongThread")
                        @Override
                        protected AIResponse doInBackground(AIRequest... requests) {
                            final AIRequest request = requests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(AIResponse aiResponse) {
                            if (aiResponse != null) {
                                if (aiResponse.getResult().getAction().equals("dinner-time")) {
                                    /*deneme = aiResponse.getResult();*/

                                    resultTextView.append(aiResponse.getResult().getStringParameter("DinnerTime"));
                                }
                                Result result = aiResponse.getResult();
                                String parameterString = result.getFulfillment().getSpeech();

                                resultTextView.append("Chatbot: " + parameterString + "\n");
                            }
                        }
                    }.execute(aiRequest);
                }

                //resultTextView.append(deneme);
                queryText.setText("");



                new MyTask().execute();

            }
        });



    }
    private class MyTask extends AsyncTask<Void,Void,Void>{
    private String surName="";
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con= DriverManager.getConnection(url,user,pass);
                Statement st=con.createStatement();
                String sql="select * from Customer";
                final ResultSet rs=st.executeQuery(sql);
                rs.next();
                surName=rs.getString(2);
            }catch (Exception e){e.printStackTrace();}
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            queryText.setText(surName);
            super.onPostExecute(result);
        }
    }
}




