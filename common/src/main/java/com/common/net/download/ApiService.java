package com.common.net.download;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 *@author: cjl
 *@date: 2019/5/9 09:42
 *@desc:
 */
public interface ApiService {


    /**
     * 文件下载
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);
}
