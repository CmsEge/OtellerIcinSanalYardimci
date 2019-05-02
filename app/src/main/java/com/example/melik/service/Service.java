package com.example.melik.service;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.melik.database.Database;
import com.example.melik.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import android.support.v4.content.ContextCompat;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import android.support.v4.app.NotificationManagerCompat;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.example.melik.myapplication.AlarmReceiver;



public class Service {

    private Database db;
    public Service(Database db){
        this.db=db;
    }
    public void InsertTables() {

        db.customerInsert("Saygun ","Aşkın" , "107", "0506 870 74 03","1234","saygunaskin@gmail.com");
        db.customerInsert("Melikenur" ,"Gülas","108","0512 345 67 89","1234","melikegulas@gmail.com");
        db.customerInsert("Ceren Yaren" ,"Erer","109","0512 345 67 90","1234","c.yaren@gmail.com");

        ArrayList<String> entree=new ArrayList<String>();
        entree.add("dsfsd");
        entree.add("dfsd");
        ArrayList<String> warm=new ArrayList<String>();
        warm.add("dsfsd");
        warm.add("dfsd");
        ArrayList<String> main=new ArrayList<String>();
        main.add("dsfsd");
        main.add("dfsd");
        ArrayList<String> dessert=new ArrayList<String>();
        dessert.add("dsfsd");
        dessert.add("dfsd");
        db.alacarteInsert("Italian Restaurant",entree,warm,main,dessert);
        db.alacarteInsert("Far East Restaurant",entree,warm,main,dessert);
        db.alacarteInsert("Ottoman Restaurant",entree,warm,main,dessert);

        db.eventInsert("Beach Volleyball","11:00","12:00","Beach");
        db.eventInsert("Water Polo","15:00","16:00","Pool");
        db.eventInsert("Animation Show","20:00","21:00","Theatre");
        db.eventInsert("Concert","23:00","00:00","Concert Hall");
        db.eventInsert("Aerobic","09:00","10:00","Near The Pool");

        db.orderInsert("Water","2TL");
        db.orderInsert("Beer","20TL");
        db.mealInsert("Breakfast","07:00","10:00");
        db.mealInsert("Brunch","10:00","11:00");
        db.mealInsert("Patisserie","11:00","22:00");
        db.mealInsert("Waffle","12:30","16:00");
        db.mealInsert("Snack Menu","12:30","16:00");
        db.mealInsert("Lunch","12:30","14:30");
        db.mealInsert("Icecream","12:30","18:00");
        db.mealInsert("Dinner","18:30","21:00");
        db.mealInsert("A La Carte Restaurants","19:00","22:30");
        db.mealInsert("Night Buffet","00:00","03:00");


        db.faqInsert("Are pets accepted?","Yes, we do accept pets. They are our best friends!");
        db.faqInsert("Do you have WIFI at the hotel?","Free wireless internet is available throughout the hotel and in all rooms.");
        db.faqInsert("What are the check-in and check-out times?","Check-in time is 14:00 and check-out time is 12:00.");
        db.faqInsert("Is smoking free in the rooms?","Our hotel has both non-smoking and smoking free rooms. Please advise your room preference when booking.");
        db.faqInsert("How can i come from the airport to the hotel?","You can use our free transfer service from the airport to our hotel.");
        db.faqInsert("Do I have to pay if I cancel the reservation? ","You can cancel your reservation until 12:00 before a day, otherwise you have to pay the first day.");
        db.faqInsert("Do I need to have my ID at check-in?","Yes. You must have an identity card, passport or driver's license at check-in.");
        db.faqInsert("What are the must see places around Alanya?","Side (historical town) in 65 km, Perge (Roman historical heritage site) in 85 km, Aspendos (Roman amphitheatre) in 70 km, Manavgat Waterfall in 60 km.");
        db.faqInsert("What is the distance between sea and the hotel?","Our hotel is located on the seafront.");
        db.faqInsert("Can non-refundable or prepaid reservations be canceled?","No prepaid or non-refundable reservations may not be canceled or changed. \n" +
                "In the event of a cancellation, change or the failure to use a reservation, \n" +
                "the total fee or a designated part of it will not be refunded.");
        db.faqInsert("Do the hotel accommodation charges have to be calculated in Turkish Lira?","No, our hotel accepts currencies such as Euro, USD, GBP, AUD and CHF.");
        db.faqInsert("Is there a discount on cash payments?","Yes, there is a 5% discount.");
        db.faqInsert("Which credit cards are honored at the hotel?","We accept Visa, Mastercard and American Express credit cards. You may handle your Visa and MasterCard credit card transactions in Euros or US dollars. \n" +
                "Our hotel applies the daily foreign exchange rates announced by the Turkish Central Bank.");
        db.faqInsert("Does the hotel have parking facilities?","Yes, you can park your car on the street in front of the hotel, free of charge.");
        db.faqInsert("Is the free airport service available only on check-in dates at the hotel?","No, you can use the free airport transfer service on either your arrival day at the hotel or on your day of departure.");
        db.faqInsert("Should I bring a hairdryer with me?","No, all of our rooms are equipped with hairdryers.");
    }


