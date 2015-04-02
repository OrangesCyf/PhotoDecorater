package com.example.carmepreviewdemo.view.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.carmepreviewdemo.manager.CameraInterface;

/**
 * 自定义相机界面
 * 
 * @author chengyanfang
 * 
 * */
@SuppressWarnings("deprecation")
public class CarmeSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	private CameraInterface mCameraInterface;
	// SurfaceHolder用来设置SurfaceView的一个对象，连接SurfaceView和Camera
	private SurfaceHolder mSurfaceHolder;
	@SuppressWarnings("deprecation")
	// 相机对象
	private Camera mCamera;
	private Context mContext;

	private Point mResizePoint;

	public CarmeSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
		// 调用GPU加速
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		CameraInterface.instance(mContext).doStopPreview();
	}

	public SurfaceHolder getSurfaceHolder() {
		return mSurfaceHolder;
	}

}
