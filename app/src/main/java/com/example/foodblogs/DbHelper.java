package com.example.foodblogs;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;

public class DbHelper extends SQLiteOpenHelper {
    //SharedPreferences pref;
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "Signup.db";
    private static final int DATABASE_VERSION = 2;

    SQLiteDatabase db;
    ContentResolver mContentResolver;
    Context context;
    public final static String COLUMN_NAME = "imagename";

    public final static String TABLE_NAME = "recipie";

    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

//        mContentResolver = context.getContentResolver();
//
//        db = this.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT ,  dishname TEXT , cusine TEXT, course TEXT,image BLOB, video BLOB, description TEXT, recipie TEXT, FOREIGN KEY(email) REFERENCES user(email)  " + " );";

        db.execSQL(SQL_CREATE_IMAGE_TABLE);

        Log.d(TAG, "Database Created Successfully" );

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addToDb(ModelClass objectModelClass){

        SQLiteDatabase db =this.getWritableDatabase();

        Bitmap  imageToStoreBitmap = objectModelClass.getImage();

        objectByteArrayOutputStream = new ByteArrayOutputStream();
        //imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);

        imageInBytes = objectByteArrayOutputStream.toByteArray();

        ContentValues cv = new  ContentValues();

        cv.put("email", objectModelClass.getEmail());
        cv.put("dishname", objectModelClass.getTitle());
        cv.put("cusine", objectModelClass.getCusine());
        cv.put("course", objectModelClass.getCourse());
        cv.put("description", objectModelClass.getDescription());
        cv.put("recipie", objectModelClass.getRecipie());
        cv.put("image",   imageInBytes);
        //cv.put("video",   video);
        long checkIfQueryRuns = db.insert( TABLE_NAME, null, cv );
        if(checkIfQueryRuns!=0){
            //Toast.makeText(context,"Data Inserted",Toast.LENGTH_SHORT).show();
            db.close();
        }
        else{
            //Toast.makeText(context,"Not Inserted",Toast.LENGTH_SHORT).show();
        }
    }

//    public void listDetails(List<ClipData.Item> arrayList){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from recipie",null);
//        if(cursor != null){
//            if(cursor.moveToFirst()){
//                do{
//                    ClipData.Item item = new ClipData.Item(arrayList);
//                }while ();
//            }
//        }
//    }
}
