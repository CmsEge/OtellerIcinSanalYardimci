package com.example.melik.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.melik.config.LanguageConfig;
import com.example.melik.database.Database;
import com.example.melik.service.Service;
import com.example.melik.places.GooglePlace;
import com.example.melik.places.PlaceMain;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.ChatView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.GsonFactory;
import ai.api.model.AIContext;
import ai.api.model.AIError;
import ai.api.model.AIEvent;
import ai.api.model.AIOutputContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getName();
    private Gson gson = GsonFactory.getGson();
    private AIDataService aiDataService;
    private ChatView chatView;
    private User myAccount;
    private User droidKaigiBot;
    private Service service;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new Database(getApplicationContext());
        service = new Service(database); //her işimizi bu servis arkadaşına yaptırıcaz tüm metotları
        //service.InsertTables();//syncdata fonksiyonunda sqllite çalıştırıyoruz bu çalıştırma için context'e ihtiyaç duyuyor o yüzden parametre olarak gönderiyoruz.
        Log.i("deneme", service.listAll("Customer").toString());
        Log.i("alacarte", database.allAlacarteNames().toString());
        Log.i("alacarte", service.listAll("Alacarte").toString());
        Log.i("reservations", service.listAll("ReservationAla").toString());
        Log.i("events", service.listAll("Event").toString());
        Log.i("notifications", service.listAll("EventNotification").toString());
        Log.i("orders: ", service.listAll("OrderTable").toString());
        Log.i("orderReq", service.listAll("OrderRequest").toString());
        Log.i("meals", service.listAll("Meals").toString());
        Log.i("roomStatus", service.listAll("RoomStatus").toString());
        initChatView();
        //Language, Dialogflow Client access token
        final LanguageConfig config = new LanguageConfig("en", "ecd717ee86524b2e977ca6e4483c7346");
        initService(config);
        Receive("Hello "+ myAccount.getName());
        NotificationHandle();
    }

    @Override
    public void onClick(View v) {

        //new message
        final Message message = new Message.Builder()
                .setUser(myAccount)
                .setRightMessage(true)
                .setMessageText(chatView.getInputText())
                .build();
        //Set to chat view
        chatView.send(message);
        sendRequest(chatView.getInputText());
        //Reset edit text
        chatView.setInputText("");
    }

    public void Notification(String getType, String type, String msg) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        String[] tokens = getType.split(":");
        int hour = Integer.parseInt(tokens[0]);
        Intent intent = new Intent(this, AlarmReceiver.class).putExtra("msg", msg);
        intent.setAction(type);
        Calendar cal = Calendar.getInstance();
        cal.set(cal.HOUR_OF_DAY, hour - 1);
        cal.set(cal.MINUTE, Integer.parseInt(tokens[1]));
        PendingIntent broadcast = PendingIntent.getBroadcast(this, (int) cal.getTimeInMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(alarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, broadcast);

    }

    public void NotificationHandle() {
        HashMap<String, String> list = service.getStartDateOfMeal();
        HashMap<String, String> list2 = service.getStartDateOfEvents();
        if(list.size()>0){
            Notification(list.get("Breakfast"), "Breakfast", "Breakfast starts at " + list.get("Breakfast") + ".Don't be late, we will be waiting for you :)");
            Notification(list.get("Lunch"), "Lunch", "Lunch starts at " + list.get("Lunch") + ".Don't be late, we will be waiting for you :)");
            Notification(list.get("Dinner"), "Dinner", "Dinner starts at " + list.get("Dinner") + ".Don't be late, we will be waiting for you :)");
        }
        if(list2.size()>0){
            for (Map.Entry<String, String> pair : list2.entrySet()) {
                Notification(pair.getValue(), "Event", pair.getKey() + " starts at " + pair.getValue() + ". I wish you will be there..");
            }
        }

        //Burada eventNotification table dan şuan chatbotla konuşan kişinin eventlere kaydı varsa o kayıtlar için notification atılmalı . myacoount.id ile
        //eventNotification tablosundan kişinin kayıtlandığı eventleri çekebilirisin
        //tabloyu gezerken if else ile kişinin eventi var mı , varsa noti attır yoksa atmasın bu kontrole dikkat et.

        //Aynı şekilde reservationAla tablosundan da konuşan kişinin yemek saatinden önce bildirim atılmalı.
    }

    private void sendRequest(String text) {
        Log.d(TAG, text);
        final String queryString = String.valueOf(text);
        final String eventString = null;
        final String contextString = null;

        if (TextUtils.isEmpty(queryString) && TextUtils.isEmpty(eventString)) {
            onError(new AIError(getString(R.string.non_empty_query)));
            return;
        }

        new AiTask().execute(queryString, eventString, contextString);
    }
    public class AiTask extends AsyncTask<String, Void, AIResponse> {
        private AIError aiError;

        @Override
        protected AIResponse doInBackground(final String... params) {
            final AIRequest request = new AIRequest();
            String query = params[0];
            String event = params[1];
            String context = params[2];

            if (!TextUtils.isEmpty(query)) {
                request.setQuery(query);
            }

            if (!TextUtils.isEmpty(event)) {
                request.setEvent(new AIEvent(event));
            }

            RequestExtras requestExtras = null;
            if (!TextUtils.isEmpty(context)) {
                final List<AIContext> contexts = Collections.singletonList(new AIContext(context));
                requestExtras = new RequestExtras(contexts, null);
            }

            try {
                return aiDataService.request(request, requestExtras);
            } catch (final AIServiceException e) {
                aiError = new AIError(e);
                return null;
            }
        }
        @Override
        protected void onPostExecute(final AIResponse response) {
            if (response != null) {
                onResult(response);
            } else {
                onError(aiError);
            }
        }
    }
    private void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gson.toJson(response);
                final HashMap<String, JsonElement> params = response.getResult().getParameters();
                if (params != null && !params.isEmpty()) {
                    Log.i("Action: ", response.getResult().getAction().toString());
                    Log.i(TAG, "Parameters: ");
                    for (final Map.Entry<String, JsonElement> entry : params.entrySet()) {
                        Log.i(TAG, String.format("%s: %s",
                                entry.getKey(), entry.getValue().toString()));
                    }
                }
                String speech = response.getResult().getFulfillment().getSpeech();
                String action = response.getResult().getAction().toString();
                switch (action) {
                    case "dinner-reservation": {
                        speech = service.DinnerReservation(speech);
                        Receive(speech);
                        break;
                    }
                    case "Dinner-Reservation.Dinner-Reservation-custom": {
                        //final AIOutputContext outputContext=response.getResult().getContext("projects/cmsbot-48dcf/agent/sessions/0176a748-a5bd-d3e9-16ac-34a081556910/contexts/dinner-reservation");
                        AIOutputContext outputContext = response.getResult().getContext("dinner-reservation");
                        Map<String, JsonElement> list = outputContext.getParameters();
                        service.setReservationInfo(Integer.parseInt(myAccount.getId()), list.get("Restaurant-Type").getAsString(), list.get("date").getAsString());
                        Receive(speech);
                        break;
                    }
                    case "Hotel-Activity": {
                        speech = service.EventInfo(speech);
                        Receive(speech);
                        break;
                    }
                    case "hotel-activity-notification": {
                        service.insertEventNotification(Integer.parseInt(response.getResult().getResolvedQuery()), Integer.parseInt(myAccount.getId()));
                        Receive(speech);
                        break;
                    }
                    case "order": {
                        speech = service.OrderInfo(speech);
                        Receive(speech);
                        break;
                    }
                    case "order-response": {
                        service.insertOrderRequest(Integer.parseInt(myAccount.getId()), Integer.parseInt(response.getResult().getResolvedQuery()));
                        //service.insertEventNotification(Integer.parseInt(response.getResult().getResolvedQuery()), Integer.parseInt(myAccount.getId()));
                        Receive(speech);
                        break;
                    }
                    case "All-meals": {
                        speech = service.MealInfo(speech);
                        Receive(speech);
                        break;
                    }
                    case "breakfast-time": {
                        speech = service.mainMealsInfo("Breakfast", speech);
                        Receive(speech);
                        break;
                    }
                    case "Lunch-Time": {
                        speech = service.mainMealsInfo("Lunch", speech);
                        Receive(speech);
                        break;
                    }
                    case "dinner-time": {
                        speech = service.mainMealsInfo("Dinner", speech);
                        Receive(speech);
                        break;
                    }
                    case "Do-not-disturb": {
                        service.insertRoomStatus(Integer.parseInt(myAccount.getId()), 1, 0, "");//disturb==1 rahatsız etmeyin demek
                        Receive(speech);
                        break;
                    }
                    case "Cleaning": {
                        service.insertRoomStatus(Integer.parseInt(myAccount.getId()), 0, 1, "");
                        Receive(speech);
                        break;
                    }
                    case "set-alarm": {

                        service.insertRoomStatus(Integer.parseInt(myAccount.getId()), 0, 0, params.get("time").getAsString());
                        Log.i("timeeeeeee ,", params.get("time").getAsString());
                        Receive(speech);
                        break;
                    }
                    case "event": {
                        Intent intent = new Intent(MainActivity.this, PlaceMain.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        Receive(speech);
                        break;
                    }
                    case "faq": {
                        speech = service.FaqInfo(speech);
                        Receive(speech);
                        break;
                    }
                    case "faq-choice": {
                        speech = service.FaqAnswer(response.getResult().getResolvedQuery());
                        Receive(speech);
                        break;
                    }
                    default:
                        Receive(speech);
                        break;
                }
            }
        });
    }

    private void Receive(String speech) {
        final Message receivedMessage = new Message.Builder()
                .setUser(droidKaigiBot)
                .setRightMessage(false)
                .setMessageText(speech)
                .build();
        chatView.receive(receivedMessage);
    }

    private void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, error.toString());
            }
        });
    }

    private void initChatView() {
        ArrayList<String> customer = service.getCustomerbyStatus();

        int myId = 0;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.robot_icon);
        Bitmap userIcon = BitmapFactory.decodeResource(getResources(), R.drawable.user_icon);
        String myName = "Siz";
        myAccount = new User(Integer.parseInt(customer.get(0)), customer.get(1), userIcon);

        int botId = 2;
        String botName = "Shire";
        droidKaigiBot = new User(botId, botName, icon);

        chatView = findViewById(R.id.chat_view);
        chatView.setRightBubbleColor(Color.parseColor("#459FB2"));
        chatView.setLeftBubbleColor(Color.parseColor("#B25D7F"));
        chatView.setBackgroundColor(Color.parseColor("#D4D4D4"));
        chatView.setSendButtonColor(Color.parseColor("#021aee"));
        //chatView.setSendIcon(R.drawable.user_icon);
        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.WHITE);
        chatView.setUsernameTextColor(Color.WHITE);
        chatView.setSendTimeTextColor(Color.WHITE);
        chatView.setDateSeparatorColor(Color.WHITE);
        chatView.setInputTextHint("new message...");
        chatView.setMessageMarginTop(5);
        chatView.setMessageMarginBottom(5);
        chatView.setMessageFontSize(40);
        chatView.setOnClickSendButtonListener(this);
    }

    private void initService(final LanguageConfig languageConfig) {
        final AIConfiguration.SupportedLanguages lang =
                AIConfiguration.SupportedLanguages.fromLanguageTag(languageConfig.getLanguageCode());
        final AIConfiguration config = new AIConfiguration(languageConfig.getAccessToken(),
                lang,
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(this, config);
    }
}





