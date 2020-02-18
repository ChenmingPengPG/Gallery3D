package com.pcm.framework.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pcm.framework.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    public LogUtils logger;

    //申请运行时权限的Code
    private static final int PERMISSION_REQUEST_CODE = 1000;
    //申明所需权限
    private String[] mStrPermission = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS
    };

    //存放还未同意的权限
    private List<String> mPerList = new ArrayList<>();

    //保存同意失败权限
    private List<String> mPerNoList = new ArrayList<>();

    private OnPermissionsResult permissionsResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logger = LogUtils.getLogger();
    }

    protected void request(OnPermissionsResult onPermissionsResult){
        if(!checkPermissionAll()){
            requestPermissionAll(permissionsResult);
        }
    }

    /**
     * 检查权限
     * @param permissions
     * @return
     */
    protected boolean checkPermissions(String permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int check = checkSelfPermission(permissions);
            return check == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    /**
     * 请求权限
     * @param mPermissions
     */
    protected void requestPermission(String[] mPermissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mPermissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPerList.clear();
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0){
                for(int i = 0; i < grantResults.length; i++){
                    //失败的权限
                    mPerNoList.add(permissions[i]);
                }
            }
            if(permissionsResult != null){
                if(mPerNoList.size() == 0){
                    permissionsResult.OnSuccess();
                }else{
                    permissionsResult.OnFail(mPerNoList);
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected boolean checkPermissionAll(){
        for(int i = 0; i < mStrPermission.length; i++){
            boolean check = checkPermissions(mStrPermission[i]);
            if(!check){
                mPerList.add(mStrPermission[i]);
            }
        }
        return mPerList.size() <= 0;
    }

    protected void requestPermissionAll(OnPermissionsResult permissionsResult){
        this.permissionsResult = permissionsResult;
        requestPermission((String[]) mPerList.toArray(new String[mPerList.size()]));
    }


    protected interface OnPermissionsResult {
        void OnSuccess();

        void OnFail(List<String> noPermissions);
    }
}
