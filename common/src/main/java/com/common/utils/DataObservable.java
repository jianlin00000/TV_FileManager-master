package com.common.utils;

import com.common.data.BaseObservableData;

import java.util.Observable;

/**
 *@author: cjl
 *@date: 2019/6/20 17:44
 *@desc:
 */
public class DataObservable
        extends Observable {
    private BaseObservableData data =null;

    private DataObservable() {}

    private static class Holder{
        private static DataObservable INSTANCE=new DataObservable();
    }

    public static DataObservable getInstance() {
        return Holder.INSTANCE;
    }

    public void setData(BaseObservableData data) {
        this.data = data;
        this.setChanged();
        this.notifyObservers(data);
    }

    public BaseObservableData getData() {
        return this.data;
    }
}