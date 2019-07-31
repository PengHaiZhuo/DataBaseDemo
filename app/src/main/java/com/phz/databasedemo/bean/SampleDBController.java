package com.phz.databasedemo.bean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * ①使用Api实现增删改查;②使用sql语句实现增删改查
 */
public class SampleDBController{
    public static final String DB_NAME="sample.db";//数据库名称
    private static final int VERSION = 1;//数据库版本号
    public final static String TABLE_NAME = "mysql_sample";
    private final SQLiteDatabase db;

    public SampleDBController(Context context) {
        SampleManagerDBOpenHelper dbOpenHelper=new SampleManagerDBOpenHelper(context,DB_NAME,null,VERSION);
        db=dbOpenHelper.getWritableDatabase();//在调用{@link #getWritableDatabase}或{@link #getReadableDatabase}中的一个之前，实际上不会创建或打开数据库。
    }

    public void dbClose(){
        if (db != null) {
            db.close();
        }
    }

    // ============ 使用Api实现增删改查===============
    public boolean insertSample(long id, String message, long time){
        SampleManagerDBModel dbModel=new SampleManagerDBModel();
        if (id>0){
            dbModel.id=id;
        }
        dbModel.message=message;
        dbModel.time=time;
        final boolean success=db.insert(TABLE_NAME,null,dbModel.toContentValues())!=-1;//insert方法返回新插入行的行id，发生错误贼返回-1
        dbClose();
        return success;
    }

    //一次执行大量数据插入，频繁开启关闭SQLiteDatabase对象浪费内存,不如手动关
    public boolean insertSampleNew(long id, String message, long time){
        SampleManagerDBModel dbModel=new SampleManagerDBModel();
        if (id>0){
            dbModel.id=id;
        }
        dbModel.message=message;
        dbModel.time=time;
        final boolean success=db.insert(TABLE_NAME,null,dbModel.toContentValues())!=-1;//insert方法返回新插入行的行id，发生错误贼返回-1
//        dbClose();
        return success;
    }

    public boolean deleteSample(String whereClause,String[] whereArgs){
        boolean sucess = db.delete(TABLE_NAME, whereClause,whereArgs) != -1;//db.delete方法的whereClause传null就是删除全部，一般传某参数=?（比如id = ?）
        dbClose();
        return sucess;
    }

    public boolean updateSample(long id,String message,long time){
        SampleManagerDBModel dbModel=new SampleManagerDBModel();
        if(id >= 0) {
            dbModel.id = id;
        }
        dbModel.message = message;
        dbModel.time = time;
        boolean success = db.update(TABLE_NAME, dbModel.toContentValues(), "_id = ?", new String[]{String.valueOf(id)}) != -1;
        dbClose();
        return success;
    }

    /**
     * @param cloums 要查询的字段
     * @param selection 查询条件
     * @param selectionArgs 填充查询条件的值
     * @return
     */
    public List<Sample> querySample(String[] cloums, String selection, String[]  selectionArgs){
           try{
               Cursor c = db.query(TABLE_NAME, cloums, selection, selectionArgs, null, null, null);
               ArrayList<Sample> arrayList = new ArrayList<>();
               if (!c.moveToLast()) {//Cursor如果是空，moveToLast返回false
                   return arrayList;
               }
               do {
                   Sample model = new Sample();
                   model.setId(c.getLong(c.getColumnIndexOrThrow(SampleManagerDBModel.ID)));
                   model.setTime(c.getLong(c.getColumnIndexOrThrow(SampleManagerDBModel.TIME)));
                   model.setMessage(c.getString(c.getColumnIndexOrThrow(SampleManagerDBModel.MESSAGE)));
                   arrayList.add(model);
               } while (c.moveToPrevious());//如果游标已经在结果集中的第一个条目之前，则此方法将返回false。
               c.close();
               return arrayList;
           }
           catch (Exception e){
               e.printStackTrace();
           }
           finally {
               dbClose();
           }
           return new ArrayList<>();
    }
    // ============ 使用Api实现增删改查END===============

    // =============sql语句 增删改查===============
    public void insertRaw(String message, long time){
        String sql = " insert into " + TABLE_NAME + "(message,time) values(?,?)";
        Object[] args = {message,time};
        db.execSQL(sql,args);
        dbClose();
    }

    //一次执行大量数据插入，频繁开启关闭SQLiteDatabase对象浪费内存
    public void insertRawNew(String message, long time){
        String sql = " insert into " + TABLE_NAME + "(message,time) values(?,?)";
        Object[] args = {message,time};
        db.execSQL(sql,args);
//        dbClose();
    }

    public void deleteRaw(String message){
        String sql = "delete from "+TABLE_NAME + " where message = ?";
        Object[] args = {message};
        db.execSQL(sql,args);
        dbClose();
    }

    public void updateRaw(long id,String message,long time){
        String sql = "update "+TABLE_NAME + " set message = ?,time = ? where _id = ?";
        Object[] args = {message,time,id};
        db.execSQL(sql,args);
        dbClose();
    }

    public List<Sample> queryRawById(String  id){
        String sql = "select _id,message,time from "+TABLE_NAME + " where _id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        ArrayList<Sample> arrayList = new ArrayList<>();
        if (!cursor.moveToLast()) {
            return arrayList;
        }
        do {
            Sample model = new Sample();
            model.setId((long)cursor.getInt(cursor.getColumnIndex(SampleManagerDBModel.ID)));
            model.setTime(cursor.getLong(cursor.getColumnIndex(SampleManagerDBModel.TIME)));
            model.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(SampleManagerDBModel.MESSAGE)));
            arrayList.add(model);
        } while (cursor.moveToPrevious());
        cursor.close();
        dbClose();
        return arrayList;
    }

    public List<Sample> queryAll(){
        try {
            final Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            ArrayList<Sample> arrayList = new ArrayList<>();
            if (!c.moveToLast()) {
                return arrayList;
            }
            do {
                Sample model = new Sample();
                model.setId((long)c.getInt(c.getColumnIndex(SampleManagerDBModel.ID)));
                model.setTime(c.getLong(c.getColumnIndex(SampleManagerDBModel.TIME)));
                model.setMessage(c.getString(c.getColumnIndexOrThrow(SampleManagerDBModel.MESSAGE)));
                arrayList.add(model);
            } while (c.moveToPrevious());//将光标移到前一行
            c.close();
            dbClose();
            return arrayList;
        } catch (Exception e){
            e.printStackTrace();
            dbClose();
        }
        return null;
    }

    // =============sql语句 增删改查END===============
}
