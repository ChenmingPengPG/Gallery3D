package com.pcm.framework.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class DialogView extends Dialog {
    public DialogView(Context mContext, int layout, int style, int gravity) {
        super(mContext, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
    }

    public DialogView(Context mContext, int layout, int style,
                      boolean isDescribe) {
        super(mContext, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if(isDescribe){
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.width = 1000;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        window.setAttributes(layoutParams);
    }
}
