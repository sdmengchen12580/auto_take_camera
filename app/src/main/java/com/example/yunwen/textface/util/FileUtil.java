package com.example.yunwen.textface.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private static final  String TAG = "FileUtil";
	private static final File parentPath = Environment.getExternalStorageDirectory();
	private static   String storagePath = "";
	private static final String DST_FOLDER_NAME = "PlayCamera";

	public static String initPath(){
		if(storagePath.equals("")){
			storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
			File f = new File(storagePath);
			if(!f.exists()){
				f.mkdir();
			}
		}
		return storagePath;
	}

	public static boolean is_pic_ok=false;
	public static boolean  saveBitmap(Bitmap b){
		String path = initPath();
		/**时间搓当做图片的名字*/
//		long dataTake = System.currentTimeMillis();
//		String jpegName = path + "/" + dataTake +".jpg";
		/**图片的url*/
		String jpegName = path + "/" + "people.jpg";
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			is_pic_ok = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is_pic_ok;
	}
}
