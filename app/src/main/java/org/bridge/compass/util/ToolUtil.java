package org.bridge.compass.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 工具类
 */
public class ToolUtil {
    // 根据转动角显示刻度
    public static String DegreeOffset(float degree) {
        float offset = (degree >= 0) ? degree : (360 + degree);
        int result = Math.round(offset);
        return String.valueOf(result) + "°";
    }

    /**
     * 根据转动角显示方位
     *
     * @param degree
     * @return
     */
    public static String DirectionStr(float degree) {
        String direction = "";
        float offset = (degree >= 0) ? degree : (360 + degree);
        if (offset < 22.5 || offset >= 337.5)
            direction = "北";
        if (offset >= 22.5 && offset < 67.5)
            direction = "东北";
        if (offset >= 67.5 && offset < 112.5)
            direction = "东";
        if (offset >= 112.5 && offset < 157.5)
            direction = "东南";
        if (offset >= 157.5 && offset < 202.5)
            direction = "南";
        if (offset >= 202.5 && offset < 247.5)
            direction = "西南";
        if (offset >= 247.5 && offset < 292.5)
            direction = "西";
        if (offset >= 292.5 && offset < 337.5)
            direction = "西北";
        return direction;
    }

    /**
     * 度数转化为度分秒
     *
     * @param degree
     * @return
     */
    public static String DegreeConvert(Double degree) {
        String parm1 = "00";
        String parm2 = "00";
        String parm3 = "00";
        String deg = String.valueOf(degree);
        String[] temp = deg.split("\\.");
        parm1 = temp[0];
        LogUtil.d("deg", parm1);
        temp = (Double.parseDouble("0." + temp[1]) * 60 + "").split("\\.");
        parm2 = temp[0];
        temp = (Double.parseDouble("0." + temp[1]) * 60 + "").split("\\.");
        parm3 = temp[0];
        return parm1 + "°" + parm2 + "′" + parm3 + "″";
    }

    /**
     * 创建自定义宽高的 bitmap
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */

    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = w;
            int newHeight = h;
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {
            return null;
        }
    }

    /**
     * dip转像素
     *
     * @param context
     * @param dip
     * @return
     */

    public static int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;

        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);

        return valuePixels;

    }

    /**
     * 取平方根
     *
     * @param x
     * @param y
     * @return
     */
    public static int SqrRoot(float x, float y) {


        int root = (int) Math.sqrt(x * x + y * y);
        return root;
    }

}
