package com.example.aicansolve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SolverActivity extends AppCompatActivity {

    final int sx = 50;
    final int sy = 50;
    final int w = 250;
    final int h = 250;

    int W;
    int H;

    int[] X;
    int[] Y;

    int empty_block;

    int step_current;

    int step_total;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver);
        Intent intent = getIntent();
        String json = intent.getStringExtra("json");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map;

        try {
            map = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        int size = (int) map.get("size");
        ArrayList<Integer> numbers = (ArrayList<Integer>) map.get("numbers");
        ArrayList<Integer> ans = (ArrayList<Integer>) map.get("ans");

        W = (int) Math.sqrt(size);
        H = (int) Math.sqrt(size);
        X = new int[size];
        Y = new int[size];

        FrameLayout fl = new FrameLayout(this);

        setContentView(fl);


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
                    blocks[id].setText(val);
                    blocks[id].setTextSize(50);
                    blocks[id].setTextColor(Color.BLACK);
                    blocks[id].setGravity(Gravity.CENTER);
                    blocks[id].setX(sx + x*w);
                    blocks[id].setY(sy + y*h);

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

        Button btn1 = new Button(this);
        btn1.setText("Next");
        btn1.setGravity(Gravity.CENTER);
        btn1.setX(350);
        btn1.setY(1100);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (step_current >= step_total) {
                    return;
                }

                int id = ans.get(step_current - 1);
                int x = X[id];
                int y = Y[id];
                int ex = X[empty_block];
                int ey = Y[empty_block];
                for (int i = 0; i < size; i++) {
                    if (blocks[i] == null) {
                        continue;
                    } else if (blocks[i].getId() == id) {
                        blocks[i].setX(sx + w*ex);
                        blocks[i].setY(sy + h*ey);
                        blocks[i].setId(empty_block);
                        empty_block = id;
                        break;
                    }
                }

                step_current = step_current + 1;

                textView.setText(String.valueOf(step_current) + " / " + String.valueOf(step_total));
            }
        });
        fl.addView(btn1, new FrameLayout.LayoutParams(500, 200));

        step_current = 1;
        step_total = ans.size() + 1;

        textView = new TextView(this);
        textView.setText(String.valueOf(step_current) + " / " + String.valueOf(step_total));
        textView.setGravity(Gravity.CENTER);
        textView.setX(350);
        textView.setY(1300);
        textView.setTextSize(30);
        fl.addView(textView, new FrameLayout.LayoutParams(500, 200));

    }

    // solver 未実装実装 -> API 側で実装

    private class PuzzleSolver {

        private class State {

            private int W;
            private int H;
            private ArrayList<Integer> numbers;
            private int empty_block;

            State(int W, int H, ArrayList<Integer> numbers) {
                this.W = W;
                this.H = H;
                this.numbers = numbers;

                for (int i = 0; i < numbers.size(); i++) {
                    int num = numbers.get(i);
                    if (num == -1) {
                        empty_block = i;
                        break;
                    }
                }
            }

            public int getIdFromPos(int x, int y) {
                return y*H +x;
            }

            public int[] getPosFromId(int id) {
                int[] pos = new int[2];
                pos[0] = id % W;
                pos[1] = (int) id / H;
                return pos;
            }

            public Boolean isMovableBlock(int id) {
                int x = getPosFromId(id)[0];
                int y = getPosFromId(id)[1];
                int ex = getPosFromId(empty_block)[0];
                int ey = getPosFromId(empty_block)[1];

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

        private State initState;
        private Map<ArrayList<Integer>, Boolean> visited = new HashMap<ArrayList<Integer>, Boolean>();

        PuzzleSolver(int W, int H, ArrayList<Integer> numbers) {

            this.initState = new State(W, H, numbers);

        }

        public ArrayList<Integer> Run() {
            return null;
        }

        private ArrayList<Integer> Solve() {
            return null;
        }


    }
}