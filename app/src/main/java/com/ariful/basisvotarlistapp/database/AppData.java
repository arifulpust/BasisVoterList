package com.ariful.basisvotarlistapp.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by bipulkhan on 12/27/16.
 */


public class AppData {

    //keys
    public static String employeeInfo = "person";
    public static String copy = "copy";
    public static String Person = "Person";
    public static String check = "check";

//AppData.saveData
    public static final String MyPREFERENCES = "basisinfo" ;



    public static void saveData(String key, String value, Context context){
        if(context!=null) {


            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value);
            editor.commit();
          //  Log.e("saveData"," "+ key+"  "+getData(key,context));


        }
        else
        {
            Log.e("saveData"," null");
        }
    }

    public static String getData(String key, Context context){
        String string="";
        if(context!=null) {
            //Log.e("contex", "not null");
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, 0);
             string = prefs.getString(key, "");
        }
        else
        {
            //Log.e("contex"," null");
        }
        return string;
    }
    public static String Clear(Context context){
        String string="";
        if(context!=null) {
            //Log.e("contex", "not null");
            SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
        }
        else
        {
            //Log.e("contex"," null");
        }
        return string;
    }


}
