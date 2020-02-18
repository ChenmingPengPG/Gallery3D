package com.pcm.fun214;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.moxun.tagcloudlib.view.TagCloudView;
import com.pcm.framework.base.BaseUIActivity;
import com.pcm.framework.entity.Constants;
import com.pcm.framework.helper.DBHelper;
import com.pcm.framework.helper.FileHelper;
import com.pcm.framework.manager.DialogManager;
import com.pcm.framework.util.LogUtils;
import com.pcm.framework.util.SpUtils;
import com.pcm.framework.view.DialogView;
import com.pcm.fun214.adapter.CloudTagAdapter;
import com.pcm.framework.data.MyData;
import com.pcm.fun214.ui.MoreInfoActivity;
import com.pcm.library.ImageSelector;


import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.pcm.framework.helper.FileHelper.ALBUM_REQUEST_CODE;

public class MainActivity extends BaseUIActivity implements View.OnClickListener{
    private TagCloudView tcv;
    private CloudTagAdapter mCloudTagAdapter;
    private Button addPicsButton;
    private DialogView mPhotoSelectView;
    private TextView tv_camera;
    private TextView tv_ablum;
    private TextView tv_cancel;
    private final int MORE_INFO_CALL = 999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermissions();

        initView();
    }

    private void requestPermissions() {
        request(new OnPermissionsResult() {
            @Override
            public void OnSuccess() {
                Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void OnFail(List<String> noPermissions) {
                logger.i("noPermissions:" + noPermissions.toString());
            }
        });

    }


    private void initView() {
        tcv = findViewById(R.id.mCloudView);
        addPicsButton = findViewById(R.id.addPics);
        addPicsButton.setOnClickListener(this);
        mCloudTagAdapter = new CloudTagAdapter(this);
        tcv.setAdapter(mCloudTagAdapter);
        tcv.setOnTagClickListener(new TagCloudView.OnTagClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, int position) {
                Intent intent = new Intent(MainActivity.this, MoreInfoActivity.class);
                intent.putExtra("position", position);
                startActivityForResult(intent, MORE_INFO_CALL);
            }
        });
        mPhotoSelectView = DialogManager.getInstance()
                .initView(this,R.layout.dialog_select_photo, Gravity.BOTTOM);
        tv_camera = (TextView) mPhotoSelectView.findViewById(R.id.tv_camera);
        tv_camera.setOnClickListener(this);
        tv_ablum = (TextView) mPhotoSelectView.findViewById(R.id.tv_ablum);
        tv_ablum.setOnClickListener(this);
        tv_cancel = (TextView) mPhotoSelectView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.addPics:
                DialogManager.getInstance().show(mPhotoSelectView);
                break;
            case R.id.tv_camera:
                DialogManager.getInstance().hide(mPhotoSelectView);
                if (!checkPermissions(Manifest.permission.CAMERA)) {
                    requestPermission(new String[]{Manifest.permission.CAMERA});
                } else {
                    //跳转到相机
                    FileHelper.getInstance().toCamera(this);
                }
                break;
            case R.id.tv_ablum:
                DialogManager.getInstance().hide(mPhotoSelectView);
                FileHelper.getInstance().toAlbum(this);

                break;
            case R.id.tv_cancel:
                DialogManager.getInstance().hide(mPhotoSelectView);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        logger.i("requestCode:" + requestCode + " resultCODE:" + resultCode);
        if(resultCode == AppCompatActivity.RESULT_OK){
            if(requestCode == FileHelper.CAMERA_REQUEST_CODE){
                String path = renameFileForCamera(FileHelper.getInstance().getTmpFile());
                addToDb(path);
                tcv.onChange();
            }else if(requestCode == ALBUM_REQUEST_CODE){
                List<String> chosedPics = null;
                if (data != null) {
                    chosedPics = data.getStringArrayListExtra(ImageSelector.EXTRA_RESULT);
                    for(String path : chosedPics){
                        path = renameFileForCamera(new File(path));
                        addToDb(path);
                    }
                    tcv.onChange();
                }
                //logger.i("---------\n" +Arrays.toString(chosedPics.toArray(new String[0])));
            }else if(requestCode == MORE_INFO_CALL){
                if(data.getBooleanExtra("isDataChanged", false)){

                    tcv.onChange();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public String renameFileForCamera(File file){
        SimpleDateFormat sdf = new SimpleDateFormat("yy_MM_dd_");
        String time = "img_" + sdf.format(new Date());

        String newPath = getExternalFilesDir(null).getAbsolutePath()+ "/"
                + time + MyData.getMyData().getCurrenIndex(time) + ".jpg";
        logger.i("time:"+time+
                " oldPath:" + file.getAbsolutePath() +
                " newPath:" + newPath);
        logger.i("rename result:" + file.renameTo(new File(newPath)));
        return newPath;
    }
    public void addToDb(String path){
        String uriString = new File(path).toURI().toString();
        String picsName = path.substring(path.lastIndexOf("/")+1, path.length()-4);
        String title = "";
        String describe = "";
        MyData.getMyData().addData(uriString, path, picsName, title, describe);
        DBHelper.getDbHelper(this).insert(uriString, path, picsName, title, describe);
    }


}
