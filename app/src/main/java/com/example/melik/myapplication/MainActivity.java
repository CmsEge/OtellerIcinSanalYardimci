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

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    //implements AIListener yazıyordu extends yanında
    private Button listenButton;
    private TextView resultTextView;
    private EditText queryText;
    private String data;
    private String deneme;

    private static final String url="jdbc:mysql://localhost:3306/Cms";
    private static final String user="root";
    private static String pass="Saskin*9";


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenButton = (Button) findViewById(R.id.listenButton);
        queryText = (EditText) findViewById(R.id.queryText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        final AIConfiguration config = new AIConfiguration("ecd717ee86524b2e977ca6e4483c7346",
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

                            if (!aiResponse.getResult().getAction().isEmpty()) {
                                if (aiResponse.getResult().getAction().equals("dinner-time")) {

                                    if(aiResponse.getResult().getAction().equals("dinner-time")){
                                        String speech;
                                        speech = aiResponse.getResult().getFulfillment().getSpeech();
                                        speech = speech.replace("dinnerTimeStart","5");
                                        speech = speech.replace("dinnerTimeFinish","10");
                                        aiResponse.getResult().getFulfillment().setSpeech(speech);
                                        Log.i("Bilgi",aiResponse.getResult().getFulfillment().getSpeech());
                                    }

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



                //new MyTask().execute();

            }
        });



    }

}




