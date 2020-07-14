package com.common.data;

/**
 *@author: cjl
 *@date: 2020/6/8 14:46
 *@desc:
 */
public class BaseObservableData<T> {
    private int type;
    private T data;

    public BaseObservableData(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public BaseObservableData setType(int type) {
        this.type = type;
        return this;
    }

    public T getData() {
        return data;
    }

    public BaseObservableData setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "BaseObservableData{" + "type=" + type + ", data=" + data + '}';
    }
}
