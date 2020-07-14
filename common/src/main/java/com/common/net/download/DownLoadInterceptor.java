package com.common.net.download;

import com.common.utils.LogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author: cjl
 * @date: 2019/4/14 0014 21
 * @desc:
 */
public class DownLoadInterceptor implements Interceptor {

    private DownloadListener mDownloadListener=null;

    public DownLoadInterceptor(DownloadListener downloadListener) {
        mDownloadListener = downloadListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        int responseCode = originalResponse.code();
        ResponseBody body = originalResponse.body();
        MediaType mediaType = body.contentType();
        LogUtil.d("responseCode:"+responseCode+",mediaType:"+mediaType);
        if (responseCode!=200){
            return originalResponse.newBuilder()
                                   .body(body)
                                   .build();
        }
        return originalResponse.newBuilder()
                               .body(new DownloadResponseBody(body,mDownloadListener))
                               .build();
    }
}
