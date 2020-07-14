package com.common.net.download;

import java.io.File;

/**
 * @author: cjl
 * @date: 2019/4/14 0014 21
 * @desc: 下载监听
 */
public class DownloadListener {

    /**
     * 开始下载
     */
    public void onStart(){};

    /**
     * 下载进度
     * @param readByte  下载字节
     * @param totalByte 总字节
     * @param isDone true--下载完成
     */
    public void onProgress(long readByte,long totalByte,boolean isDone){};

    /**
     * 下载失败
     * @param msg 失败原因
     */
    public void onFailed(String msg){};
    /**
     * 空间不足
     */
    public void onSpaceNotEnought(long availableSpace,long requestSpace){};

    /**
     * 下载完成
     * @param url 下载url
     * @param file 下载完成的文件
     */
    public void onComplete(String url, File file){};


}
