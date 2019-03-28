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

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class Service {

    private Database db;
    public Service(Database db){
        this.db=db;
    }
    public void InsertTables() {

        db.customerInsert("0", "Saygun", "Askin", "107", "0506 870 74 03");
        db.customerInsert("1","Melikenur","Gülas","108","0512 345 67 89");
        db.customerInsert("2","Ceren Yaren","Erer","109","0512 345 67 90");

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


        db.faqInsert("Are pets accepted?","No, we do not accept pets.");
        db.faqInsert("Do you have WIFI at the hotel?","Free wireless internet is available throughout the hotel and in all rooms.");
        db.faqInsert("What are the check-in and check-out times?","Check-in time is 14:00 and check-out time is 12:00.");

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
        for(String i: list){
            a+="\n"+i;
        }
        speech=speech.replace("$RestaurantTypes",a);
        return speech;
    }
    public void getReservationInfo(int cusID,String AlaName,String date){
        List<String> list=new ArrayList<String>();
        list=this.getAllAlacarteNames();
        int alaID=0;
        int a=1;
        for(String i: list){
            if(i.equals(AlaName)){alaID=a;break;}else{a++;}
        }
        this.reservationAlaInsert(date,cusID,alaID);
    }

    public String EventInfo(String speech)
    {
        ArrayList<HashMap<String, String>> list=db.listAll("Event");
        String s="";
        for(HashMap<String,String> i: list){
            s+="\n"+ i.get("eventId")+"."+i.get("eventName")+" starts at "+i.get("startTime")+" and ends at "+i.get("endTime")+" at "+i.get("eventPlace");
        }
        s+="\n";
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
        for(HashMap<String,String> i: list){
            s+="\n"+ i.get("orderId")+"."+i.get("orderName")+" costs "+i.get("cost");
        }
        s+="\n";
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
}
