package com.example.kiemtragk.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kiemtragk.Model.UserModel;

import java.util.ArrayList;
import java.util.List;
public class SQLite extends SQLiteOpenHelper {
    private SQLiteDatabase myDB;
    private static final String DB_NAME = "KTGK.db";
    private static final int VERSION = 1;
    private Context mContext;

    /*
     Table name
    */
    private static final String TABLE_LOGIN = "Login";
    public SQLite(Context context) {
        super( context, DB_NAME, null, VERSION );
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sQueryLogin = "Create table if not exists " + TABLE_LOGIN
                + "( "
                + " USER_ID INTERGER"
                + " )";


        db.execSQL( sQueryLogin );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_LOGIN );
        onCreate( db );
    }

    public void OpenDB() {
        myDB = getWritableDatabase();
    }

    public void CloseDB() {
        if (myDB.isOpen() && myDB != null) {
            myDB.close();
        }
    }


    // INSERT DATA TO TABLES
    public long insertLogin(int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( "USER_ID", ID );
        return db.insert( TABLE_LOGIN, null, values );
    }

    // DELETE TABLES
    public void deleteLogin() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sQuery = "DELETE FROM " + TABLE_LOGIN;
        db.execSQL( sQuery );
    }
    // SELECT DATA FROM TABLES
    public List<UserModel> selectLogin() {
        String sQuery = "SELECT * FROM " + TABLE_LOGIN;
        List<UserModel> slist = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( sQuery, null );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            UserModel userModel = new UserModel();
            userModel.setUser_id( cursor.getInt( cursor.getColumnIndex( "USER_ID" ) ) );
            slist.add( userModel );
        }
        cursor.close();
        return slist;
    }
}
