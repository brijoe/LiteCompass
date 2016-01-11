package org.bridge.compass.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import org.bridge.compass.R;
import org.bridge.compass.util.ToolUtil;

/**
 * 自定义的水平仪View
 */
public class GradienterView extends View {
    // 定义水平仪仪表盘图片
    public Bitmap circle;
    // 定义水平仪仪表游标
    public Bitmap pointer;
    // 游标的X,Y坐标
    public int pointerX, pointerY;

    public GradienterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载背景图片，游标图片
        circle = BitmapFactory.decodeResource(getResources(),
                R.drawable.gradienter_circle_lying1);
        pointer = BitmapFactory.decodeResource(getResources(),
                R.drawable.gradienter_pointer_lying1);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 如果水平仪不水平，改变bitmap资源
        // 游标位于中间时（水平仪完全水平），游标的x,y坐标
        int circle_w = circle.getWidth();
        int circle_h = circle.getHeight();
        int pointer_w = pointer.getWidth();
        int pointer_h = pointer.getHeight();
        int centerX = (circle_w - pointer_w) / 2;
        int centerY = (circle_h - pointer_h) / 2;
//		if (pointerX == centerX && pointerY == centerY) {
//			circle = BitmapFactory.decodeResource(getResources(),
//					R.drawable.gradienter_circle_lying2);
//			pointer = BitmapFactory.decodeResource(getResources(),
//					R.drawable.gradienter_pointer_lying2);
//
//		} else {
//			circle = BitmapFactory.decodeResource(getResources(),
//					R.drawable.gradienter_circle_lying1);
//			pointer = BitmapFactory.decodeResource(getResources(),
//					R.drawable.gradienter_pointer_lying1);
//
//		}

        // 绘制水平仪表盘图片

        int size1 = ToolUtil.DipToPixels(getContext(), 250);
        circle = ToolUtil.resizeBitmap(circle, size1, size1);
        canvas.drawBitmap(circle, 0, 0, null);
        // 根据游标的坐标绘制游标
        int size2 = ToolUtil.DipToPixels(getContext(), 30);
        pointer = ToolUtil.resizeBitmap(pointer, size2, size2);
        canvas.drawBitmap(pointer, pointerX, pointerY, null);
    }

}
