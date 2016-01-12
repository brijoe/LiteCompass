package org.bridge.compass.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import org.bridge.compass.R;
import org.bridge.compass.util.LogUtil;
import org.bridge.compass.util.ToolUtil;

/**
 * 自定义的水平仪View
 */
public class GradienterView extends View {
    String TAG = "View";
    // 定义水平仪仪表盘图片
    private Bitmap circle1, circle2;
    // 定义水平仪仪表游标
    private Bitmap pointer1, pointer2;
    // 游标的X,Y坐标
    private int pointerX, pointerY;
    //中心位置坐标
    private int centerX, centerY;
    //图片像素尺寸
    private int circleSize, pointerSize;
    private Bitmap circle, pointer;


    public GradienterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载背景图片，游标图片
        circle1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.gradienter_circle_lying1);
        circle2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.gradienter_circle_lying2);
        pointer1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.gradienter_pointer_lying1);
        pointer2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.gradienter_pointer_lying2);
        //计算图像像素尺寸
        circleSize = ToolUtil.DipToPixels(getContext(), 250);
        pointerSize = ToolUtil.DipToPixels(getContext(), 30);
        //调整尺寸
        circle1 = ToolUtil.resizeBitmap(circle1, circleSize, circleSize);
        circle2 = ToolUtil.resizeBitmap(circle2, circleSize, circleSize);
        pointer1 = ToolUtil.resizeBitmap(pointer1, pointerSize, pointerSize);
        pointer2 = ToolUtil.resizeBitmap(pointer2, pointerSize, pointerSize);
        //计算中心位置坐标
        centerX = (circle1.getWidth() - pointer1.getWidth()) / 2;
        centerY = (circle1.getHeight() - pointer1.getHeight()) / 2;
        LogUtil.d(TAG, "构造方式执行");
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        LogUtil.d(TAG, "onDraw方法执行");
        //根据pointer坐标是否在中心位置决定加载bitmap
        circle = (pointerX == centerX && pointerY == centerY) ? circle2 : circle1;
        pointer = (pointerX == centerX && pointerY == centerY) ? pointer2 : pointer1;
        // 绘制水平仪表盘图片
        canvas.drawBitmap(circle, 0, 0, null);
        // 根据游标的坐标绘制游标
        canvas.drawBitmap(pointer, pointerX, pointerY, null);
    }

    /**
     * 提供给外部设置中心游标pointer的坐标
     *
     * @param newX
     * @param newY
     */
    public void setPointer(int newX, int newY) {
        this.pointerX = newX;
        this.pointerY = newY;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public int getPointerWidth() {
        return pointer1.getWidth();
    }

    public int getCircleWidth() {
        return circle1.getWidth();
    }

}
