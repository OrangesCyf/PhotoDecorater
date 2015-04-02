package com.example.carmepreviewdemo.view.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {

	CarmeSurfaceView mCarmeSurfaceView;

	public MyFrameLayout(Context context) {
		super(context);
		init(context, null);
	}

	public MyFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		mCarmeSurfaceView = new CarmeSurfaceView(context, attrs);
		addView(mCarmeSurfaceView);
	}

	public CarmeSurfaceView getCarmeSurfaceView() {
		return mCarmeSurfaceView;
	}

	public Bitmap getContentBitmap() {
		clearFocus();
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		return bitmap;
	}
}
