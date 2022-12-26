package com.example.currencyconverter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class OmdbDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Omdb.db";

    public static final String OMDB_TABLE = "currency";
    public static final String OMDB_COL_NAME = "name";
    public static final String OMDB_COL_RATE = "rate";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + OMDB_TABLE + " ("
            + BaseColumns._ID+" INTEGER PRIMARY KEY,"
            + OMDB_COL_NAME + " varchar,"
            + OMDB_COL_RATE + " varchar)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + OMDB_TABLE;

    public OmdbDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
