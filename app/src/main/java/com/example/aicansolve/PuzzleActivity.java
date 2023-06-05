package com.example.aicansolve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PuzzleActivity extends AppCompatActivity {

    final int sx = 50;
    final int sy = 50;
    final int w = 250;
    final int h = 250;

    int W;
    int H;

    int[] X;
    int[] Y;

    int empty_block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_puzzle);

        Intent intent = getIntent();
        String str = intent.getStringExtra("path");
        String json;
        File file = new File(str);
        InputStream inputStream = null;

        Bitmap image;

        try {
            inputStream = new FileInputStream(file);
            image = BitmapFactory.decodeStream(inputStream);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String requestUrl = "http://192.168.11.34:80/read_pazzle";
        AsyncHttpRequest task = new AsyncHttpRequest(this, image);
        try {
            json = task.execute(requestUrl).get();
            Log.d("Puzzle Activity", json);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map;

        try {
            map = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        int size = (int) map.get("size");
        final ArrayList<Integer> numbers = (ArrayList<Integer>) map.get("numbers");
        W = (int) Math.sqrt(size);
        H = (int) Math.sqrt(size);
        X = new int[size];
        Y = new int[size];

        FrameLayout fl = new FrameLayout(this);
        setContentView(fl);

        Button btn1 = new Button(this);
        btn1.setText("I cannot solve...");
        btn1.setGravity(Gravity.CENTER);
        btn1.setX(350);
        btn1.setY(1100);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SolverActivity.class);
                intent.putExtra("json", json);
                startActivity(intent);
            }
        });
        fl.addView(btn1, new FrameLayout.LayoutParams(500, 200));


        TextView[] blocks = new TextView[size];

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; ++x) {
                int id = y*H + x;
                int num = numbers.get(id);
                String val = String.valueOf(num);

                X[id] = x;
                Y[id] = y;

                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);

                if (num == -1) {
                    empty_block = id;
                }
                else {

                    blocks[id] = new TextView(this);
                    blocks[id].setId(id);
                    blocks[id].setBackgroundColor(Color.GRAY);
                    blocks[id].setId(id);
                    blocks[id].setText(val);
                    blocks[id].setTextSize(50);
                    blocks[id].setTextColor(Color.BLACK);
                    blocks[id].setGravity(Gravity.CENTER);
                    blocks[id].setClickable(true);
                    blocks[id].setX(sx + x*w);
                    blocks[id].setY(sy + y*h);

                    blocks[id].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int id = v.getId();
                            int x = X[id];
                            int y = Y[id];
                            System.out.println(empty_block);
                            if (isMovableBlock(v)) {
                                int ex = X[empty_block];
                                int ey = Y[empty_block];
                                v.setX(sx + w*ex);
                                v.setY(sy + h*ey);
                                v.setId(empty_block);
                                empty_block = y*H + x;
                            }
                        }
                    });

                    fl.addView(blocks[id], w, h);
                }
            }
        }

        for (int y = 0; y < H + 1; y++) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w * W,5);
            View line = new View(this);
            line.setBackgroundColor(Color.BLACK);
            lp.leftMargin = sx;
            lp.topMargin = sy + y*h;
            fl.addView(line, lp);
        }

        for (int x = 0; x < W + 1; x++) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(5,h * H);
            View line = new View(this);
            line.setBackgroundColor(Color.BLACK);
            lp.leftMargin = sx + x*w;
            lp.topMargin = sy;
            fl.addView(line, lp);
        }
    }


    private Boolean isMovableBlock(View view) {
        int id = view.getId();
        int x = X[id];
        int y = Y[id];
        int ex = (int) empty_block % W;
        int ey = (int) empty_block / H;

        int[] dx = { 1, -1, 0, 0};
        int[] dy = { 0, 0, 1, -1};

        for (int i = 0; i < 4; ++i) {
            int nx = ex + dx[i];
            int ny = ey + dy[i];
            if (nx < 0 || nx >= W || ny < 0 || ny >= H) {
                continue;
            }
            if (nx == x && ny == y) {
                return true;
            }
        }
        return false;
    }
}