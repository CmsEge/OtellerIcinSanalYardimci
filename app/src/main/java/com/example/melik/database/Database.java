package com.example.melik.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;//Bu neden bilmiyorum versiyon değiştirirsek diye bizi bilgilendirmek amaçlı sanırım.

    private static final String DATABASE_NAME = "ChatBot";

    private static final String TABLE_NAME = "Customer";//burası normal değişken tanımlama gibi
    private static String CUSTOMER_ID = "customerId";
    private static String ID = "id";
    private static String CUSTOMER_NAME = "cName";
    private static String CUSTOMER_SURNAME = "cSurname";
    private static String ROOM_NO = "roomNo";
    private static String PHONE_NO = "phoneNo";

    public Database(Context context) {//Database context ile oluşuyor.

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //burası normal sorgu kısmı bunu bir string ile oluşturuyoruz.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ID + " TEXT,"
                + CUSTOMER_NAME + " TEXT,"
                + CUSTOMER_SURNAME + " TEXT,"
                + ROOM_NO + " TEXT,"
                + PHONE_NO + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);//Daha sonra bu stringi db'ye gönderdiğimizde tabloyu oluşturuyor.

    }

    /**
     * Tablodan müşteri silmek istediğimizde müşteri id'si göndererek bu fonksiyonu çalıştıracağız.
     * @param customerId The hotel's unique customer number(autoincrement for now)
     * @return nothing
     */
    public void customerDelete(int customerId){

        SQLiteDatabase db = this.getWritableDatabase();//kullanmakta olduğumuz database'i yazılabilir(writable) olarak açıyor gibi bişey.Algoritma text işlemleri gibi.
        db.delete(TABLE_NAME, CUSTOMER_ID + " = ?",
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

        db.insert(TABLE_NAME, null, values);//bu değerleri insert'e direk gönderiyoruz.
        db.close();

    }

    /**
     * Müşteri id'si ile müşterinin tüm bilgilerini çekmek için bu fonksiyonu kullanacağız.
     * @param cId The hotel's unique customer number(autoincrement for now)
     * @return HashMap<String,String> customer
     */
    public HashMap<String, String> customerDetail(int cId){

        HashMap<String,String> customer = new HashMap<String,String>();//Bir hashmap oluşturuyoruz.
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE customerId="+cId;//Gönderdiğimiz cId ile bir query oluşturuyoruz.

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
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
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

        db.update(TABLE_NAME, values, CUSTOMER_ID + " = ?",
                new String[] { String.valueOf(customerId) });

    }

    /**
     * Tablonun Satır sayısını döndüren fonksiyon.
     * @return int rowCount
     */
    public int getRowCount() {

        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;

    }

    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!
     * Tabloları silen fonksiyon.
     * @return nothing
     */
    public void resetTables(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
