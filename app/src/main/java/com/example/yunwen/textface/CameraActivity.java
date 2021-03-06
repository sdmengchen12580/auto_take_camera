package com.example.yunwen.textface;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import com.example.yunwen.textface.camera.CameraInterface;
import com.example.yunwen.textface.camera.preview.CameraSurfaceView;
import com.example.yunwen.textface.mode.GoogleFaceDetect;
import com.example.yunwen.textface.person.Changeactivity;
import com.example.yunwen.textface.ui.FaceView;
import com.example.yunwen.textface.util.DisplayUtil;
import com.example.yunwen.textface.util.EventUtil;
import com.example.yunwen.textface.util.FileUtil;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class CameraActivity extends Activity {
    /**
     * 自定义的surfaceview
     */
    CameraSurfaceView surfaceView = null;
    /**
     * 切换前后置
     */
    ImageButton switchBtn;
    /**
     * 自定义imageview
     */
    FaceView faceView;


    float previewRate = -1f;
    private MainHandler mMainHandler = null;
    GoogleFaceDetect googleFaceDetect = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        /**初始化控件*/
        initUI();
        /**初始化参数*/
        initViewParams();
        mMainHandler = new MainHandler();
        /**初始化谷歌人脸检测*/
        googleFaceDetect = new GoogleFaceDetect(getApplicationContext(), mMainHandler);
        /**handler通知————开始人脸检测*/
        mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopGoogleFaceDetect();
    }

    /**
     * 初始化控件
     */
    /**
     * 定时器每0.2s去检测是否有人
     */
    Timer timer_haspeople = new Timer();
    /**
     * 定时器每0.2s去检测是否有人
     */
    Timer timer_takepic_ok = new Timer();

    private void initUI() {
        /**自定义surfaceview*/
        surfaceView = findViewById(R.id.camera_surfaceview);
        /**自定义faceview*/
        faceView = findViewById(R.id.face_view);
    }

    public  void open_timer(){
        /**循环判断是否有人*/
        timer_haspeople.schedule(new TimerTask() {
            @Override
            public void run() {
                if (faceView.isthePeople()) {
                    timer_haspeople.cancel();
                    takePicture();
                    /**oppo手机的需要在此停止人脸检测*/
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopGoogleFaceDetect();
                        }
                    });
                    timer_takepic_ok.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (FileUtil.is_pic_ok) {
                                timer_takepic_ok.cancel();
                                /**is_pic_ok代表图片已经拍好了*/
                                FileUtil.is_pic_ok=false;
                                startActivity(new Intent(CameraActivity.this, TwoActivity.class));
                            }
                        }
                    }, 0, 200);
                } else {
                    return;
                }
            }
        }, 0, 500);
    }

    /**
     * 初始化surfaceview的参数
     */
    private void initViewParams() {
        LayoutParams params = surfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this);
        surfaceView.setLayoutParams(params);
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /**不停的小框移动，跟着人脸*/
                case EventUtil.UPDATE_FACE_RECT:
                    Face[] faces = (Face[]) msg.obj;
                    faceView.setFaces(faces);
                    open_timer();
                    break;
                /**开始人脸检测*/
                case EventUtil.CAMERA_HAS_STARTED_PREVIEW:
                    startGoogleFaceDetect();
                    break;
            }
            super.handleMessage(msg);
        }
    }


    /**
     * 拍照片
     */
    private void takePicture() {
        CameraInterface.getInstance().doTakePicture();
    }

    /**
     * 开启surfaceview
     */
    private void startGoogleFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if (params.getMaxNumDetectedFaces() > 0) {
            if (faceView != null) {
                /**一定要清空，不然之前人的位置会一直显示*/
                faceView.clearFaces();
                faceView.setVisibility(View.VISIBLE);
            }
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(googleFaceDetect);
            /**此时已经开始了人脸检测*/
          CameraInterface.getInstance().getCameraDevice().startFaceDetection();
         }
    }
    /**
     * 关闭surfaceview
     */
    private void stopGoogleFaceDetect() {
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if (params.getMaxNumDetectedFaces() > 0) {
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(null);
            CameraInterface.getInstance().getCameraDevice().stopFaceDetection();
            faceView.clearFaces();
        }
    }

}
