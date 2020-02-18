package com.pcm.fun214.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;

import com.pcm.framework.Framework;
import com.pcm.framework.entity.Constants;
import com.pcm.framework.helper.DBHelper;
import com.pcm.framework.helper.FileHelper;
import com.pcm.framework.util.LogUtils;
import com.pcm.framework.util.SpUtils;
import com.pcm.fun214.BuildConfig;
import com.pcm.fun214.R;
import com.pcm.framework.data.MyData;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (getApplicationInfo().packageName.equals(
                getCurProcessName(getApplicationContext()))) {
            //获取渠道
            //String flavor = FlavorHelper.getFlavor(this);
            //Toast.makeText(this, "flavor:" + flavor, Toast.LENGTH_SHORT).show();
            /**
             * 各种初始化
             */
            Framework.getFramework().init(this);
        }
        boolean isFirstApp = SpUtils.getInstance().getBoolean(Constants.SP_IS_FIRST_APP, true);
        if(isFirstApp){
            initData();
            SpUtils.getInstance().putBoolean(Constants.SP_IS_FIRST_APP, false);
        }else{
            DBHelper.getDbHelper(this).queryAndGetToMyData();
        }
    }
    /**
     * 找到drawable中需要加载的pics
     */
    private void initData() {
//        Field[] fields = R.drawable.class.getDeclaredFields();
//        for(Field field:fields){
//    /*获取文件名对应的系统生成的id
//    需指定包路径 getClass().getPackage().getName()
//    指定资源类型drawable*/
//            int resID = getResources().getIdentifier(field.getName(),
//                    "drawable", getClass().getPackage().getName());
//            if(field.getName().startsWith("img_")){
//                String uri = "android.resource://"+ BuildConfig.APPLICATION_ID + "/drawable/" + field.getName();
//                String dbTitle;
//                String name = field.getName().substring(0,11);
//                if (MyData.getMyData().datamap.containsKey(name)) {
//                    dbTitle = MyData.getMyData().datamap.get(name);
//                }else{
//                    dbTitle = "";
//                }
//                MyData.getMyData().addData(uri, field.getName(), dbTitle, "");
//                DBHelper.getDbHelper(this).insert(uri, field.getName(), dbTitle, "");
//                LogUtils.getLogger().setLogPath(getExternalFilesDir(null).getAbsolutePath());
//                LogUtils.getLogger().i("picName:" + field.getName() + " resID:" + resID);
//            }
//        }
        try {
            String[] list = getResources().getAssets().list("");
            for(String l : list){
                if(l.endsWith("jpg") || l.endsWith("png")){
                    LogUtils.getLogger().i("name:" + l);
                    FileHelper.getInstance().copyFileToExternal(this,l);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        File[] files = getExternalFilesDir(null).listFiles();
        for(File file: files){
            if(!file.isDirectory()){
                String name = file.getName().substring(0,12);
                LogUtils.getLogger().i("name:" + name);
                String uri = Uri.fromFile(file).toString();
                String dbTitle = "";
                if (MyData.getMyData().datamap.containsKey(name)) {
                    dbTitle = MyData.getMyData().datamap.get(name);
                }
                MyData.getMyData().addData(uri, file.getAbsolutePath(), name, dbTitle, "");
                DBHelper.getDbHelper(this).insert(uri, file.getAbsolutePath(), name, dbTitle, "");
            }
        }
    }



    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess :
                activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
