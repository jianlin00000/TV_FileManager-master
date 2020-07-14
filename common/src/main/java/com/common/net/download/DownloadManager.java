package com.common.net.download;

import android.util.Log;

import com.common.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author: cjl
 * @date: 2019/4/14
 * @desc: 下载管理器
 */
public class DownloadManager {

    public static final String TAG=DownloadManager.class.getSimpleName();

    DownloadListener mDownloadListener=null;
    private              ApiService                  mApiService;
    //记录下载队列，key为文件url
    private              Map<String, FileSubscriber> mSubMap;
    //最大下载任务数量
    private static final int                         TASK_MAX_COUNT=3;

    private DownloadManager() {
        mSubMap=new ConcurrentHashMap<>();
    }

    public static  DownloadManager getInstance(){
        return Holder.INSTANCE;
    }

    private static class Holder{
        private static DownloadManager INSTANCE = new DownloadManager();
    }

    /**
     * 开始下载
     * @param url
     * @param file 存储文件
     * @param listener
     */
    public void startDownload(@NonNull String url, File file, DownloadListener listener){
        if (isDownloading(url)){//文件正在下载
            Log.e(TAG,"文件正在下载.....");
            return;
        }

        mDownloadListener=listener;
        FileSubscriber subscriber=new FileSubscriber(url,file,listener);
        mSubMap.put(url,subscriber);
        DownLoadInterceptor interceptor = new DownLoadInterceptor(listener);
        mApiService = getApiService(interceptor);
        mApiService.downloadFile(url)
                   .subscribeOn(Schedulers.io())
                   .unsubscribeOn(Schedulers.io())
                   .map(responseBody -> responseBody.byteStream())
                   .observeOn(Schedulers.computation())
                   .doOnNext(inputStream -> writeCaches(inputStream,file,subscriber))
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe((Observer<? super InputStream>) subscriber);
    }

    /**
     * 移除当前的下载记录
     * @param url
     */
    public void removeRecord(String url){
        if (mSubMap.containsKey(url)){
            mSubMap.remove(url);
        }
    }


    /**
     * 指定的链接文件是否正在下载
     * @param url
     * @return
     */
    public boolean isDownloading(String url){
        return mSubMap.containsKey(url);
    }

    /**
     * 停止下载
     * @param url
     */
    public void stopDownload(@NonNull String url){
        LogUtil.d("");
        if (isDownloading(url)){
            FileSubscriber subscriber = mSubMap.get(url);
            if (subscriber!=null){
                subscriber.disposable();
                mSubMap.remove(url);
            }
        }
    }

    /**
     * 停止全部下载
     */
    public void stopAllDown() {
        LogUtil.d("");
        if (mSubMap!=null&&mSubMap.size()==0) return;
        Set<String> set = mSubMap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            String url = iterator.next();
            FileSubscriber subscriber = mSubMap.get(url);
            if (subscriber!=null){
                subscriber.disposable();
            }
        }
        mSubMap.clear();
    }

    private ApiService getApiService(DownLoadInterceptor interceptor) {
        ApiService apiService;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                                                        .retryOnConnectionFailure(true)
                                                        .connectTimeout(20, TimeUnit.SECONDS)
                                                        .readTimeout(20,TimeUnit.SECONDS)
                                                        .addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://www.baidu.com/")
                .build();
        apiService = retrofit.create(ApiService.class);
        return apiService;
    }


    private void writeCaches(InputStream inputStream, File file,FileSubscriber subscriber) {
        //判断本地存储空间是否充足
        if (file!=null){
            long freeSpace = file.getUsableSpace();
            if (freeSpace<file.length()){
                if (mDownloadListener!=null){
                    mDownloadListener.onSpaceNotEnought(freeSpace,file.length());
                    return;
                }
            }
        }
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024*3];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                fos.write(b,0,len);
            }
            inputStream.close();
            fos.close();
        } catch (FileNotFoundException e) {
            LogUtil.e("FileNotFoundException");
            mDownloadListener.onFailed("FileNotFoundException");
        } catch (IOException e) {
            LogUtil.e("download--IOException:"+e.getMessage());
            subscriber.onError(e);
        }finally{
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.e("download--IOException"+e.getMessage());
                }
            }
        }
    }


}
