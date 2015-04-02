package com.example.carmepreviewdemo.manager;

import java.io.IOException;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.opengl.GLES20;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.carmepreviewdemo.util.CamParaUtil;
import com.example.carmepreviewdemo.util.FileUtil;
import com.example.carmepreviewdemo.util.ImageUtil;

/**
 * Camera和界面SurfaceView的结合层，降低功能和UI的耦合
 * */
@SuppressWarnings("deprecation")
public class CameraInterface {
	@SuppressWarnings("deprecation")
	private Camera mCamera;

	@SuppressWarnings("deprecation")
	private Camera.Parameters mParameters;

	private boolean isPreviewing = false;
	private float mPreviewingRate = -1f;

	private static CameraInterface mCameraInterface;

	private static Context mContext;

	private Bitmap mBitmap;

	public interface CameraOpenOverCallback {
		public void cameraHasOpened();
	}

	private CameraInterface(Context context) {
		mContext = context;
	}

	public static synchronized CameraInterface instance(Context context) {
		if (mCameraInterface == null) {
			mCameraInterface = new CameraInterface(context);
		}
		return mCameraInterface;
	}

	/**
	 * 打开Camera
	 * 
	 * @param callback
	 * */
	public void openCamera(CameraOpenOverCallback callback) {
		mCamera = Camera.open();
		callback.cameraHasOpened();
	}

	/**
	 * 开启预览
	 * 
	 * @param holder
	 * @param previewRate
	 * */
	public void doStartPreview(SurfaceHolder holder, float previewRate) {
		if (isPreviewing) {
			mCamera.stopPreview();
			return;
		}

		if (mCamera != null) {
			mParameters = mCamera.getParameters();
			// 设置图片格式
			mParameters.setPictureFormat(PixelFormat.JPEG);
			// 设置PreviewSize
			Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
					mParameters.getSupportedPictureSizes(), previewRate, 800);
			mParameters.setPictureSize(pictureSize.width, pictureSize.height);
			// 设置PictureSize
			Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
					mParameters.getSupportedPreviewSizes(), previewRate, 800);
			mParameters.setPreviewSize(previewSize.width, previewSize.height);
			// 设置方向
			mCamera.setDisplayOrientation(90);
			// 设置聚焦模式
			List<String> focusModes = mParameters.getSupportedFocusModes();
			if (focusModes.contains("continuous-video")) {
				mParameters
						.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			}
			mCamera.setParameters(mParameters);

			// 开启预览
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}

			isPreviewing = true;
			mPreviewingRate = previewRate;
		}
	}

	/**
	 * 停止预览，释放Camera
	 * */
	public void doStopPreview() {
		if (null != mCamera) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			isPreviewing = false;
			mPreviewingRate = -1f;
			mCamera.release();
			mCamera = null;
		}
	}

	/**
	 * 拍照
	 */
	public void doTakePicture(PictureCallback aJpegPictureCallback) {
		if (isPreviewing && (mCamera != null)) {
			mCamera.takePicture(null, null, aJpegPictureCallback);
			isPreviewing = false;
		} else {
			isPreviewing = true;
			mCamera.startPreview();
		}
	}

	/* 为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量 */
	// 快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
	ShutterCallback mShutterCallback = new ShutterCallback() {
		public void onShutter() {

		}
	};
	// 拍摄的未压缩原数据的回调,可以为null
	PictureCallback mRawCallback = new PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {

		}
	};
	// 对jpeg图像数据的回调,最重要的一个回调
	PictureCallback mJpegPictureCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			Bitmap b = null;
			if (null != data) {
				b = BitmapFactory.decodeByteArray(data, 0, data.length);// data是字节数据，将其解析成位图
				mCamera.stopPreview();
				isPreviewing = false;
			}
			// 保存图片到sdcard
			if (null != b) {
				Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
				FileUtil.saveBitmapToSDcard(rotaBitmap, mContext, null);
				mBitmap = rotaBitmap;
			}
			// 再次进入预览
			// mCamera.startPreview();
			// isPreviewing = true;
		}
	};

	public Bitmap capture(SurfaceView aSurfaceView) throws InterruptedException {
		final Semaphore waiter = new Semaphore(0);

		final int width = aSurfaceView.getMeasuredWidth();
		final int height = aSurfaceView.getMeasuredHeight();

		// Take picture on OpenGL thread
		final int[] pixelMirroredArray = new int[width * height];
		new Thread((new Runnable() {
			@Override
			public void run() {
				final IntBuffer pixelBuffer = IntBuffer
						.allocate(width * height);
				GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA,
						GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
				int[] pixelArray = pixelBuffer.array();

				// Convert upside down mirror-reversed image to right-side up
				// normal image.
				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						pixelMirroredArray[(height - i - 1) * width + j] = pixelArray[i
								* width + j];
					}
				}
				waiter.release();
			}
		})).start();
		aSurfaceView.requestLayout();
		waiter.acquire();

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.copyPixelsFromBuffer(IntBuffer.wrap(pixelMirroredArray));
		return bitmap;
	}

	public Bitmap getmBitmap() {
		return mBitmap;
	}

}
