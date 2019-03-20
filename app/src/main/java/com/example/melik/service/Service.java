package com.example.melik.service;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melik.database.CustomerDB;

import java.util.ArrayList;
import java.util.HashMap;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class Service {
    private  final AIConfiguration config = new AIConfiguration("ecd717ee86524b2e977ca6e4483c7346",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);
    private Boolean success;

    private Button listenButton;
    private TextView resultTextView ;
    private EditText queryText;
    ArrayList<HashMap<String, String>> customerList;
    String customerNames[];
    int customerIds[];

    public Service(Button listenButton,EditText queryText, TextView resultTextView){
        this.setListenButton(listenButton);
        this.setResultTextView(resultTextView);
        this.setQueryText(queryText);

    }
    public void setListenButton(Button listenButton) { this.listenButton = listenButton; }
    public void setQueryText(EditText queryText) { this.queryText = queryText; }
    public void setResultTextView(TextView resultTextView){ this.resultTextView=resultTextView; }


    @SuppressLint("StaticFieldLeak")
    public void StartChat(){
        String data;
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
                                String speech;
                                speech = aiResponse.getResult().getFulfillment().getSpeech();
                                speech = speech.replace("dinnerTimeStart","5");
                                speech = speech.replace("dinnerTimeFinish","10");
                                aiResponse.getResult().getFulfillment().setSpeech(speech);
                                Log.i("Bilgi",aiResponse.getResult().getFulfillment().getSpeech());
                        }

                        Result result = aiResponse.getResult();
                        String parameterString = result.getFulfillment().getSpeech();
                        resultTextView.append("Chatbot: " + parameterString + "\n");
                    }
                }
            }.execute(aiRequest);
        }
        queryText.setText("");
    }


    public void InsertTables(CustomerDB db) {


        db.customerInsert("17614534630", "Saygun", "Askin", "107", "0506 870 74 03");
        db.customerInsert("11223344556","Melikenur","Gülas","108","0512 345 67 89");
        db.customerInsert("11223344567","Ceren Yaren","Erer","109","0512 345 67 90");

    }

}
