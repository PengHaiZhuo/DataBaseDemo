package com.phz.databasedemo;


import android.widget.Toast;

import com.phz.databasedemo.bean.Sample;
import com.phz.databasedemo.bean.SampleDBController;

import java.util.List;

import static com.phz.databasedemo.bean.SampleDBController.DB_NAME;

public class DataBaseActivity extends DataBaseBaseActivity {
    private SampleDBController dbController;

    @Override
    public void deleteAll() {
        deleteDatabase(DB_NAME);
        if (mDataBaseAdapter != null) {
            mDataBaseAdapter.refreshData(isSQL);
        }
    }

    @Override
    public void deleteData(Sample s) {
        dbController=new SampleDBController(this);
        if (isSQL){
            dbController.deleteRaw(s.getMessage());
        }else {
            if (!dbController.deleteSample("_id = ?", new String[]{String.valueOf(s.getId())})) {
                Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void updataData(Sample s) {
        dbController=new SampleDBController(this);
        if (isSQL){
            dbController.updateRaw(s.getId(),s.getMessage(),s.getTime());
        }else {
            dbController.updateSample(s.getId(),s.getMessage(),s.getTime());
        }
        if (mDataBaseAdapter != null) {
            mDataBaseAdapter.refreshData(isSQL);
        }
    }

    @Override
    public void queryData(String s) {
        List<Sample> sampleList;
        dbController=new SampleDBController(this);
        if (isSQL){
            sampleList=dbController.queryRawById(s);
        }else {
            sampleList=dbController.querySample(new String[]{"_id", "time", "message"}, "_id = ?", new String[]{s});
        }
        if (sampleList != null && sampleList.size() == 0) {
            Toast.makeText(this, "没有查到相关内容", Toast.LENGTH_SHORT).show();
        }
        mDataBaseAdapter.addNewSampleData(sampleList);
    }

    @Override
    public void initDataBase() {
        dbController=new SampleDBController(this);
        if (isSQL){
            samples=dbController.queryAll();
        }else {
            samples=dbController.querySample(null,null,null);
        }
    }

    @Override
    public void insertData(Sample s) {
        dbController=new SampleDBController(this);//为什么每个方法需要调用新建呢，因为每次调用都关闭了SQLiteDatabase。题外话：此外多线程操作操作数据库报错。
        if (s == null) {
            for (int i = 0; i < 1000; i++) {
                String content = "第" + i + "个数据";
                if (isSQL){
                    dbController.insertRawNew(content,System.currentTimeMillis() - 60 * 1000 * i);//
                }
                else {
                    dbController.insertSampleNew(-1, content, System.currentTimeMillis() - 60 * 1000 * i);
                }
            }
            dbController.dbClose();
        }
        else {
            if (isSQL){
                dbController.insertRaw(s.getMessage(),s.getTime());
            }else {
                dbController.insertSample(-1,s.getMessage(),s.getTime());
            }
        }
        if (mDataBaseAdapter != null) {
            mDataBaseAdapter.refreshData(isSQL);
        }
    }
}
