package com.example.aicansolve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PicturePreviewActivity extends AppCompatActivity {

    private final int FP = ViewGroup.LayoutParams.MATCH_PARENT;
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_preview);
        Intent intent = getIntent();
        String str = intent.getStringExtra("bitmap");
        File file = new File(str);
        InputStream inputStream = null;

        Button btn = findViewById(R.id.button);
        btn.setX(450);
        btn.setY(1300);
        btn.setTextColor(Color.BLACK);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), PuzzleActivity.class);
                intent.putExtra("path", str);
                startActivity(intent);
            }
        });

        try {
            inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}