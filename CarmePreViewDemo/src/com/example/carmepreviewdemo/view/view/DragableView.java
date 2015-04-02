package com.example.carmepreviewdemo.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.example.carmepreviewdemo.R;

public class DragableView extends ImageView {

	/* 处理大小、位置 */
	public static int WIDTH = 150;
	public static int HEIGHT = 150;
	public static int LEFT = 150;
	public static int TOP = 150;
	public static int RIGHT = 150;
	public static int BOTTOM = 150;

	/* 屏幕界限 */
	private int screenW;
	private int screenH;
	private TranslateAnimation trans; // 处理超出边界的动画

	/* 处理拖动位移 */
	private float start_x, start_y;
	private float stop_x, stop_y;

	/* 操作模式 */
	static final int NONE = 0;
	static final int DRAG = 1; // 拖动中
	static final int ZOOM = 2; // 缩放中
	static final int BIGGER = 3; // 放大ing
	static final int SMALLER = 4; // 缩小ing
	private int mode = NONE; // 当前的事件

	/* 缩放比例控制 */
	private float beforeLenght; // 两触点距离
	private float afterLenght; // 两触点距离
	private float scale = 0.04f; // 缩放的比例 X Y方向都是这个值 越大缩放的越快

	public DragableView(Context context, int parentWidth, int parentHeight) {
		this(context, null, parentWidth, parentHeight);
	}

	public DragableView(Context context, AttributeSet attrs, int parentWidth,
			int parentHeight) {
		super(context, attrs);
		screenW = parentWidth;
		screenH = parentHeight;
		setBackgroundResource(R.drawable.ic_launcher);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(WIDTH, HEIGHT);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			stop_x = (int) event.getRawX();
			stop_y = (int) event.getRawY();
			start_x = (int) event.getX();
			start_y = stop_y - this.getTop();
			if (event.getPointerCount() == 2)
				beforeLenght = spacing(event);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			if (spacing(event) > 10f) {
				mode = ZOOM;
				beforeLenght = spacing(event);
			}
			break;
		case MotionEvent.ACTION_UP:
			/* 判断是否超出范围 并处理 */
			int disX = 0;
			int disY = 0;
			if (getHeight() <= screenH || this.getTop() < 0) {
				if (this.getTop() < 0) {
					int dis = getTop();
					this.layout(this.getLeft(), 0, this.getRight(),
							0 + this.getHeight());
					disY = dis - getTop();
				} else if (this.getBottom() > screenH) {
					disY = getHeight() - screenH + getTop();
					this.layout(this.getLeft(), screenH - getHeight(),
							this.getRight(), screenH);
				}
			}
			if (getWidth() <= screenW) {
				if (this.getLeft() < 0) {
					disX = getLeft();
					this.layout(0, this.getTop(), 0 + getWidth(),
							this.getBottom());
				} else if (this.getRight() > screenW) {
					disX = getWidth() - screenW + getLeft();
					this.layout(screenW - getWidth(), this.getTop(), screenW,
							this.getBottom());
				}
			}
			if (disX != 0 || disY != 0) {
				trans = new TranslateAnimation(disX, 0, disY, 0);
				trans.setDuration(500);
				this.startAnimation(trans);
			}
			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			/* 处理拖动 */
			if (mode == DRAG) {
				if (Math.abs(stop_x - start_x - getLeft()) < 88
						&& Math.abs(stop_y - start_y - getTop()) < 85) {
					this.setPosition((int) (stop_x - start_x),
							(int) (stop_y - start_y),
							(int) (stop_x + this.getWidth() - start_x),
							(int) (stop_y - start_y + this.getHeight()));
					stop_x = (int) event.getRawX();
					stop_y = (int) event.getRawY();
				}
			}
			/* 处理缩放 */
			else if (mode == ZOOM) {
				if (spacing(event) > 10f) {
					afterLenght = spacing(event);
					float gapLenght = afterLenght - beforeLenght;
					if (gapLenght == 0) {
						break;
					} else if (Math.abs(gapLenght) > 5f) {
						if (gapLenght > 0) {
							this.setScale(scale, BIGGER);
						} else {
							this.setScale(scale, SMALLER);
						}
						beforeLenght = afterLenght;
					}
				}
			}
			break;
		}
		return true;
	}

	/**
	 * 就算两点间的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	/**
	 * 实现处理拖动
	 */
	private void setPosition(int left, int top, int right, int bottom) {
		this.layout(left, top, right, bottom);
	}

	/**
	 * 实现处理缩放
	 */
	private void setScale(float temp, int flag) {

		if (flag == BIGGER) {
			this.setFrame(this.getLeft() - (int) (temp * this.getWidth()),
					this.getTop() - (int) (temp * this.getHeight()),
					this.getRight() + (int) (temp * this.getWidth()),
					this.getBottom() + (int) (temp * this.getHeight()));
		} else if (flag == SMALLER) {
			this.setFrame(this.getLeft() + (int) (temp * this.getWidth()),
					this.getTop() + (int) (temp * this.getHeight()),
					this.getRight() - (int) (temp * this.getWidth()),
					this.getBottom() - (int) (temp * this.getHeight()));
		}
	}

}
