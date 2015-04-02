package com.example.carmepreviewdemo.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carmepreviewdemo.R;
import com.example.carmepreviewdemo.manager.CameraInterface;
import com.example.carmepreviewdemo.manager.CameraInterface.CameraOpenOverCallback;
import com.example.carmepreviewdemo.manager.ProcessImageTask;
import com.example.carmepreviewdemo.manager.ProcessImageTask.ImageFilterCallback;
import com.example.carmepreviewdemo.util.DisplayUtil;
import com.example.carmepreviewdemo.util.FileUtil;
import com.example.carmepreviewdemo.util.FilterManager;
import com.example.carmepreviewdemo.util.ImageUtil;
import com.example.carmepreviewdemo.view.dialog.FilterPicker;
import com.example.carmepreviewdemo.view.view.CarmeSurfaceView;
import com.example.carmepreviewdemo.view.view.DragableView;
import com.example.carmepreviewdemo.view.view.MyFrameLayout;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements CameraOpenOverCallback,
		ImageFilterCallback, OnItemClickListener {

	MyFrameLayout myFrameLayout = null;
	CarmeSurfaceView mCarmeSurfaceView = null;
	TextView mPickerFilterTv;

	float previewRate = -1f;
	Bitmap mBitmap;
	FilterPicker mPopupWindow;
	LayoutParams mParams;

	ImageView savedImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		initViewParams();
		openCamera();
	}

	private void openCamera() {
		new Thread() {
			@Override
			public void run() {
				CameraInterface.instance(MainActivity.this).openCamera(
						MainActivity.this);
			}
		}.start();
	}

	private void initUI() {
		myFrameLayout = (MyFrameLayout) findViewById(R.id.myframe_layout);
		mCarmeSurfaceView = myFrameLayout.getCarmeSurfaceView();
		findViewById(R.id.take_photo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.take_photo:
					while (myFrameLayout.getChildCount() > 1) {
						myFrameLayout.removeViewAt(myFrameLayout
								.getChildCount() - 1);

					}
					CameraInterface.instance(MainActivity.this).doTakePicture(
							new PictureCallback() {
								public void onPictureTaken(byte[] data,
										Camera camera) {
									// TODO Auto-generated method stub
									Bitmap b = null;
									if (null != data) {
										b = BitmapFactory.decodeByteArray(data,
												0, data.length);// data是字节数据，将其解析成位图
									}
									// 保存图片到sdcard
									if (null != b) {
										mBitmap = ImageUtil.getRotateBitmap(b,
												90.0f);
									}
								}
							});
					break;
				default:
					break;
				}
			}
		});
		mPickerFilterTv = (TextView) findViewById(R.id.add_filter);
		mPickerFilterTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showFilterPickPopupWindow();
			}
		});

		findViewById(R.id.add_image).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				android.widget.FrameLayout.LayoutParams lp = new android.widget.FrameLayout.LayoutParams(
						DragableView.WIDTH, DragableView.WIDTH);
				lp.setMargins(DragableView.LEFT, DragableView.TOP, 0, 0);
				myFrameLayout.addView(new DragableView(MainActivity.this,
						myFrameLayout.getWidth(), myFrameLayout.getHeight()),
						lp);
			}
		});

		findViewById(R.id.save_img).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FileUtil.saveBitmapToSDcard(myFrameLayout.getContentBitmap(),
						MainActivity.this, null);
			}
		});
		savedImg = (ImageView) findViewById(R.id.saved_image);
		savedImg.setVisibility(View.GONE);
	}

	private void initViewParams() {
		mParams = (LayoutParams) mCarmeSurfaceView.getLayoutParams();
		Point p = DisplayUtil.getScreenMetrics(this);
		mParams.width = p.x;
		mParams.height = p.y - DisplayUtil.dip2px(this, 200);
		previewRate = DisplayUtil.getScreenRate(this); // 默认全屏的比例预览
		mCarmeSurfaceView.setLayoutParams(mParams);
	}

	private void showFilterPickPopupWindow() {
		if (mPopupWindow == null) {
			mPopupWindow = new FilterPicker(this, mPickerFilterTv, this);
		} else {
			mPopupWindow.showPopupWindow();
		}
	}

	@Override
	public void cameraHasOpened() {
		SurfaceHolder holder = mCarmeSurfaceView.getSurfaceHolder();
		CameraInterface.instance(MainActivity.this).doStartPreview(holder,
				previewRate);
	}

	@Override
	public void onExecuting() {
		Toast.makeText(this, "处理中...", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCompleteExecute(Bitmap result) {
		ImageView aImageView = new ImageView(this);
		aImageView.setScaleType(ScaleType.CENTER_CROP);
		aImageView.setImageBitmap(result);
		// 始终插到SurfaceView前，挂件后
		myFrameLayout.addView(aImageView, mParams);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		new ProcessImageTask(FilterManager.builderFilterInfoList()
				.get(position).getFilter(), MainActivity.this, mBitmap)
				.execute();
	}
}
