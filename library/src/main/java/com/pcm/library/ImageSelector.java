package com.pcm.library;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.pcm.library.bean.MediaSelectConfig;
import com.pcm.library.ui.ImageSelectActivity;
import com.pcm.library.utils.CheckNullUtils;


/**
 * 图片选择器
 *
 * @author jiahui
 *         date 16/3/17
 */
public class ImageSelector {
    public static final String EXTRA_RESULT = Constant.KEY_EXTRA_RESULT;

    private volatile static ImageSelector sSelector;
    private MediaSelectConfig mMediaSelectConfig;

    private ImageSelector() {
    }

    public static ImageSelector create() {
        if (sSelector == null) {
            synchronized (ImageSelector.class) {
                if (sSelector == null)
                    sSelector = new ImageSelector();
            }
        }
        return sSelector;
    }

    /**
     * set media config
     *
     * @param config {@link MediaSelectConfig}
     * @return {@link ImageSelector}
     */
    public ImageSelector setMediaConfig(MediaSelectConfig config) {
        mMediaSelectConfig = config;
        return sSelector;
    }

    public void startImageAction(Activity activity, int requestCode) {
        CheckNullUtils.check(mMediaSelectConfig);
        mMediaSelectConfig.setMediaType(MediaSelectConfig.IMAGE);
        ImageSelectActivity.start(activity, requestCode, mMediaSelectConfig);
    }

    public void startImageAction(Fragment fragment, int requestCode) {
        CheckNullUtils.check(mMediaSelectConfig);
        mMediaSelectConfig.setMediaType(MediaSelectConfig.IMAGE);
        ImageSelectActivity.start(fragment, requestCode, mMediaSelectConfig);
    }

    public void startVideoAction(Activity activity, int requestCode) {
        CheckNullUtils.check(mMediaSelectConfig);
        mMediaSelectConfig.setMediaType(MediaSelectConfig.VIDEO);
        ImageSelectActivity.start(activity, requestCode, mMediaSelectConfig);
    }

    public void startVideoAction(Fragment fragment, int requestCode) {
        CheckNullUtils.check(mMediaSelectConfig);
        mMediaSelectConfig.setMediaType(MediaSelectConfig.VIDEO);
        ImageSelectActivity.start(fragment, requestCode, mMediaSelectConfig);
    }
}
