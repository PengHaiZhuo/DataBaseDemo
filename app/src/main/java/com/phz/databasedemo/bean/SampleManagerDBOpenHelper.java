package com.phz.databasedemo.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SampleManagerDBOpenHelper extends SQLiteOpenHelper {
//    public static final String DB_NAME="sample.db";
//    private static final int VERSION = 1;

    /**
     *
     * @param context
     * @param name 数据库名称
     * @param factory 为null就好
     * @param version 数据库版本
     */
    public SampleManagerDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + SampleDBController.TABLE_NAME
                + String.format(
                "("
                        + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "%s VARCHAR, "
                        + "%s INTEGER"
                        +")"
                , SampleManagerDBModel.ID
                , SampleManagerDBModel.MESSAGE
                , SampleManagerDBModel.TIME
        )) ;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                upToDbVersion2(db);
            case 2:
                upToDbVersion3(db);
            default:
                break;
        }
    }

    public void upToDbVersion2(SQLiteDatabase db){
        db.execSQL("ALTER TABLE " + SampleDBController.TABLE_NAME + " ADD COLUMN level text");
    }

    public void upToDbVersion3(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("message", "升级到版本3,数据变了");
        db.update(SampleDBController.TABLE_NAME, values, null, null);
    }

}
