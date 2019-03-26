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

        db.customerInsert("17614534630", "Saygun", "Askin", "107", "0506 870 74 03");
        db.customerInsert("11223344556","Melikenur","Gülas","108","0512 345 67 89");
        db.customerInsert("11223344567","Ceren Yaren","Erer","109","0512 345 67 90");

        db.alacarteInsert("Italian Restourant","dsfsdf","zf","sfsdf","fdgdfg");
        db.alacarteInsert("Far East Restourant","dsfsdf","zf","sfsdf","fdgdfg");
        db.alacarteInsert("Ottoman Restourant","dsfsdf","zf","sfsdf","fdgdfg");

    }
    public ArrayList<String> getAllAlacarteNames(){
        return db.allAlacarteNames();
    }
}
