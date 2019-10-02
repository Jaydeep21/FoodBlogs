package com.example.foodblogs;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import static android.provider.BaseColumns._ID;


public class DatabaseHelper extends SQLiteOpenHelper {
    SharedPreferences pref;

    Context context;
    public DatabaseHelper(Context context){ super(context,"Signup.db",null,2);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(uid integer  primary key AUTOINCREMENT, username varchar(20),email text , password varchar(20)) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
    }

    public boolean insert(String username, String email, String password){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username",username);
        contentValues.put("email",email);
        contentValues.put("password",password);
        long ins = db.insert("user",null,contentValues);
        if(ins==-1)return false;
        else return true;
    }

    public boolean emailpassword(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email=? and password=?",new String[]{email,password});
        if(cursor.getCount()>0){
            return true;}
        else return false;
    }

    public boolean checkemail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email=?",new String[]{email});
        if(cursor.getCount()>0) return false;
        else return true;
    }
}
