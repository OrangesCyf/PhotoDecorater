package com.example.carmepreviewdemo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Environment;
import android.os.Handler;

public class FileUtil {
	private static final File parentPath = Environment
			.getExternalStorageDirectory();
	private static String storagePath = "";
	private static final String DST_FOLDER_NAME = "PlayCamera";

	/**
	 * 初始化保存路径
	 * 
	 * @return
	 */
	private static String initPath() {
		if (storagePath.equals("")) {
			storagePath = parentPath.getAbsolutePath() + "/" + DST_FOLDER_NAME;
			File f = new File(storagePath);
			if (!f.exists()) {
				f.mkdir();
			}
		}
		return storagePath;
	}

	/**
	 * 将图片保存到相册
	 * 
	 * @param b
	 */
	public static void saveBitmapToSDcard(Bitmap b, Context context,
			final OnPictureSavedListener listener) {

		String path = initPath();
		long dataTake = System.currentTimeMillis();
		String jpegName = dataTake + ".jpg";

		File file = new File(path, jpegName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			b.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
				Uri.fromFile(file)));

		MediaScannerConnection.scanFile(context,
				new String[] { file.toString() }, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					@Override
					public void onScanCompleted(final String path, final Uri uri) {
						if (listener != null) {
							new Handler().post(new Runnable() {

								@Override
								public void run() {
									listener.onPictureSaved(uri);
								}
							});
						}
					}
				});
	}

	/**
	 * 保存成功后回调
	 * */
	public interface OnPictureSavedListener {
		void onPictureSaved(Uri uri);
	}

}
