package org.bridge.compass.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义字体的TextView
 */
public class TextViewCustom extends TextView {
    /**
     * 使用自定义字体
     *
     * @param context
     */

    public TextViewCustom(Context context) {
        super(context);
        initView(context);
    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        AssetManager assetMgr = context.getAssets();
        Typeface font = Typeface.createFromAsset(assetMgr,
                "fonts/Helvetica Neue CE 35 Thin.ttf");
        setTypeface(font);
    }

}
