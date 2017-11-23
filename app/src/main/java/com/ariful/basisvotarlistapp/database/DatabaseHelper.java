package com.ariful.basisvotarlistapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ariful.basisvotarlistapp.activity.MainActivity;
import com.ariful.basisvotarlistapp.model.Person;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Dream71 on 13/11/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    public static String DB_PATH =null;

    // private static String DB_NAME = "db_info";
    private static String DB_NAME = "basis.sqlite";

    private SQLiteDatabase db;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH="/data/data/"+context.getPackageName()+"/"+"databases/";
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
        }else{
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");

            }
        }

    }
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[2024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(db != null)
            db.close();

        super.close();

    }

    public boolean addItems(String tableName,ArrayList<ContentValues> list){

        SQLiteDatabase db = this.getWritableDatabase();
        if(list == null || list.size()==0){
            return false;
        }
        for (int i = 0;i<list.size();i++){
            ContentValues values = list.get(i);
            db.insert(tableName, null, values);
        }
        db.close();
        return true;
    }
    public boolean deleteAllItems(String tableName){

        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "DELETE FROM " + tableName;
        db.execSQL(Query);
        db.close();
        return true;
    }
    public int deleteItems(String tableName,String columnName,String id){

        SQLiteDatabase db = this.getWritableDatabase();
        // String Query = "DELETE FROM " + tableName;
        int deleted = db.delete(tableName, columnName + "=?", new String[]{"" + id});
        //db.execSQL(Query);
        db.close();
        return deleted;
    }
    public int UpdateValue( String KeyId, String TableName, Person person) {
        int update=-1;
        try {
            Log.e("person", person.id + "  \n" + person.company_name +"   "+person.getName()+ person.toString());
        }
        catch (Exception e)
        {

        }
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBTablesInfo.NAME, person.name);
        cv.put(DBTablesInfo.COMPANY_NAME, person.company_name);
        cv.put(DBTablesInfo.COMMENTS, person.comments);
        cv.put(DBTablesInfo.DESIGNATION, person.designation);
        cv.put(DBTablesInfo.ADDRESS, person.address);
        cv.put(DBTablesInfo.MID, person.MID);
        cv.put(DBTablesInfo.MOBILE, person.mobile);
        cv.put(DBTablesInfo.POSIBILITY, person.possiblity);
        cv.put(DBTablesInfo.ISVISITED, person.isvisited);
        cv.put(DBTablesInfo.ISCALLED, person.isCalled);


        try {
            update = database.update(TableName, cv, KeyId+" = " + person.id, null);
            Log.e("UPDATE", cv.toString()+"  "+update);


        } catch (SQLiteException exception) {
            exception.printStackTrace();
        }

        database.close();
        return update;

    }

    public   String getMenuQuery(int position )
    {
        Log.e("getMenuQuery","call");
        String selectQuery="";
        if(position==0) {
            selectQuery = "";
        }
        else if(position==(MainActivity.menuNames.size()-1))
        {
            String quat="\"";
            String date=quat+MainActivity.menuNames+quat;
            //  Log.e("collumnValue",""+quat+collumnValue+quat);
            //  String notquery="";
            int i,j;
            int length=MainActivity.menuNames.size()-1;
            for( i=1;i<length;i++)
            {
                String[] address = MainActivity.menuNames.get(i).trim().split("\\+");
                Log.e("collumnValue",address+"   "+quat+MainActivity.menuNames+quat);
                for( j=0;j<address.length;j++)
                {
                    if(i==(length-1)&&j==(address.length-1))
                    {
                        selectQuery=selectQuery+DBTablesInfo.ADDRESS+" NOT  like '%"+address[j].trim()+"%'";
                    }
                    else {
                        selectQuery = selectQuery + DBTablesInfo.ADDRESS+ " NOT  like '%" + address[j].trim() + "%' and ";
                    }
                }
            }
            // selectQuery = "SELECT  * FROM " + tableName+" where "+selectQuery;
        }
        else
        {
            String[] address = MainActivity.menuNames.get(MainActivity.listPosition).split("\\+");
            int i;
            for( i=0;i<address.length-1;i++)
            {
                selectQuery=selectQuery+DBTablesInfo.ADDRESS+" like '%"+address[i].trim()+"%' or ";
            }
            selectQuery=selectQuery+DBTablesInfo.ADDRESS+"   like '%"+address[i].trim()+"%' ";
            //  selectQuery = "SELECT  * FROM " + tableName+" where "+selectQuery;
        }
        return selectQuery;
    }
    public Cursor getAllResItems(String tableName) {
        String selectQuery="";

        Log.e("getAllResItems","call");


        if(MainActivity.listPosition==0) {
            selectQuery = "SELECT  * FROM " + tableName;
        }
        else if(MainActivity.listPosition==(MainActivity.menuNames.size()-1))
        {
            String quat="\"";
            String date=quat+MainActivity.menuNames+quat;
            int i,j;
            int length=MainActivity.menuNames.size()-1;
            for( i=1;i<length;i++)
            {
                String[] address = MainActivity.menuNames.get(i).trim().split("\\+");
                Log.e("collumnValue",address+"   "+quat+MainActivity.menuNames+quat);
                for( j=0;j<address.length;j++)
                {
                    if(i==(length-1)&&j==(address.length-1))
                    {
                        selectQuery=selectQuery+DBTablesInfo.ADDRESS+" NOT  like '%"+address[j].trim()+"%'";
                    }
                    else {
                        selectQuery = selectQuery + DBTablesInfo.ADDRESS+ " NOT  like '%" + address[j].trim() + "%' and ";
                    }
                }
            }
            selectQuery = "SELECT  * FROM " + tableName+" where "+selectQuery;
        }
        else
        {
            String[] address = MainActivity.menuNames.get(MainActivity.listPosition).split("\\+");
            int i;
            for( i=0;i<address.length-1;i++)
            {
                selectQuery=selectQuery+DBTablesInfo.ADDRESS+" like '%"+address[i].trim()+"%' or ";
            }
            selectQuery=selectQuery+DBTablesInfo.ADDRESS+"   like '%"+address[i].trim()+"%' ";
            selectQuery = "SELECT  * FROM " + tableName+" where "+selectQuery;
        }
Log.e("selectQuery ",""+selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("Count",""+cursor.getCount());
        db.close();
        return cursor;
    }
    public Cursor getAllDateItems(String tableName,String DateCullmn,String collumnValue,String ordrCollumn) {


        String quat="\"";
        String date=quat+collumnValue+quat;
        Log.e("collumnValue",""+quat+collumnValue+quat);
        String selectQuery = "SELECT  * FROM " + tableName+" where "+DateCullmn+" ="+date+" order by "+ordrCollumn+" DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("Count",""+cursor.getCount()+"    "+selectQuery);
        db.close();
        return cursor;
    }
    public Cursor getAllDateItemsByCat(String tableName,String AddressCollumn,String collumnValue) {

        Log.e("getAllDateItemsByCat","call");
        String quat="\"";
        String date=quat+collumnValue+quat;
        Log.e("collumnValue",""+quat+collumnValue+quat);
        String selectQuery="";
        if(collumnValue.equals("None"))
        {
            selectQuery = " NOT like '%"+"Medium"+"%' and "+AddressCollumn+" NOT like '%"+"Confirm"+"%' and "+AddressCollumn+" NOT like '%"+"Negative"+"%' and "+AddressCollumn+" NOT like '%"+"Positive"+"%'";
        }
        else
            selectQuery = " like '%"+collumnValue+"%'";
        if(MainActivity.listPosition==0)
        {

        }
        else
        {
            selectQuery=selectQuery+" and "+ getMenuQuery(MainActivity.listPosition);
        }


        selectQuery = "SELECT  * FROM "+tableName +" where "+AddressCollumn+selectQuery;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("Count",""+cursor.getCount()+"    "+selectQuery);
        db.close();
        return cursor;
    }
    public Cursor getAllItemsByVisitedORCalled(String tableName,String AddressCollumn,int value) {

        Log.e("323"," getAllItemsByVisitedORCalledcall");
        String quat="\"";
        //   String date=quat+collumnValue+quat;
        //Log.e("collumnValue",""+quat+collumnValue+quat);
        String selectQuery="";
        if(value==0)
        {
            selectQuery =AddressCollumn+" = '1'";
        }
        else {
            selectQuery = AddressCollumn + " = '0'";
        }
        if(MainActivity.listPosition>0)
        {
            selectQuery=selectQuery+" and "+ getMenuQuery(MainActivity.listPosition);
        }


        selectQuery = "SELECT  * FROM " + tableName+" where "+selectQuery;
        Cursor cursor=null;
        Log.e("Count", "" + "    " + selectQuery);
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            Log.e("Count", "" + cursor.getCount() + "    " + selectQuery);
            db.close();
        }
        catch (Exception e)
        {
            Log.e("Exception",""+e.getMessage());
        }
        return cursor;
    }
    public Cursor getAllDateItemsByAddress(String tableName,String AddressCollumn,String collumnValue) {

        Log.e("323"," getAllDateItemsByAddress call");
        String quat="\\+";
        String[] address = collumnValue.split(quat);
        // String date=quat+collumnValue+quat;
        Log.e("collumnValue",address+"   "+quat+collumnValue+quat);

        String notquery="";
        int i;
        for( i=0;i<address.length-1;i++)
        {
            notquery=notquery+AddressCollumn+" like '%"+address[i].trim()+"%' or ";
        }
        // String selectQuery = "SELECT  * FROM " + tableName+" where "+AddressCollumn+" like '%"+collumnValue+"%'";

        notquery=notquery+AddressCollumn+"   like '%"+address[i].trim()+"%' ";

//        if(MainActivity.listPosition>0)
//        {
//            notquery=notquery+" and "+ getMenuQuery(MainActivity.listPosition);
//        }
        String selectQuery = "SELECT  * FROM " + tableName+" where "+notquery;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("Count",""+cursor.getCount()+"    "+selectQuery);
        db.close();
        return cursor;
    }
    public Cursor getAllDateItemsByAddressNot(String tableName,String AddressCollumn,ArrayList<String> collumnValue) {

        Log.e("323"," getAllDateItemsByAddressNot call");
        String quat="\"";
        String date=quat+collumnValue+quat;
        Log.e("collumnValue",""+quat+collumnValue+quat);
        String notquery="";
        int i,j;
        int length=collumnValue.size()-2;
        for( i=1;i<length;i++)
        {

            String[] address = collumnValue.get(i).trim().split("\\+");

            Log.e("collumnValue",address+"   "+quat+collumnValue+quat);



            for( j=0;j<address.length;j++)
            {
                if(i==(length-1)&&j==(address.length-1))
                {
                    notquery=notquery+AddressCollumn+" NOT  like '%"+address[j].trim()+"%'";
                }
                else {
                    notquery = notquery + AddressCollumn + " NOT  like '%" + address[j].trim() + "%' and ";
                }
                // notquery=notquery+AddressCollumn+" like '%"+address[i].trim()+"%' or ";
            }

            //notquery=notquery+AddressCollumn+" NOT  like '%"+collumnValue.get(i)+"%' and ";
        }
        //notquery=notquery+AddressCollumn+" NOT  like '%"+collumnValue.get(i)+"%' ";
//        if(MainActivity.listPosition>0)
//        {
//            notquery=notquery+" and "+ getMenuQuery(MainActivity.listPosition);
//        }
        String selectQuery = "SELECT  * FROM " + tableName+" where "+notquery;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("Count",""+cursor.getCount()+"    "+selectQuery);
        db.close();
        return cursor;
    }
    public Cursor getAllDateItemsByMultiValue(String tableName,String collumnValue) {

        Log.e("323"," getAllDateItemsByMultiValue call"+MainActivity.listPosition);
        String quat="\"";
        String date=quat+collumnValue+quat;
        Log.e("collumnValue",""+quat+collumnValue+quat);
        String selectQuery="";
//        Cursor cursor = db.query(
//                tableName,
//                new String[] { DBTablesInfo.ADDRESS, DBTablesInfo.NAME, DBTablesInfo.COMPANY_NAME, DBTablesInfo.MID, DBTablesInfo.MOBILE, DBTablesInfo.DESIGNATION },
//                SUBCATEGORY + " LIKE '%" + subcategory + "%'",
//                null, null, null, null, null);

        {
            selectQuery = DBTablesInfo.ADDRESS + " like '%" + collumnValue + "%' or " + DBTablesInfo.NAME + " like '%" + collumnValue + "%' or " + DBTablesInfo.COMPANY_NAME + " like '%" + collumnValue + "%' or " + DBTablesInfo.MID + " like '%" + collumnValue + "%' or " + DBTablesInfo.DESIGNATION + " like '%" + collumnValue + "%' or " + DBTablesInfo.COMMENTS + " like '%" + collumnValue + "%' or " + DBTablesInfo.POSIBILITY + " like '%" + collumnValue + "%' or " + DBTablesInfo.MOBILE + " like '%" + collumnValue + "%'";
            Log.e("selectQuery", "" + selectQuery);
        }
        if(MainActivity.listPosition>0)
        {
            selectQuery= "( "+getMenuQuery(MainActivity.listPosition)+") and ( "+selectQuery+")";
        }
        selectQuery="SELECT  * FROM " + tableName+" where "+selectQuery;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("Count",""+cursor.getCount()+"    "+selectQuery);
        db.close();
        return cursor;
    }
    public void open() {
        db = this.getWritableDatabase();
    }

    /**
     * method to close database connection
     *
     * @param: takes no params
     */

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //return cursor
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
        return db.query(DBTablesInfo.TABALE_NAME, null, null, null, null, null, null);


    }
}