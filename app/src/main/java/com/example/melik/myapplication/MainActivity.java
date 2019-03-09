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
import ai.api.android.AIService;
import ai.api.model.AIContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import ai.api.*;
import ai.api.ui.AIDialog;

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



    private int name=0;
    private String surName="";
    private String errmsg="";
    public void run() {
        System.out.println("Select Records Example by using the Prepared Statement!");
        Connection con = null;
        int count = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection
                    ("jdbc:mysql://192.168.1.26:3306/Cms", "root", "14.Cms.14");
           try{
                String sql;
                //	  sql
                //	  = "SELECT title,year_made FROM movies WHERE year_made >= ? AND year_made <= ?";
                sql
                        = "SELECT name,surName FROM Customer";
                PreparedStatement prest = con.prepareStatement(sql);
                //prest.setInt(1,1980);
                //prest.setInt(2,2004);
                ResultSet rs = prest.executeQuery();
                while (rs.next()){
                    name = rs.getInt(1);
                    surName = rs.getString(2);
                    count++;
                    System.out.println(name + "\t" + "- " + surName);
                }
                System.out.println("Number of records: " + count);
                prest.close();
                con.close();
            }
            catch (SQLException s){
                System.out.println("SQL statement is not executed!");
                errmsg=errmsg+s.getMessage();

            }
        }
        catch (Exception e){
            e.printStackTrace();
           // errmsg=errmsg+e.getMessage();
        }

            //handler.sendEmptyMessage(0);

    }






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
                            if (!aiResponse.toString().isEmpty()) {
                                if (aiResponse.getResult().getAction().equals("dinner-time")) {
                                    /*deneme = aiResponse.getResult();*/


                                    Log.i("Bilgi",aiResponse.getResult().getContexts().toString());
                                    /*String speech;
                                    aiResponse.getResult().getFulfillment().setDisplayText("5");
                                    speech = aiResponse.getResult().getFulfillment().getSpeech();
                                    Log.i("bilgi",speech);*/


                                    //resultTextView.append(aiResponse.getResult().getStringParameter("DinnerTime"));
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
            }
        });



    }

}




