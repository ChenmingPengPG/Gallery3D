package com.pcm.library.utils;

import android.text.TextUtils;
import android.util.Log;


import com.pcm.library.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {
    private static LogUtils logger;
    private LogUtils(){

    }
    public static LogUtils getLogger(){
        if(logger == null){
            synchronized (LogUtils.class){
                if(logger == null){
                    logger = new LogUtils();
                }
            }
        }
        return logger;
    }
    private String rootPath = "/storage/emulated/0/Android/data/com.pcm.fun214";
    private final String fileName = "Meet.log";
    private static SimpleDateFormat mSimpleDateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void setLogPath(String rootPath){
        this.rootPath = rootPath;
    }
    public void i(String text) {
        if (BuildConfig.LOG_DEBUG) {
            if (!TextUtils.isEmpty(text)) {
                Log.i(BuildConfig.LOG_TAG, text);
                writeToFile(text);
            }
        }
    }

    public void e(String text) {
        if (BuildConfig.LOG_DEBUG) {
            if (!TextUtils.isEmpty(text)) {
                Log.e(BuildConfig.LOG_TAG, text);
                writeToFile(text);
            }
        }
    }

    /**
     * 写入内存卡
     *
     * @param text
     */
    public  void writeToFile(String text) {
        //开始写入
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {

            // 时间 + 内容
            String log = mSimpleDateFormat.format(new Date()) + " " + text + "\n";
            //检查父路径
            File file = new File(rootPath);
            if(!file.exists()){
                file.mkdirs();
            }
            //创建log文件
            file = new File(rootPath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            fileOutputStream = new FileOutputStream(rootPath + fileName, true);
            //编码问题 GBK 正确的存入中文
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, Charset.forName("gbk")));
            bufferedWriter.write(log);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            e(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            e(e.toString());
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
