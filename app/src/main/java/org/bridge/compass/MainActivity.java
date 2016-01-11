package org.bridge.compass;

import java.util.ArrayList;

import org.bridge.compass.util.ToolUtil;
import org.bridge.compass.util.LogUtil;
import org.bridge.compass.view.GradienterView;
import org.bridge.compass.view.TextViewCustom;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class MainActivity extends Activity implements SensorEventListener,
		BDLocationListener {
	// 切换页面
	private ViewPager mViewPager;
	private ImageView mPage0;
	private ImageView mPage1;
	// 指南针
	private ImageView kd;
	private TextViewCustom txt_digit_north;
	private TextViewCustom txt_digit_east;
	private TextViewCustom txt_angle;
	private TextViewCustom txt_direction;
	private LocationClient mLocationClient;
	private SensorManager sensorManager;
	private float[] accelerometerValues = new float[3];
	private float[] magneticValues = new float[3];
	private float lastRotateDegree;
	// 水平仪
	private GradienterView gradienterView;
	private TextViewCustom txt_gradienter_offset;
	// 定义水平仪能处理的最大倾斜角，超过该角度,游标将直接位于边界
	private final int MAX_ANGLE = 30;
	private int lastX, lastY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		registerSensor();
		requestLocation();
	}

	private void initView() {

		//
		mViewPager = (ViewPager) findViewById(R.id.new_viewpager);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		// 生成布局
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.view_compass, null);
		View view2 = mLi.inflate(R.layout.view_gradienter, null);
		// 装载View
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		// 自定义ViewPager适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		mViewPager.setAdapter(mPagerAdapter);
		// 指南针

		txt_digit_north = (TextViewCustom) findViewById(R.id.txt_digit_north);
		txt_digit_east = (TextViewCustom) findViewById(R.id.txt_digit_east);
		txt_direction = (TextViewCustom) view1.findViewById(R.id.txt_direction);
		txt_angle = (TextViewCustom) view1.findViewById(R.id.txt_angle);
		kd = (ImageView) view1.findViewById(R.id.kd);
		// 水平仪
		// 获取水平仪的主组件
		gradienterView = (GradienterView) view2
				.findViewById(R.id.gradienterView);
		txt_gradienter_offset = (TextViewCustom) view2
				.findViewById(R.id.txt_gradienter_offset);
	}

	private void registerSensor() {
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		Sensor magneticSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		Sensor accelerometerSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, magneticSensor,
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(this, accelerometerSensor,
				SensorManager.SENSOR_DELAY_GAME);

	}

	private void requestLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		// 设置定位参数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.setNeedDeviceDirect(false);// 网络定位中不需要获取设备方向
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(this); // 注册监听函数
		mLocationClient.start();// 开启定位

	}

	private void updateView(BDLocation location) {
		if (location != null) {
			// 显示纬度
			txt_digit_north.setText(ToolUtil.DegreeConvert(location
					.getLatitude()));
			// 显示经度
			txt_digit_east.setText(ToolUtil.DegreeConvert(location
					.getLongitude()));
		} else {
			Toast.makeText(this, "当前定位不可用T_T", Toast.LENGTH_LONG).show();
			txt_digit_north.setText("------");
			txt_digit_east.setText("------");
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (sensorManager != null) {
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	//
	@Override
	public void onSensorChanged(SensorEvent event) {
		// 判断当前是加速度传感器还是地磁传感器
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// 赋值时要调用clone()方法
			accelerometerValues = event.values.clone();
		} else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			magneticValues = event.values.clone();
		}
		float[] values = new float[3];
		float[] R = new float[9];
		SensorManager.getRotationMatrix(R, null, accelerometerValues,
				magneticValues);
		SensorManager.getOrientation(R, values);
		// 将计算出的旋转角度取反，用于旋转指南针刻度盘
		float rotateDegree = -(float) Math.toDegrees(values[0]);
		LogUtil.d("rotate deg", rotateDegree + "");
		if (Math.abs(rotateDegree - lastRotateDegree) > 1) {
			RotateAnimation animation = new RotateAnimation(lastRotateDegree,
					rotateDegree, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(1000);
			animation.setInterpolator(new LinearInterpolator());
			animation.setFillAfter(true);
			kd.startAnimation(animation);
			lastRotateDegree = rotateDegree;
			txt_angle.setText(ToolUtil.DegreeOffset(-rotateDegree));
			txt_direction.setText(ToolUtil.DirectionStr(-rotateDegree));
		}
		// 水平仪
		float gradienter_yAngle = (float) Math.toDegrees(values[1]);// 围绕X轴旋转夹角
		float gradienter_zAngle = (float) Math.toDegrees(values[2]);// 围绕Y轴旋转夹角

		// txt_gradienter_offset.setText(ToolUtil.SqrRoot(gradienter_yAngle,
		// gradienter_zAngle) + "°");
		txt_gradienter_offset.setText(Math.round(gradienter_yAngle) + "°"
				+ Math.round(gradienter_zAngle) + "°");

		int circle_w = gradienterView.circle.getWidth();
		int circle_h = gradienterView.circle.getHeight();
		int pointer_w = gradienterView.pointer.getWidth();
		int pointer_h = gradienterView.pointer.getHeight();
		// 游标位于中间时（水平仪完全水平），游标的x,y坐标
		int x = (circle_w - pointer_w) / 2;
		int y = (circle_h - pointer_h) / 2;
		// 如果Z轴的倾斜角还在最大角度之内
		if (Math.abs(gradienter_zAngle) <= MAX_ANGLE) {
			// 根据与z轴的倾斜角度计算x坐标的变化值
			int deltaX = (int) ((circle_w - pointer_w) / 2 * gradienter_zAngle / MAX_ANGLE);
			x += deltaX;
		}
		// 如果与z轴的倾斜角已经大于MAX_ANGLE,气泡应到最左边
		else if (gradienter_zAngle > MAX_ANGLE) {
			x = 0;
		}
		// 如果与z轴的倾斜角已经小于负的MAX_ANGLE,气泡应到最右边
		else {
			x = circle_w - pointer_w;
		}
		if (Math.abs(gradienter_yAngle) <= MAX_ANGLE) {
			// 根据与y轴的倾斜角度计算y坐标的变化值
			int deltaY = (int) ((circle_h - pointer_h) / 2 * gradienter_yAngle / MAX_ANGLE);
			// if(Math.abs(d)deltaY)
			y += deltaY;
		}
		// 如果与y轴的倾斜角已经大于MAX_ANGLE,气泡应到最下边
		else if (gradienter_yAngle > MAX_ANGLE) {
			y = circle_h - pointer_h;
		}
		// 如果与y轴的倾斜角已经小于负的MAX_ANGLE,气泡应到最右边
		else {
			y = 0;
		}
		// 如果计算出来的x,y坐标还位于水平仪的仪表盘内，更新水平仪游标的坐标
		if (isContain(x, y)) {
			gradienterView.pointerX = x;
			gradienterView.pointerY = y;
		}
		// 通知系统重绘gradienterView组件
		 if (Math.abs(x - lastX) > 5&& Math.abs(y - lastY) <3) {
			 LogUtil.d("on draw","x="+x+",y="+y);
			 gradienterView.postInvalidate();
			 lastX = x;
			 lastY = y;
		 }
		 //if()
		gradienterView.postInvalidate();
	}

	/**
	 * 判断游标是否位于仪表盘中
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isContain(int x, int y) {
		int circle_w = gradienterView.circle.getWidth();
		int pointer_w = gradienterView.pointer.getWidth();
		// 计算游标的圆心坐标x,y
		int pointerCx = x + pointer_w / 2;
		int pointerCy = y + pointer_w / 2;
		// 计算仪表盘的圆心坐标
		int circleCx = circle_w / 2;
		int circleCy = circle_w / 2;
		// 计算游标的圆心与水平仪仪表盘的圆心
		double distance = Math.sqrt((pointerCx - circleCx)
				* (pointerCx - circleCx) + (pointerCy - circleCy)
				* (pointerCy - circleCy));
		// 若两个圆心的距离小于他们的半径差，则游标位于仪表盘内
		if (distance < (circle_w - pointer_w) / 2)
			return true;
		else
			return false;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		updateView(location);
	}

	/**
	 * @author George viewPager变化监听器
	 * 
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 1:
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

	}
}