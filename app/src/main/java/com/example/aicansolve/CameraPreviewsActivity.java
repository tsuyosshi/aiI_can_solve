package com.example.aicansolve;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.Manifest;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Executable;
import android.util.Base64;
import java.util.List;

public class CameraPreviewsActivity extends Activity {

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    Camera mCamera;

    Bitmap mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        FrameLayout fl = new FrameLayout(this);
        setContentView(fl);

        mSurfaceView = new SurfaceView(this);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolderCallback());

        Button btn1 = new Button(this);
        btn1.setText("撮影");
        btn1.setX(240);
        btn1.setY(1500);
        btn1.setOnClickListener(new TakePictureClickListener());
        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(200,150);

        Button btn2 = new Button(this);
        btn2.setText("次へ");
        btn2.setX(720);
        btn2.setY(1500);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExternalStorageWritable()) {
                    Intent intent = new Intent(getApplicationContext(), PicturePreviewActivity.class);
                    Bitmap image = mImage;
                    String imgName = "image.jpg";
                    int w = image.getWidth();
                    int h = image.getHeight();
                    Matrix m = new Matrix();
                    m.setRotate(90);
                    image = Bitmap.createBitmap(image, 0, 0, w, h, m, false);

                    File imageFile = new File(getFilesDir(), imgName);
                    FileOutputStream out;
                    try {
                        out = new FileOutputStream(imageFile);
                        image.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    intent.putExtra("bitmap", imageFile.getAbsolutePath());
                    startActivity(intent);
                }
            }
        });
        FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(200,150);

        fl.addView(mSurfaceView);
        fl.addView(btn1, lp1);
        fl.addView(btn2, lp2);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }

    class SurfaceHolderCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
            try {
                int openCameraType = Camera.CameraInfo.CAMERA_FACING_BACK;
                if (openCameraType <= Camera.getNumberOfCameras()) {
                    mCamera = Camera.open(openCameraType);
                    mCamera.setPreviewDisplay(surfaceHolder);
                    mCamera.setDisplayOrientation(90);
                } else {
                    Log.d("Camera Sample", "cannot bind camera");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            setPreviewSize(i1, i2);
            mCamera.startPreview();
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        protected void setPreviewSize(int width, int height) {
            Camera.Parameters params = mCamera.getParameters();
            List<Camera.Size> supported = params.getSupportedPreviewSizes();
            if (supported != null) {
                for (Camera.Size size : supported) {
                    if (size.width <= width && size.height <= height) {
                        params.setPreviewSize(size.width, size.height);
                        mCamera.setParameters(params);
                        break;
                    }
                }
            }
        }
    }

    class TakePictureClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d("TakePictureClickListener", "onClick");
            mCamera.takePicture(null, null , new TakePictureCallback());
            Log.d("TakePictureClickListener", "endTakePicture");
        }
    }


    class TakePictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("TakePictureCallback", "onPictureTaken");
            mImage = BitmapFactory.decodeByteArray(data, 0, data.length, null);
            Log.d("TakePictureCallback", String.valueOf(data.length));
            camera.startPreview();
        }
    }
}
