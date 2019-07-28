package com.phz.databasedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.phz.databasedemo.bean.Sample;
import com.phz.databasedemo.rv.adapters.DataBaseAdapter;
import com.phz.databasedemo.rv.interfaces.ItemBackListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class DataBaseBaseActivity extends AppCompatActivity {
    @BindView(R.id.database_submit_et)
    EditText database_submit_et;//点击删除，长摁修改
    @BindView(R.id.database_search_et)
    EditText database_search_et;//查询的id
    @BindViews({R.id.database_submit_btn,R.id.database_search_btn,R.id.database_delete_btn,R.id.database_show_btn})
    List<Button> database_list_btn;
    @BindView(R.id.database_recycleview)
    RecyclerView database_recycleview;
    @BindView(R.id.database_checkBox)
    CheckBox database_checkBox;

    public boolean isSQL;//是否使用SQL语句方式
    private Unbinder unbinder;
    public DataBaseAdapter mDataBaseAdapter;
    public LinearLayoutManager mLinearLayoutManager;
    public List<Sample> samples = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        unbinder= ButterKnife.bind(this);
        initDataBase();
        initView();
    }

    private void initView() {
        mDataBaseAdapter = new DataBaseAdapter(samples, this, new ItemBackListener() {
            @Override
            public void onItemClick(Sample s) {
                deleteData(s);
            }

            @Override
            public void onItemLongClick(Sample s) {
                String message = s.getMessage();
                if (message.startsWith("已经")) {
                    CharSequence sequence = message.subSequence(6, message.length());
                    String toString = sequence.toString();
                    s.setMessage(toString);
                } else {
                    s.setMessage("已经被修改了" + s.getMessage());
                }
                s.setTime(System.currentTimeMillis());
                updataData(s);
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        database_recycleview.setLayoutManager(mLinearLayoutManager);
        database_recycleview.setAdapter(mDataBaseAdapter);
    }
    @OnClick({R.id.database_submit_btn,R.id.database_search_btn,R.id.database_delete_btn,R.id.database_show_btn})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.database_submit_btn:
                String content=database_submit_et.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    insertData(null);
                } else {
                    insertData(new Sample(content, System.currentTimeMillis()));
                }
                break;
            case R.id.database_search_btn:
                queryData(database_search_et.getText().toString().trim());
                break;
            case R.id.database_delete_btn:
                deleteAll();
                break;
            case R.id.database_show_btn:
            if (mDataBaseAdapter != null) {
                mDataBaseAdapter.refreshData(isSQL);
            }
            break;
        }
    }

    @OnCheckedChanged(R.id.database_checkBox)
    public void OnViewCheckedChanged(CompoundButton view){
        switch (view.getId()){
            case R.id.database_checkBox:
                if (view.isChecked()){
                    isSQL=true;
                    Log.e("isSQL","改变了");
                }
                else {
                    isSQL=false;
                    Log.e("isSQL","改变了");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void insertData(Sample s) {
        mDataBaseAdapter.addSampleData(s);
    }

    public abstract void deleteAll();

    public abstract void deleteData(Sample s);

    public abstract void updataData(Sample s);

    public abstract void queryData(String s);

    public abstract void initDataBase();
}
