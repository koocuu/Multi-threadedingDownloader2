package com.example.multi_threadeddownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class DownloadOperation {
    private URL url;
    private String filePath;
    private int threadCount;
    private DownloadThread[] tasks;
    private long size;

    public DownloadOperation(String urlString, String filePath, int threadCount) throws MalformedURLException {
        try {
            this.url = new URL(URLEncoder.encode(urlString,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.filePath = filePath;
        this.threadCount = threadCount;
        tasks = new DownloadThread[threadCount];
    }
    public void Download(){
        //获取文件大小
        getFileSize();
    }
    private void getFileSize(){
        //url
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("HEAD");
            //获取资源大小
            size = conn.getContentLengthLong();

            //创建文件，以保存下载数据
            creatFile();

            //分配线程下载数据
            disPatch();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            conn.disconnect();
        }
    }

    private void disPatch() {
        //每个线程下载的平均大小
        long avrage = size/threadCount;
        long start = 0;
        long downloadSize = avrage;
        for (int i = 0;i<threadCount;i++){
            start = i*avrage;
            //最后一个线程
            if (i == threadCount -1){
                downloadSize = size - start;
            }
            DownloadThread dt = new DownloadThread(url,filePath,start,downloadSize);
            //保存这个线程对象
            tasks[i] = dt;
            dt.start();
        }
    }

    public float currentRate(){
        long len = 0;
        for (DownloadThread dt:tasks){
            len += dt.downloadedLength;
        }
        return (float) len / size;
    }
    private void creatFile() {
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置文件大小
        RandomAccessFile rac = null;
        try {
            rac = new RandomAccessFile(file,"rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            rac.setLength(size);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                rac.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
