package com.example.carmepreviewdemo.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;

/**
 * 专门用来设置、打印Camera的PreviewSize、PictureSize、FocusMode的，
 * 并能根据Activity传进来的长宽比(主要是16:9 或 4:3两种尺寸)自动寻找适配的PreviewSize和PictureSize， 消除变形
 * */
@SuppressWarnings("deprecation")
public class CamParaUtil {

	private CameraSizeComparator mSizeComparator = new CameraSizeComparator();
	private static CamParaUtil mCamParaUtil = null;

	private CamParaUtil() {

	}

	public static CamParaUtil getInstance() {
		if (mCamParaUtil == null) {
			mCamParaUtil = new CamParaUtil();
		}
		return mCamParaUtil;
	}

	public Size getPropPreviewSize(List<Camera.Size> list, float th,
			int minWidth) {
		Collections.sort(list, mSizeComparator);

		int i = 0;
		for (Size s : list) {
			if ((s.width >= minWidth) && equalRate(s, th)) {
				break;
			}
			i++;
		}
		if (i == list.size()) {
			i = 0;// 如果没找到，就选最小的size
		}
		return list.get(i);
	}

	public Size getPropPictureSize(List<Camera.Size> list, float th,
			int minWidth) {
		Collections.sort(list, mSizeComparator);

		int i = 0;
		for (Size s : list) {
			if ((s.width >= minWidth) && equalRate(s, th)) {
				break;
			}
			i++;
		}
		if (i == list.size()) {
			i = 0;// 如果没找到，就选最小的size
		}
		return list.get(i);
	}

	public boolean equalRate(Size s, float rate) {
		float r = (float) (s.width) / (float) (s.height);
		if (Math.abs(r - rate) <= 0.03) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 打印支持的previewSizes
	 * 
	 * @param params
	 */
	public void printSupportPreviewSize(Camera.Parameters params) {
		List<Size> previewSizes = params.getSupportedPreviewSizes();
		for (int i = 0; i < previewSizes.size(); i++) {
			Size size = previewSizes.get(i);
		}

	}

	/**
	 * 打印支持的pictureSizes
	 * 
	 * @param params
	 */
	public void printSupportPictureSize(Camera.Parameters params) {
		List<Size> pictureSizes = params.getSupportedPictureSizes();
		for (int i = 0; i < pictureSizes.size(); i++) {
			Size size = pictureSizes.get(i);
		}
	}

	/**
	 * 打印支持的聚焦模式
	 * 
	 * @param params
	 */
	public void printSupportFocusMode(Camera.Parameters params) {
		List<String> focusModes = params.getSupportedFocusModes();
		for (String mode : focusModes) {
		}
	}

	public class CameraSizeComparator implements Comparator<Camera.Size> {
		public int compare(Size lhs, Size rhs) {
			// TODO Auto-generated method stub
			if (lhs.width == rhs.width) {
				return 0;
			} else if (lhs.width > rhs.width) {
				return 1;
			} else {
				return -1;
			}
		}

	}
}
