package com.example.aicansolve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int FP = ViewGroup.LayoutParams.MATCH_PARENT;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraPreviewsActivity.class);
                startActivity(intent);
            }
        });

//        Resources r = getResources();

//        Bitmap image = BitmapFactory.decodeResource(r, R.drawable.slide_4x4_001);
//        image = Bitmap.createScaledBitmap(image, 2182, 3086, true);

//        ImageView imageView = new ImageView(this);
//        imageView.setImageBitmap(image);
//        setContentView(imageView, new LinearLayout.LayoutParams(WC, WC));


//        String requestUrl = "http://192.168.32.190/read_pazzle";
//        AsyncHttpRequest task = new AsyncHttpRequest(this, image);
//        task.execute(requestUrl);

//        try {
//            // アップロード先と文字コードで初期化
//            Log.d("MainActivity", "onCreate");
//            PostMultipart multipart = new PostMultipart("http://10.0.2.2:5000/", "UTF-8");
//
//            // 通常パラメータ追加
//            multipart.addField("hoge", "fuga");
//
//            // 画像ファイルの場合
//            File picture_one = new File("res/drawable/slide_4x4_001.jpg");
//            multipart.addFile("file1", picture_one);
//
//            List<String> response = multipart.post();
//
//
//
//        } catch (IOException e) {
//            Log.e("ERROR", e.getMessage());
//        }

    }
}