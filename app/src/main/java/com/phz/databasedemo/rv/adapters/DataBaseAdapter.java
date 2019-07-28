package com.phz.databasedemo.rv.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phz.databasedemo.R;
import com.phz.databasedemo.bean.Sample;
import com.phz.databasedemo.bean.SampleDBController;
import com.phz.databasedemo.rv.interfaces.ItemBackListener;
import com.phz.databasedemo.rv.viewHolder.TextViewHolder;

import java.util.ArrayList;
import java.util.List;


public class DataBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Sample> mSampleList = new ArrayList<>();
    private Context mContext;
    private ItemBackListener mItemBackListener;

    public DataBaseAdapter(List<Sample> mSampleList, Context mContext,ItemBackListener itemBackListener) {
        this.mSampleList = mSampleList;
        this.mContext = mContext;
        mItemBackListener = itemBackListener;
    }

    public void addSampleData(Sample data) {
        if (data != null) {
            mSampleList.add(data);
        }
        notifyDataSetChanged();
    }

    public void addNewSampleData(List<Sample> data){
        mSampleList.clear();
        mSampleList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 显示全部
     * @param flag
     */
    public void refreshData(boolean flag){
        SampleDBController thingDBController = new SampleDBController(mContext);
        if (flag){
            mSampleList = thingDBController.queryAll();
        }else {
            mSampleList=thingDBController.querySample(null,null,null);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.text_view_holder, viewGroup, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        if(mSampleList.size() <= position ){
            return;
        }
        if(viewHolder instanceof TextViewHolder){
            final Sample sample = mSampleList.get(position);
            ((TextViewHolder) viewHolder).mTextViewHolderContentTv.setText(sample.getMessage() + " Id 为：" + String.valueOf(sample.getId()));
            ((TextViewHolder) viewHolder).mTextViewHolderTimeTv.setText(sample.getTime()+"");
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSampleList.remove(sample);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,mSampleList.size() - position);
                    mItemBackListener.onItemClick(sample);
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemBackListener.onItemLongClick(sample);
                    return true;//默认返回false，会继续响应其他监听中的事件（返回true，那么长按监听只执行长按监听中执行的代码）
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if (mSampleList != null) {
            ret = mSampleList.size();
        }
        return ret;
    }
}
