
package com.phz.databasedemo.rv.interfaces;


import com.phz.databasedemo.bean.Sample;

public interface ItemBackListener{
        void onItemClick(Sample s);
        void onItemLongClick(Sample s);
    }