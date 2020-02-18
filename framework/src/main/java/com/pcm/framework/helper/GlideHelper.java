package com.pcm.framework.helper;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pcm.framework.R;

import java.io.File;

public class GlideHelper {
    public static void loadFile(Context mContext, File file, ImageView imageView) {
        if (mContext != null) {
            Glide.with(mContext)
                    .load(file)
                    .placeholder(R.drawable.glide_load_ing_img)
                    .error(R.drawable.glide_load_error_img)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        }
    }

    public static void loadUrl(Context mContext, String url, ImageView imageView) {
        if (mContext != null) {
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.drawable.glide_load_ing_img)
                    .error(R.drawable.glide_load_error_img)
                    .format(DecodeFormat.PREFER_RGB_565)
                    // 取消动画，防止第一次加载不出来
                    .dontAnimate()
                    //加载缩略图
                    .thumbnail(0.3f)
                    .skipMemoryCache(false)
                    //若缓存 可能导致加载不一致
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        }
    }
}
