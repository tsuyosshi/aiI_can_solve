package com.example.aicansolve;


import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import android.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;

import org.json.JSONObject;

public class AsyncHttpRequest extends AsyncTask<String, Void, String> {

    private Activity mainActivity;
    private Bitmap mImage;
    private HttpURLConnection httpConn;


    public AsyncHttpRequest(Activity activity, Bitmap image) {

        this.mainActivity = activity;
        this.mImage = image;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("AsyncHttpRequest", "doInBackground");
        String requestURL = strings[0];
        OutputStream outputStream;
        InputStream inputStream;

        StringBuffer result = new StringBuffer();

        try {
            URL url = new URL(requestURL);

            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(100000);
            httpConn.setReadTimeout(100000);
            httpConn.addRequestProperty("User-Agent", "Android");
            httpConn.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);

            ByteArrayOutputStream jpg = new ByteArrayOutputStream();
            mImage.compress(Bitmap.CompressFormat.JPEG, 100, jpg);
            byte[] data = jpg.toByteArray();
            String base64str = Base64.encodeToString(data, Base64.NO_WRAP);
            Log.d("URL", base64str);

            HashMap<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("image", base64str);
            JSONObject responseJsonObject = new JSONObject(jsonMap);
            String jsonText = responseJsonObject.toString();
            OutputStreamWriter out = new OutputStreamWriter(httpConn.getOutputStream());
            out.write(jsonText);
            out.flush();
            httpConn.connect();

            // HTTPレスポンスコード
            final int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // 通信に成功した
                // テキストを取得する
                final InputStream in = httpConn.getInputStream();
                String encoding = httpConn.getContentEncoding();
                if (null == encoding) {
                    encoding = "UTF-8";
                }
                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;
                // 1行ずつテキストを読み込む
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                bufReader.close();
                inReader.close();
                in.close();
            } else {
                // 通信が失敗した場合のレスポンスコードを表示
                System.out.println(status);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (httpConn != null) {
                // コネクションを切断
                httpConn.disconnect();
            }
        }
        Log.d("Debug", result.toString());
        return result.toString();
    }

}
