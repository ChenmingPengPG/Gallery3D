package com.pcm.fun214.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moxun.tagcloudlib.view.TagsAdapter;
import com.pcm.framework.helper.GlideHelper;
import com.pcm.fun214.R;
import com.pcm.framework.data.MyData;

import java.io.File;


public class CloudTagAdapter extends TagsAdapter {


    private Context mContext;
    private LayoutInflater inflater;

    public CloudTagAdapter(Context mContext) {
        this.mContext = mContext;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return MyData.getMyData().getPhotoUris().size();
    }

    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        View view = inflater.inflate(R.layout.tagcloud_item, null);
        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        TextView tv_name = view.findViewById(R.id.tv_name);

        GlideHelper.loadFile(context, new File(MyData.getMyData().getPathes().get(position)), iv_icon);
        if(MyData.getMyData().getPicsName().get(position) == null){
            tv_name.setText("");
        }else{
            tv_name.setText(MyData.getMyData().getTitle().get(position));
        }

        return view;
    }

    @Override
    public Object getItem(int position) {
        return MyData.getMyData().getTitle().get(position);
    }

    @Override
    public int getPopularity(int position) {
        return 5;
    }

    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }
}
