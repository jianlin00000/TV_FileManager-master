package com.common.net.callback;

import com.common.net.response.BaseResponse;
import com.common.utils.LogUtil;


/**
 * @author: cjl
 * @desc:
 */
public abstract class RxObserver<T> extends RxBaseObserver<T> {

    public static final int REQUEST_SUCCESS   = 1; //请求成功


    public RxObserver() {
        super();
    }

    @Override
    public void onNext(BaseResponse<T> baseResponse) {
        int code = baseResponse.getStatus();
        //请求成功
        if (code== REQUEST_SUCCESS) {
          if (baseResponse!=null){
              onSuccess(baseResponse.getData());
          }else {
              LogUtil.e("baseResponse=null");
          }
          return;
        }
        String errorMsg=baseResponse.getMessage();
        onError(code,errorMsg);
    }

    @Override
    protected void onError(int errorCode,String errorMsg) {
        onFail(errorCode, errorMsg);

    }

    protected abstract void onSuccess(T data);

    protected abstract void onFail(int errorCode, String errorMsg);

}
