package com.molaja.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by florianhidayat on 10/8/15.
 */
public class ChatDBHelper extends SQLiteOpenHelper {

    private static ChatDBHelper ourInstance;

    private static final String DB_NAME = "ChatDB.db";
    private static final int DB_VERSION = 1;

    public ChatDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public static ChatDBHelper getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new ChatDBHelper(context);

        return ourInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
