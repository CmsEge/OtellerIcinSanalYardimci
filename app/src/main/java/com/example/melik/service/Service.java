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

import java.util.ArrayList;
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
}
