package com.example.melik.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;//Bu neden bilmiyorum versiyon değiştirirsek diye bizi bilgilendirmek amaçlı sanırım.
    private static final String DATABASE_NAME = "ChatBot";

    //Table of Customer
    private static final String TABLE_CUSTOMER = "Customer";//burası normal değişken tanımlama gibi
    private static String CUSTOMER_ID = "customerId";
    private static String ID = "id";
    private static String CUSTOMER_NAME = "cName";
    private static String CUSTOMER_SURNAME = "cSurname";
    private static String ROOM_NO = "roomNo";
    private static String PHONE_NO = "phoneNo";

    //Table of Alacarte
    private static final String TABLE_ALACARTE = "Alacarte";//burası normal değişken tanımlama gibi
    private static String ALACARTE_ID = "alacarteId";
    private static String NAME = "name";
    private static String ENTREE_START = "entreeStart";
    private static String WARM_STARTER = "warmStarter";
    private static String MAIN_COURSE = "mainCourse";
    private static String DESSERT = "dessert";

    //Table of ReservationAlacarte
    private static final String TABLE_RESERVATION_ALA = "ReservationAla";//burası normal değişken tanımlama gibi
    private static String RESERVATION_ID = "reservationId";
    private static String DATE = "date";
    private static String ALA_ID = "alaID";
    private static String CUS_ID = "cusID";

    public Database(Context context) {//Database context ile oluşuyor.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //burası normal sorgu kısmı bunu bir string ile oluşturuyoruz.
        String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + "("
                + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ID + " TEXT,"
                + CUSTOMER_NAME + " TEXT,"
                + CUSTOMER_SURNAME + " TEXT,"
                + ROOM_NO + " TEXT,"
                + PHONE_NO + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_CUSTOMER);//Daha sonra bu stringi db'ye gönderdiğimizde tabloyu oluşturuyor.

        String CREATE_TABLE_ALACARTE = "CREATE TABLE " + TABLE_ALACARTE + "("
                + ALACARTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT,"
                + ENTREE_START + " TEXT,"
                + WARM_STARTER + " TEXT,"
                + MAIN_COURSE + " TEXT,"
                + DESSERT + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_ALACARTE);

        String CREATE_TABLE_RESERVATION_ALA = "CREATE TABLE " + TABLE_RESERVATION_ALA + "("
                + RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE + " TEXT,"
                + CUS_ID + " INTEGER,"
                + ALA_ID + " INTEGER,"
                + " FOREIGN KEY ("+ CUS_ID +") REFERENCES "+TABLE_CUSTOMER+"("+CUSTOMER_ID+"),"
                + " FOREIGN KEY ("+ ALA_ID +") REFERENCES "+TABLE_ALACARTE+"("+ALACARTE_ID+"));";
        db.execSQL(CREATE_TABLE_RESERVATION_ALA);
    }
    //Customer Functions
    public void customerDelete(int customerId){

        SQLiteDatabase db = this.getWritableDatabase();//kullanmakta olduğumuz database'i yazılabilir(writable) olarak açıyor gibi bişey.Algoritma text işlemleri gibi.
        db.delete(TABLE_CUSTOMER, CUSTOMER_ID + " = ?",
                new String[] { String.valueOf(customerId) });//Burada sorguyu oluşturup direk silme fonksiyonuna gönderiyor.customerId ile gönderiyor.
        db.close();//işlem bitince her fonksiyonda db kapatılıyor.
    }
    /**
     * Müşteri eklemesi yapacağımızda bu fonksiyonu kullanacağız.
     * @param id customer's identification number
     * @param cName customer's first name
     * @param cSurname customer's last name
     * @param roomNo customer's room number
     * @param phoneNo customer's phone number
     * @return nothing
     */
    public void customerInsert(String id, String cName, String cSurname, String roomNo, String phoneNo) {

        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(ID, id);
        values.put(CUSTOMER_NAME, cName);
        values.put(CUSTOMER_SURNAME, cSurname);
        values.put(ROOM_NO, roomNo);
        values.put(PHONE_NO, phoneNo);

        db.insert(TABLE_CUSTOMER, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    /**
     * Müşteri id'si ile müşterinin tüm bilgilerini çekmek için bu fonksiyonu kullanacağız.
     * @param cId The hotel's unique customer number(autoincrement for now)
     * @return HashMap<String,String> customer
     */
    public HashMap<String, String> customerDetail(int cId){

        HashMap<String,String> customer = new HashMap<String,String>();//Bir hashmap oluşturuyoruz.
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER+ " WHERE customerId="+cId;//Gönderdiğimiz cId ile bir query oluşturuyoruz.

        SQLiteDatabase db = this.getReadableDatabase();//Bir değişiklik yapmayacağımız için sadece okunabilir açıyoruz db'yi.
        Cursor cursor = db.rawQuery(selectQuery, null);//Bir cursor ayarlıyor yine algoritmadaki cursor mantığıyla altta bunu en başa çekeceğiz.

        cursor.moveToFirst();//cursor en başa alındı
        if(cursor.getCount() > 0){//cursor boş bir yeri göstermiyorsa içeri giriyor.
            customer.put(ID, cursor.getString(1));
            customer.put(CUSTOMER_NAME, cursor.getString(2));
            customer.put(CUSTOMER_SURNAME, cursor.getString(3));
            customer.put(ROOM_NO, cursor.getString(4));
            customer.put(PHONE_NO, cursor.getString(5));
        }
        cursor.close();//cursor kapatılıyor.
        db.close();
        return customer;//Hashmap tipindeki customer geri döndürülüyor.
    }
    /**
     * Tüm customer tablomuzu görmek için bu fonksiyonu kullanacağız.
     * @return ArrayList<HashMap<String, String>> customerList
     */
    public ArrayList<HashMap<String, String>> allCustomers(){

        SQLiteDatabase db = this.getReadableDatabase();//yine sadece okunabilir.
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> customerList = new ArrayList<HashMap<String, String>>();//bu kadar iç içe arraylist hashmap anlamam ben internetten baktım...

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));//tek tek customerleri çıkarıyor ve bir altta listeye ekliyor.
                }
                customerList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return customerList;//Tüm müşterilerin listesini geri döndürüyor.
    }
    /**
     * Müşterinin herhangi bir verisinde değişiklik yapmak istediğimizde bu fonksiyonu kullanacağız.
     * @param customerId The hotel's unique customer number(autoincrement for now)
     * @param id customer's identification number
     * @param cName customer's first name
     * @param cSurname customer's last name
     * @param roomNo customer's room number
     * @param phoneNo customer's phone number
     * @return nothing
     */
    public void customerUpdate(int customerId, String id, String cName, String cSurname, String roomNo, String phoneNo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(CUSTOMER_NAME, cName);
        values.put(CUSTOMER_SURNAME, cSurname);
        values.put(ROOM_NO, roomNo);
        values.put(PHONE_NO, phoneNo);

        db.update(TABLE_CUSTOMER, values, CUSTOMER_ID + " = ?",
                new String[] { String.valueOf(customerId) });

    }
    /**
     * Tablonun Satır sayısını döndüren fonksiyon.
     * @return int rowCount
     */
    public int getRowCount() {

        String countQuery = "SELECT  * FROM " + TABLE_CUSTOMER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }

    //Alacarte Functions
    public void alacarteInsert(String name,ArrayList<String> entree, ArrayList<String> warm, ArrayList<String> main, ArrayList<String> dessert) {

        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(NAME, name);
        values.put(ENTREE_START, String.valueOf(entree));
        values.put(WARM_STARTER, String.valueOf(warm));
        values.put(MAIN_COURSE, String.valueOf(main));
        values.put(DESSERT, String.valueOf(dessert));

        db.insert(TABLE_ALACARTE, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public List<String> allAlacarteNames(){

        SQLiteDatabase db = this.getReadableDatabase();//yine sadece okunabilir.
        String selectQuery = "SELECT * FROM " + TABLE_ALACARTE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> List = new ArrayList<String>();

        if(cursor.getCount()>0){
            if (cursor.moveToFirst()) {
                do {
                    List.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }else{
            Log.i("alacartenames","boş");
        }

        db.close();
        cursor.close();
        return List;//Tüm müşterilerin listesini geri döndürüyor.
    }

    //ReservationAla Functions
    public void reservationAlaInsert(String date, int cus, int ala) {

        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(DATE, date);
        values.put(CUS_ID, cus);
        values.put(ALA_ID, ala);

        db.insert(TABLE_RESERVATION_ALA, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public ArrayList<HashMap<String, String>> listAll(String tableName){

        SQLiteDatabase db = this.getReadableDatabase();//yine sadece okunabilir.
        String selectQuery = "SELECT * FROM " + tableName;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();//bu kadar iç içe arraylist hashmap anlamam ben internetten baktım...

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));//tek tek customerleri çıkarıyor ve bir altta listeye ekliyor.
                }
                List.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return List;//Tüm müşterilerin listesini geri döndürüyor.
    }



    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!
     * Tabloları silen fonksiyon.
     * @return nothing
     */
    public void resetTables(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CUSTOMER, null, null);
        db.close();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