    public List<String> getAllAlacarteNames(){
        return db.allAlacarteNames();
    }
    public ArrayList<HashMap<String, String>> allCustomer(){
        return db.allCustomers();
    }
    public void  reservationAlaInsert(String date, int cus, int ala) {
        db.reservationAlaInsert(date,cus,ala);
    }
    public ArrayList<HashMap<String, String>> listAll(String tableName){
        return db.listAll(tableName);
    }
    public String DinnerReservation(String speech){
        List<String> list=new ArrayList<String>();
        list=this.getAllAlacarteNames();
        String a="";
        if(list.size()>0){
            for(String i: list){
                a+="\n"+i;
            }
        }else{
            Log.i("EventInfo","Alacarte Tablosu boş");
        }
        speech=speech.replace("$RestaurantTypes",a);
        return speech;
    }
    public void setReservationInfo(int cusID,String AlaName,String date){
        List<String> list=new ArrayList<String>();
        list=this.getAllAlacarteNames();
        int alaID=0;
        int a=1;
        if(list.size()>0){
            for(String i: list){
                if(i.equals(AlaName)){
                    alaID=a;
                    this.reservationAlaInsert(date,cusID,alaID);
                    break;
                }else{a++;}
            }
        }else{
            Log.i("EventInfo","Alacarte Tablosu boş");
        }
    }

    public String EventInfo(String speech)
    {
        ArrayList<HashMap<String, String>> list=db.listAll("Event");
        String s="";
        if(list.size()>0){
            for(HashMap<String,String> i: list){
                s+="\n"+ i.get("eventId")+"."+i.get("eventName")+" starts at "+i.get("startTime")+" and ends at "+i.get("endTime")+" at "+i.get("eventPlace");
            }
            s+="\n";
        }
        else{
            Log.i("EventInfo","Event Tablosu boş");

        }
        speech=speech.replace("$HotelActivities",s);
        return speech;
    }
    public String FaqInfo(String speech)
    {
        ArrayList<HashMap<String, String>> list=db.listAll("FAQTable");
        String s="";
        for(HashMap<String,String> i: list){
            s+="\n"+i.get("faqId")+"."+i.get("question")+"\n";
        }
        //s+="\n";
        speech=speech.replace("$faq",s);
        return speech;
    }

    public String FaqAnswer(String speech){
        String s = db.listAnswer(Integer.parseInt(speech));
        speech = speech.replace("$answer",s);
        return s;
    }

    public String MealInfo(String speech){
        ArrayList<HashMap<String, String>> list=db.listAll("Meals");
        String s="";
        for(HashMap<String,String> i: list){
            s+="\n"+i.get("mealName")+" starts at "+i.get("mealStartTime")+" and ends at "+i.get("mealEndTime");
        }
        s+="\n";
        speech=speech.replace("$mealTable",s);
        return speech;
    }

    public String mainMealsInfo(String mealName, String speech){
        List<String> list=db.listMealTimes(mealName);
        speech=speech.replace("$StartTime",list.get(0));
        speech=speech.replace("$EndTime",list.get(1));

        return speech;

    }

    public void insertEventNotification(int eveId, int custId){
        db.eventNotificationInsert(eveId,custId);
    }
    public String OrderInfo(String speech){
        ArrayList<HashMap<String, String>> list=db.listAll("OrderTable");
        String s="";
        if(list.size()>0){
            for(HashMap<String,String> i: list){
                s+="\n"+ i.get("orderId")+"."+i.get("orderName")+" costs "+i.get("cost");
            }
            s+="\n";
        }else{
            Log.i("EventInfo","Order Tablosu boş");
        }
        speech=speech.replace("$orderList",s);  //buraya bak
        return speech;
    }
    public void insertOrderRequest(int custId,int orderId){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date=dateFormat.format(cal.getTime());
        String time=dateFormat2.format(cal.getTime());
        db.orderRequestInsert(custId,orderId,date,time);

    }
    public void insertRoomStatus(int custID, int disturb, int clean, String alarm){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date=dateFormat.format(cal.getTime());
        String time=dateFormat2.format(cal.getTime());
        db.roomStatusInsert(custID,date,time,disturb,clean,alarm);

    }
    public int getCustomerID(String name,String surname,String roomNo,String phoneNo,String cusPassword, String email){
        return db.getCustomerID(name,surname,roomNo,phoneNo,cusPassword,email);
    }
    public void insertCustomer(String name,String surname,String roomNo,String phoneNo,String cusPassword, String email){
        db.customerInsert(name,surname,roomNo,phoneNo,cusPassword,email);
    }
    public HashMap<String,String> getStartDateOfMeal(){
        ArrayList<HashMap<String, String>> list=db.listAll("Meals");
        HashMap<String,String> list2= new HashMap<String,String>();
        for(HashMap<String,String> i: list){
            switch (i.get("mealName")){
                case "Breakfast":{

                    list2.put("Breakfast",i.get("mealStartTime"));
                    break;
                }
                case "Lunch":{
                    list2.put("Lunch",i.get("mealStartTime"));
                    break;
                }
                case "Dinner":{
                    list2.put("Dinner",i.get("mealStartTime"));
                    break;
                }
            }
        }
        return list2;
    }
    public HashMap<String,String> getStartDateOfEvents(){
        ArrayList<HashMap<String, String>> list=db.listAll("Event");
        HashMap<String,String> list2= new HashMap<String,String>();
        for(HashMap<String,String> i: list){

            list2.put(i.get("eventName"),i.get("startTime"));

        }
        return list2;
    }
    public String getCustomerByEmail(String email){
        return db.getCustomerPassword(email);
    }
    public boolean customerControl(String name, String surname, String email){return db.cusControl(name,surname,email); }

    public void updateCustomer(String email, int status){
        db.customerUpdate(email,status);
    }

    public void changeStatus(){
        db.changeStatus();
    }

    public ArrayList<String> getCustomerbyStatus(){
        ArrayList<String> list= db.getCustomerbyStatus();
        return list;
    }
}
