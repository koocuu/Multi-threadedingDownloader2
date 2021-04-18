package com.example.multi_threadeddownloader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadThread extends Thread{
    private URL url;
    private String filePath;
    private long startPosition;
    private long size;
    public long downloadedLength;

    public DownloadThread(URL url, String filePath, long startPosition, long size) {
        this.url = url;
        this.filePath = filePath;
        this.startPosition = startPosition;
        this.size = size;
    }

    @Override
    public void run() {
        //定位文件到线程该写入的位置
        try {
            RandomAccessFile raf = new RandomAccessFile(filePath,"rw");
            raf.seek(startPosition);

            //开始下载
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setDoInput(true);
            coon.setConnectTimeout(5*1000);

            //获取输入流
            InputStream is = url.openStream();
            is.skip(startPosition);

            //开始读取数据写入文件
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1){
                raf.write(buffer,0,len);
                downloadedLength += len;

                //判断当前线程下载是否结束
                if (downloadedLength > size){
                    break;
                }
            }

            is.close();
            coon.disconnect();
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
