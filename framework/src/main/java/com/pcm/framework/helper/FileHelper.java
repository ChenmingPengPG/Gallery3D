package com.pcm.framework.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;


import com.pcm.framework.util.LogUtils;
import com.pcm.library.ImageSelector;
import com.pcm.library.bean.MediaSelectConfig;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileHelper {
    public static final int CAMERA_REQUEST_CODE = 1004;
    public static final int ALBUM_REQUEST_CODE = 1005;
    private static volatile FileHelper mInstance = null;
    private FileHelper(){
        sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    }
    public static FileHelper getInstance(){
        if(mInstance == null){
            synchronized (FileHelper.class){
                if(mInstance == null){
                    mInstance = new FileHelper();
                }
            }
        }
        return mInstance;
    }
    private SimpleDateFormat sdf;

    public Uri getImageUri() {
        return imageUri;
    }

    private Uri imageUri;
    private File tmpFile = null;

    public void toCamera(Activity mActivity){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = sdf.format(new Date());
        tmpFile = new File(mActivity.getExternalFilesDir(null), fileName+".jpg");
        //兼容android N
        if(Build.VERSION.SDK_INT  < Build.VERSION_CODES.N){
            imageUri = Uri.fromFile(tmpFile);
        }else{
            //利用FileProvider
            imageUri = FileProvider.getUriForFile(mActivity,
                    mActivity.getPackageName() + ".fileprovider", tmpFile);
            //添加权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        }
        LogUtils.getLogger().i("imageUri:" + imageUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        mActivity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void toAlbum(Activity mActivity){
        MediaSelectConfig config = new MediaSelectConfig()
                .setSelectMode(MediaSelectConfig.MODE_MULTI) //设置选择图片模式，单选与多选
//                .setOriginData() //已选择图片地址
                .setShowCamera(true) //是否展示打开摄像头拍照入口，只针对照片，视频列表无效
                .setOpenCameraOnly(false) //是否只是打开摄像头拍照而已
                .setMaxCount(30) //选择最大集合，默认9
                .setImageSpanCount(3) //自定义列表展示个数，默认3
                ;

        //打开照片列表
        ImageSelector.create()
                .setMediaConfig(config)
                .startImageAction(mActivity, ALBUM_REQUEST_CODE);
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        mActivity.startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }
    public File getTmpFile() {
        return tmpFile;
    }

    /**
     * 通过Uri去提醒查询真实地址
     * @param uri
     */
    public String getRealPathFromUri(Uri uri, Context mContext){
        String realPath = "";

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(mContext, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        if (cursor != null) {
            int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            realPath = cursor.getString(index);
        }

        return realPath;
    }

    public void copyFileToExternal(Context context,String fileName) throws IOException {
        InputStream in = context.getAssets().open(fileName);
        File outFile = new File(context.getExternalFilesDir(null), fileName);
        FileOutputStream fos = new FileOutputStream(outFile);
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            fos.write(buffer, 0, read);
        }
        in.close();
        fos.flush();
        fos.close();
    }


}
