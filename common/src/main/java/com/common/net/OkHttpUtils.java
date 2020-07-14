package com.common.net;

import com.common.utils.JsonUtil;
import com.common.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpUtils {

    private static final HttpLoggingInterceptor.Level LOG_LEVEL= HttpLoggingInterceptor.Level.BODY ;

    public static OkHttpClient.Builder createOkHttpBuilder(long timeoutMS) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(LOG_LEVEL);
        return new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .readTimeout(timeoutMS, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMS, TimeUnit.MILLISECONDS)
                .connectTimeout(timeoutMS, TimeUnit.MILLISECONDS);
    }

    public static OkHttpClient.Builder createOkHttpBuilder(long timeoutMS,Interceptor headInterceptor) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(LOG_LEVEL);
        return new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addInterceptor(headInterceptor)
                .readTimeout(timeoutMS, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMS, TimeUnit.MILLISECONDS)
                .connectTimeout(timeoutMS, TimeUnit.MILLISECONDS);
    }


    public static class HttpLogger implements HttpLoggingInterceptor.Logger {
        private StringBuilder mMessage = new StringBuilder();
        @Override
        public void log(String message) {
            // 请求或者响应开始
            if (message.startsWith("--> POST") || message.startsWith("--> GET") ) {
                mMessage.setLength(0);
            }
            // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
            if ((message.startsWith("{") && message.endsWith("}"))
                    || (message.startsWith("[") && message.endsWith("]"))) {
                message = JsonUtil.formatJson(JsonUtil.decodeUnicode(message));
                mMessage.append(message.concat("\n"));
            } else if (message.startsWith("<--") && message.contains("http://")) {
                mMessage.append(message.concat("\n"));
            }
            // 响应结束，打印整条日志
            if (message.startsWith("<-- END HTTP")) {
                LogUtil.d(mMessage.toString());
            }
        }
    }


}
