package com.common.net.response;

/**
 *@author: cjl
 *@date: 2019/5/6 10:45
 *@desc:
 */
public class BaseResponse<T> {


    /**
     * status : 10000
     * message : sign不能为空
     * data : {}
     */

    public int status;
    public String   message;
    public T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" + "status=" + status + ", message='" + message + '\'' + ", data=" + data + '}';
    }
}
