package com.example.carmepreviewdemo.manager;

import HaoRan.ImageFilter.IImageFilter;
import HaoRan.ImageFilter.Image;
import android.graphics.Bitmap;
import android.os.AsyncTask;

//import com.example.carmepreviewdemo.util.filter.IImageFilter;
//import com.example.carmepreviewdemo.util.filter.Image;

public class ProcessImageTask extends AsyncTask<Void, Void, Bitmap> {
	private IImageFilter filter;
	private ImageFilterCallback mImageFilterCallback;
	private Bitmap mBitmap;

	public ProcessImageTask(IImageFilter imageFilter,
			ImageFilterCallback aImageFilterCallback, Bitmap aBitmap) {
		this.filter = imageFilter;
		mImageFilterCallback = aImageFilterCallback;
		mBitmap = aBitmap;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (mImageFilterCallback != null) {
			mImageFilterCallback.onExecuting();
		}
	}

	public Bitmap doInBackground(Void... params) {
		Image img = null;
		try {
			img = new Image(mBitmap);
			if (filter != null) {
				img = filter.process(img);
				img.copyPixelsFromBuffer();
			}
			return img.getImage();
		} catch (Exception e) {
			if (img != null && img.destImage.isRecycled()) {
				img.destImage.recycle();
				img.destImage = null;
				System.gc();
			}
		} finally {
			if (img != null && img.image.isRecycled()) {
				img.image.recycle();
				img.image = null;
				System.gc();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null) {
			super.onPostExecute(result);
			if (mImageFilterCallback != null) {
				mImageFilterCallback.onCompleteExecute(result);
			}
		}
	}

	public interface ImageFilterCallback {
		public void onExecuting();

		public void onCompleteExecute(Bitmap result);
	}
}
