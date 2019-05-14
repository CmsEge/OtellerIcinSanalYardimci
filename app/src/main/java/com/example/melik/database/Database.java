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
    private static String CUSTOMER_NAME = "cName";
    private static String CUSTOMER_SURNAME="sName";
    private static String ROOM_NO = "roomNo";
    private static String PHONE_NO = "phoneNo";
    private static String PASSWORD = "cusPassword";
    private static String EMAIL = "email";
    private static String STATUS = "status";

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

    //Table of Event
    private static final String TABLE_EVENT = "Event";//burası normal değişken tanımlama gibi
    private static String EVENT_ID = "eventId";
    private static String EVENT_NAME = "eventName";
    private static String START_TIME = "startTime";
    private static String END_TIME = "endTime";
    private static String EVENT_PLACE = "eventPlace";

    //Table of EventNotification
    private static final String TABLE_EVENT_NOTIFICATION = "EventNotification";//burası normal değişken tanımlama gibi
    private static String NOTIFICATION_ID = "notificationId";
    private static String EVE_ID = "eveId";
    private static String CUST_ID = "custId";

    //Table of OrderTable
    private static final String TABLE_ORDER = "OrderTable";//burası normal değişken tanımlama gibi
    private static String ORDER_ID = "orderId";
    private static String ORDER = "orderName";
    private static String COST = "cost";

    //Table of OrderRequest
    private static final String TABLE_ORDER_REQUEST = "OrderRequest";//burası normal değişken tanımlama gibi
    private static String ORDER_REQ_ID = "orderReqId";
    private static String ORD_ID = "orderId";
    private static String CUSTO_ID = "custID";
    private static String DATE_ORDER = "date";
    private static String TIME = "time";

    //Table of Meals
    private static final String TABLE_MEALS = "Meals";//burası normal değişken tanımlama gibi
    private static String MEAL_ID = "mealId";
    private static String MEAL_NAME = "mealName";
    private static String MEAL_START_TIME = "mealStartTime";
    private static String MEAL_END_TIME = "mealEndTime";

    //Table of RoomStatus
    private static final String TABLE_ROOM_STATUS = "RoomStatus";//burası normal değişken tanımlama gibi
    private static String STATUS_ID = "statusId";
    private static String CUSTOME_ID = "custID";
    private static String DATE_ = "date";
    private static String TIME_ = "time";
    private static String DISTURB = "disturb";
    private static String CLEAN = "clean";
    private static String ALARM = "alarm";

    //Table of Faq.
    private static final String TABLE_FAQ = "FAQTable";//burası normal değişken tanımlama gibi
    private static String FAQ_ID = "faqId";
    private static String QUESTION = "question";
    private static String ANSWER = "answer";


    public Database(Context context) {//Database context ile oluşuyor.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //burası normal sorgu kısmı bunu bir string ile oluşturuyoruz.
        String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + "("
                + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CUSTOMER_NAME + " TEXT,"
                + CUSTOMER_SURNAME + " TEXT,"
                + ROOM_NO + " TEXT,"
                + PHONE_NO + " TEXT,"
                + PASSWORD + " TEXT,"
                + EMAIL + " TEXT,"
                + STATUS + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_CUSTOMER);

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

        String CREATE_TABLE_EVENT = "CREATE TABLE " + TABLE_EVENT + "("
                + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EVENT_NAME + " TEXT,"
                + START_TIME + " TEXT,"
                + END_TIME + " TEXT,"
                + EVENT_PLACE + " TEXT"+ ")";
        db.execSQL(CREATE_TABLE_EVENT);

        String CREATE_TABLE_EVENT_NOTIFICATION = "CREATE TABLE " + TABLE_EVENT_NOTIFICATION + "("
                + NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EVE_ID + " INTEGER,"
                + CUST_ID + " INTEGER,"
                + " FOREIGN KEY ("+ EVE_ID +") REFERENCES "+TABLE_EVENT+"("+EVENT_ID+"),"
                + " FOREIGN KEY ("+ CUST_ID +") REFERENCES "+TABLE_CUSTOMER+"("+CUSTOMER_ID+"));";
        db.execSQL(CREATE_TABLE_EVENT_NOTIFICATION);

        String CREATE_TABLE_ORDER = "CREATE TABLE "+ TABLE_ORDER +"("
                + ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ORDER + " TEXT,"
                + COST + " TEXT"+")";
        db.execSQL(CREATE_TABLE_ORDER);

        String CREATE_TABLE_ORDER_REQUEST = "CREATE TABLE " + TABLE_ORDER_REQUEST + "("
                + ORDER_REQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CUSTO_ID + " INTEGER,"
                + ORD_ID + " INTEGER,"
                + DATE_ORDER + " TEXT,"
                + TIME + " TEXT,"
                + " FOREIGN KEY ("+ CUSTO_ID +") REFERENCES "+TABLE_CUSTOMER+"("+CUSTOMER_ID+"),"
                + " FOREIGN KEY ("+ ORD_ID +") REFERENCES "+TABLE_ORDER+"("+ORDER_ID+"));";
        db.execSQL(CREATE_TABLE_ORDER_REQUEST);

        String CREATE_TABLE_MEALS = "CREATE TABLE " + TABLE_MEALS + "("
                + MEAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MEAL_NAME + " TEXT,"
                + MEAL_START_TIME + " TEXT,"
                + MEAL_END_TIME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_MEALS);

        String CREATE_TABLE_ROOM_STATUS = "CREATE TABLE " + TABLE_ROOM_STATUS + "("
                + STATUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CUSTOME_ID + " INTEGER,"
                + DATE_ + " TEXT,"
                + TIME_ + " TEXT,"
                + DISTURB + " INTEGER,"
                + CLEAN + " INTEGER,"
                + ALARM + " TEXT,"
                + " FOREIGN KEY ("+ CUSTOME_ID +") REFERENCES "+TABLE_CUSTOMER+"("+CUSTOMER_ID+"));";
        db.execSQL(CREATE_TABLE_ROOM_STATUS);

        String CREATE_TABLE_FAQ = "CREATE TABLE " + TABLE_FAQ + "("
                + FAQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + QUESTION + " TEXT,"
                + ANSWER + " TEXT " + " ) ";
        db.execSQL(CREATE_TABLE_FAQ);
    }
    //Customer Functions
    public void customerDelete(int customerId){
        SQLiteDatabase db = this.getWritableDatabase();//kullanmakta olduğumuz database'i yazılabilir(writable) olarak açıyor gibi bişey.Algoritma text işlemleri gibi.
        db.delete(TABLE_CUSTOMER, CUSTOMER_ID + " = ?",
                new String[] { String.valueOf(customerId) });//Burada sorguyu oluşturup direk silme fonksiyonuna gönderiyor.customerId ile gönderiyor.
        db.close();//işlem bitince her fonksiyonda db kapatılıyor.
    }
    public void customerInsert( String cName, String sName,String roomNo, String phoneNo, String cusPassword, String email) {
        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(CUSTOMER_NAME, cName);
        values.put(CUSTOMER_SURNAME, sName);
        values.put(ROOM_NO, roomNo);
        values.put(PHONE_NO, phoneNo);
        values.put(PASSWORD, cusPassword);
        values.put(EMAIL, email);
        values.put(STATUS, 0);

        db.insert(TABLE_CUSTOMER, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public HashMap<String, String> customerDetail(int cId){
        HashMap<String,String> customer = new HashMap<String,String>();//Bir hashmap oluşturuyoruz.
        String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER+ " WHERE customerId="+cId;//Gönderdiğimiz cId ile bir query oluşturuyoruz.
        SQLiteDatabase db = this.getReadableDatabase();//Bir değişiklik yapmayacağımız için sadece okunabilir açıyoruz db'yi.
        Cursor cursor = db.rawQuery(selectQuery, null);//Bir cursor ayarlıyor yine algoritmadaki cursor mantığıyla altta bunu en başa çekeceğiz.
        cursor.moveToFirst();//cursor en başa alındı
        if(cursor.getCount() > 0){//cursor boş bir yeri göstermiyorsa içeri giriyor.
            customer.put(CUSTOMER_NAME, cursor.getString(2));
            customer.put(CUSTOMER_SURNAME,cursor.getString(3));
            customer.put(ROOM_NO, cursor.getString(4));
            customer.put(PHONE_NO, cursor.getString(5));
            customer.put(PASSWORD, cursor.getString(6));
            customer.put(EMAIL, cursor.getString(7));
        }
        cursor.close();//cursor kapatılıyor.
        db.close();
        return customer;//Hashmap tipindeki customer geri döndürülüyor.
    }
    public int getCustomerID(String name,String surname,String roomNo,String phoneNo, String cusPassword, String email){
        String selectQuery = "SELECT customerId FROM " + TABLE_CUSTOMER+ " WHERE "+ CUSTOMER_NAME+" = "+"'"+name+"'"+" AND "+CUSTOMER_SURNAME+" = "+"'"+surname+"'"+" " +
                "AND  "+ROOM_NO+" = "+"'"+roomNo+"'"+" AND "+PHONE_NO+" = "+"'"+phoneNo+"'"+" " +
                "AND "+PASSWORD+" = "+"'"+cusPassword+"'"+" AND "+EMAIL+" = "+"'"+email
                +"'";//Gönderdiğimiz cId ile bir query oluşturuyoruz.
        SQLiteDatabase db = this.getReadableDatabase();//Bir değişiklik yapmayacağımız için sadece okunabilir açıyoruz db'yi.
        Cursor cursor = db.rawQuery(selectQuery, null);//Bir cursor ayarlıyor yine algoritmadaki cursor mantığıyla altta bunu en başa çekeceğiz.
        cursor.moveToFirst();//cursor en başa alındı
        if(cursor.getCount() > 0){//cursor boş bir yeri göstermiyorsa içeri giriyor.
            return cursor.getInt(0);
        }
        cursor.close();//cursor kapatılıyor.
        db.close();
        return 0;
    }
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
    public void customerUpdate(String email, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS, status);

        db.update(TABLE_CUSTOMER, values, EMAIL + " = ?",
                new String[] { String.valueOf(email) });
    }
    public void changeStatus(){
        String selectQuery = "SELECT email FROM " + TABLE_CUSTOMER+ " WHERE "+STATUS+" = "+"'"+1
                +"'";//Gönderdiğimiz cId ile bir query oluşturuyoruz.
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);//Bir cursor ayarlıyor yine algoritmadaki cursor mantığıyla altta bunu en başa çekeceğiz.
        cursor.moveToFirst();//cursor en başa alındı
        if(cursor.getCount() > 0){//cursor boş bir yeri göstermiyorsa içeri giriyor.
            customerUpdate(cursor.getString(0),0);
        }
        cursor.close();//cursor kapatılıyor.
        db.close();
    }
    public String getCustomerPassword(String email){
        String selectQuery = "SELECT cusPassword FROM " + TABLE_CUSTOMER+ " WHERE "+ EMAIL+" = "+"'"+email+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String answer = cursor.getString(0);
            db.close();
            cursor.close();
            return answer;
        }
        return null;
    }
    public boolean cusControl(String name, String surname, String email){

        String selectQuery = "SELECT customerId FROM " + TABLE_CUSTOMER+ " WHERE "+ CUSTOMER_NAME+" = "+"'"+name+"'"+
                " AND "+CUSTOMER_SURNAME+" = "+"'"+surname+"'"+" " +
                "AND "+EMAIL+" = "+"'"+email
                +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            boolean answer = cursor.isNull(0);
            db.close();
            cursor.close();
            return answer;
        }else{
            db.close();
            cursor.close();
            return true;
        }
    }
    public ArrayList<String> getCustomerbyStatus(){
        String selectQuery = "SELECT customerId, cName FROM " + TABLE_CUSTOMER+ " WHERE "+STATUS+" = "+"'"+1
                +"'";//Gönderdiğimiz cId ile bir query oluşturuyoruz.
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list= new ArrayList<String>();
        Cursor cursor = db.rawQuery(selectQuery, null);//Bir cursor ayarlıyor yine algoritmadaki cursor mantığıyla altta bunu en başa çekeceğiz.
        cursor.moveToFirst();//cursor en başa alındı

        if(cursor.getCount() > 0){//cursor boş bir yeri göstermiyorsa içeri giriyor.
            list.add(Integer.toString(cursor.getInt(0)));
            list.add(cursor.getString(1));
        }
        cursor.close();//cursor kapatılıyor.
        db.close();
        return list;
    }
    public void orderInsert(String order, String cost) {
        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(ORDER, order);
        values.put(COST, cost);
        db.insert(TABLE_ORDER, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public void orderRequestInsert(int custId, int orderId, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(ORD_ID, orderId);
        values.put(CUSTO_ID, custId);
        values.put(DATE_ORDER,date );
        values.put(TIME,time );
        db.insert(TABLE_ORDER_REQUEST, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public void faqInsert(String question, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(QUESTION, question);
        values.put(ANSWER, answer);
        db.insert(TABLE_FAQ, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public String listAnswer(int id){
        SQLiteDatabase db = this.getReadableDatabase();//yine sadece okunabilir.
        String selectQuery = "SELECT answer FROM " + TABLE_FAQ+ " WHERE "+FAQ_ID+"="+"'"+id+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String answer = cursor.getString(0);
            db.close();
            cursor.close();
            return answer;
        }
        db.close();
        cursor.close();
        return null;
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
        return List;
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
    public List<String> listMealTimes(String mealName){
        SQLiteDatabase db = this.getReadableDatabase();//yine sadece okunabilir.
        String selectQuery = "SELECT mealStartTime, mealEndTime FROM " + TABLE_MEALS+ " WHERE "+MEAL_NAME+"="+"'"+mealName+"'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        List<String> List = new ArrayList<String>();

        if(cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    List.add(cursor.getString(0));
                    List.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        db.close();
        cursor.close();
        return List;
    }
    public void mealInsert(String mealName, String mealStartTime, String mealEndTime) {

        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(MEAL_NAME, mealName);
        values.put(MEAL_START_TIME, mealStartTime);
        values.put(MEAL_END_TIME, mealEndTime);

        db.insert(TABLE_MEALS, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public void eventInsert(String eventName, String startTime, String endTime, String eventPlace) {
        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(EVENT_NAME, eventName);
        values.put(START_TIME, startTime);
        values.put(END_TIME, endTime);
        values.put(EVENT_PLACE, eventPlace);

        db.insert(TABLE_EVENT, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public void eventNotificationInsert(int eveId, int custId) {
        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(EVE_ID, eveId);
        values.put(CUST_ID, custId);

        db.insert(TABLE_EVENT_NOTIFICATION, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public void roomStatusInsert(int custID, String date,String time, int disturb,int clean,String alarm) {

        SQLiteDatabase db = this.getWritableDatabase();//yine yazılabilir olarak açıyoruz db'yi.
        ContentValues values = new ContentValues();//ContentValues tipinde bir değişken oluşturuyoruz.Isme takılmayın mantık anlaşılıyor içine atıyoruz gönderdiğimiz parametreleri.
        values.put(CUSTOME_ID, custID);
        values.put(DATE_, date);
        values.put(TIME_, time);
        values.put(DISTURB, disturb);
        values.put(CLEAN, clean);
        values.put(ALARM, alarm);

        db.insert(TABLE_ROOM_STATUS, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();
    }
    public void resetTables(String tableName){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, null, null);
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
   /* public  HashMap<String, String> CustEvents(int custId){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT Event.eventName, Event.startTime FROM  Event INNER JOIN EventNotification ON EventNotification.eveId=Event.eventId WHERE EventNotification.custId="+"'"+custId+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        HashMap<String, String> map = new HashMap<String, String>();//bu kadar iç içe arraylist hashmap anlamam ben internetten baktım...
        if (cursor.moveToFirst()) {
            do {

                map.put(cursor.getString(0), cursor.getString(1));//tek tek customerleri çıkarıyor ve bir altta listeye ekliyor.
            } while (cursor.moveToNext());
        }
        db.close();
        return map;
    }*/
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CUSTOMER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
