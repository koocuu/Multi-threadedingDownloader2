package com.example.multi_threadeddownloader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.net.MalformedURLException;

public class MainActivity {

    public static void main(String[] args) {
        //网络资源位置
        String url = "xxxx";
        //下载目录
        String path = "xxxxx";
        try {
            DownloadManager.getInstance().loadData(url,path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}