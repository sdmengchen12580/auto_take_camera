package com.example.yunwen.textface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.yunwen.textface.camera.CameraInterface;
import com.example.yunwen.textface.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TwoActivity extends AppCompatActivity {


    ImageView imageView;
    String img_url;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        imageView = (ImageView) findViewById(R.id.img);

        try {
            img_url= FileUtil.initPath() + "/" + "people.jpg";
            FileInputStream fis = new FileInputStream(img_url);
            bitmap=BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        findViewById(R.id.tuihui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TwoActivity.this,CameraActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
}

/**图片选转的方法*/
    public Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(degrees, (float) bitmap.getWidth()/2,
                    (float) bitmap.getHeight()/2);
            try {
                Bitmap tempBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);
                if (bitmap != tempBm) {
                    // bitmap回收
                    bitmap.recycle();
                    bitmap = tempBm;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
