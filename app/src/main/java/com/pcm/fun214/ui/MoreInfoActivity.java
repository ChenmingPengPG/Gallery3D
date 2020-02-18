package com.pcm.fun214.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pcm.framework.base.BaseBackActivity;
import com.pcm.framework.helper.DBHelper;
import com.pcm.framework.manager.DialogManager;
import com.pcm.framework.view.DialogView;
import com.pcm.fun214.R;
import com.pcm.framework.data.MyData;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;

public class MoreInfoActivity extends BaseBackActivity implements View.OnClickListener {
    private ImageView imageView;
    private TextView textView;
    private TextView describeTV;
    private EditText titleEt;
    private EditText describeEt;
    private EditText yearEt;
    private EditText monthEt;
    private EditText dayEt;
    private Button confirmButton;
    private Button cancelButton;
    public static final int INIT_VALUE = -1;
    private int position = INIT_VALUE;
    private DialogView describeView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreinfo);
        initView();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                if(position != INIT_VALUE){
                    //String picsName = MyData.getMyData().getPicsName().get(position);
                    String photoUris = MyData.getMyData().getPhotoUris().get(position);
                    MyData.getMyData().deleteData(position);
                    DBHelper.getDbHelper(this).delete(new String[]{photoUris});

                    Intent intent = new Intent();
                    intent.putExtra("isDataChanged", true);
                    setResult(AppCompatActivity.RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.addDescribe:

                DialogManager.getInstance().show(describeView);
                Toast.makeText(this, "点击了添加描述,added in to do list", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        imageView = this.findViewById(R.id.img);
        textView = this.findViewById(R.id.tv);
        describeTV = this.findViewById(R.id.descrbeTV);
        position = getIntent().getIntExtra("position", -1);
        //设置bitmap
        String uri = MyData.getMyData().getPhotoUris().get(position);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uri));
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //从图像文件获取相片信息
        String[] name = MyData.getMyData().getPicsName().get(position).split("_");
        logger.i("photo name:" + MyData.getMyData().getPicsName().get(position));
        try {
            ExifInterface exifInterface = new ExifInterface(MyData.getMyData().getPathes().get(position));
            String takeTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            logger.i("takeTime:"+ takeTime);
            if(takeTime != null){
                textView.setText(takeTime);
            }else if(name.length >= 4){
                textView.setText(name[1] + "年" + name[2] + "月" + name[3] + "日拍摄");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        describeTV.setText(MyData.getMyData().getDescribe().get(position));
        describeView = DialogManager.getInstance().initViewDescribe(this, R.layout.dialog_add_describe);
        titleEt = describeView.findViewById(R.id.etTitle);
        yearEt = describeView.findViewById(R.id.etYear);
        monthEt = describeView.findViewById(R.id.etMonth);
        dayEt = describeView.findViewById(R.id.etDay);
        describeEt = describeView.findViewById(R.id.describeEt);
        confirmButton = describeView.findViewById(R.id.butrtonConfirm);
        cancelButton = describeView.findViewById(R.id.butrtonCancel);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.butrtonCancel:
                DialogManager.getInstance().hide(describeView);
                break;
            case R.id.butrtonConfirm:
                String title = titleEt.getText().toString();
                String describe = describeEt.getText().toString();
                String uri = MyData.getMyData().getPhotoUris().get(position);
                String time =  yearEt.getText().toString() + "_" +
                    monthEt.getText().toString()+"_" +
                    dayEt.getText().toString()+"_";
                String name = "img_" + time + MyData.getMyData().getCurrenIndex(time);
                textView.setText(time);
                describeTV.setText(describe);
                MyData.getMyData().getDescribe().set(position, describe);
                MyData.getMyData().getTitle().set(position, title);
                MyData.getMyData().getPicsName().set(position, name);
                DBHelper.getDbHelper(this).updateNameTitleDes(uri, name, title, describe);

                Intent intent = new Intent();
                intent.putExtra("isDataChanged", true);
                setResult(AppCompatActivity.RESULT_OK, intent);
                DialogManager.getInstance().hide(describeView);
                break;
        }
    }
}
