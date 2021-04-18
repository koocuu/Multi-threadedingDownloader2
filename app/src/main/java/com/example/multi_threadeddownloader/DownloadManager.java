package com.example.multi_threadeddownloader;

import java.net.MalformedURLException;
import java.util.Map;

public class DownloadManager {
    private DownloadManager() {

    }
    private Map<String,String>[] source;
    private static DownloadManager manager;
    public static DownloadManager getInstance(){
        if (manager == null){
            synchronized (new Object()){
                manager = new DownloadManager();
            }
        }
        return manager;
    }

    public void loadData(String urlString,String filePath) throws MalformedURLException {
        DownloadOperation downloader = new DownloadOperation(urlString,filePath,3);
        downloader.Download();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    float rate = downloader.currentRate();
                    if (rate < 1.000001){
                    System.out.println("当前下载比例："+downloader.currentRate());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }}else {
                        break;
                    }

                }

            }
        }).start();
    }

    public void loadData(Map<String,String>[] datas){}

}
