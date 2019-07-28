package com.phz.databasedemo.rv.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.phz.databasedemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_holder_content_tv)
    public TextView mTextViewHolderContentTv;
    @BindView(R.id.text_view_holder_time_tv)
    public TextView mTextViewHolderTimeTv;

    public TextViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

}
