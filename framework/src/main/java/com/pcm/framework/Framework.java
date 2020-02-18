package com.pcm.framework;

import android.app.Application;

import com.pcm.framework.util.LogUtils;
import com.pcm.framework.util.SpUtils;


public class Framework {
    private static volatile Framework framework;
    private Framework(){}
    public static Framework getFramework(){
        if(framework == null) {
            synchronized (Framework.class){
                if(framework == null){
                    framework = new Framework();
                }
            }
        }
        return framework;
    }
    public void init(Application application){
        SpUtils.getInstance().initSp(application);
        LogUtils.getLogger().setLogPath(application.getExternalFilesDir(null).getAbsolutePath());
    }

    private void initData() {

    }


}
