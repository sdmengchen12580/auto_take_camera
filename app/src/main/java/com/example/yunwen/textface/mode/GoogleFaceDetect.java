package com.example.yunwen.textface.mode;


import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.yunwen.textface.util.EventUtil;

public class GoogleFaceDetect implements FaceDetectionListener {
	private Context mContext;
	private Handler mHander;
	public GoogleFaceDetect(Context c, Handler handler){
		mContext = c;
		mHander = handler;
	}
	@Override
	public void onFaceDetection(Face[] faces, Camera camera) {
		Log.i("____________________________", "在检测中...");
		if(faces != null){
			Message m = mHander.obtainMessage();
			m.what = EventUtil.UPDATE_FACE_RECT;
			m.obj = faces;
			m.sendToTarget();
		}
	}
}
