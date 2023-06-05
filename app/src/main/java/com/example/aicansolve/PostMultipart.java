//package com.example.aicansolve;
//
//import android.graphics.Bitmap;
//import android.util.Log;
//
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by solid_red on 2017/03/13.
// */
//
//public class PostMultipart {
//    private final String boundary;
//    private static final String LINE_FEED = "\r\n";
//    private HttpURLConnection httpConn;
//    private String charset;
//    private OutputStream outputStream;
//    private PrintWriter writer;
//
//    public PostMultipart(String requestURL, String charset)
//            throws IOException {
//
//        Log.d("PostMultipart", "PostMultipart");
//
//        this.charset = charset;
//
//        boundary = "===" + System.currentTimeMillis() + "===";
//        URL url = new URL(requestURL);
//        httpConn = (HttpURLConnection) url.openConnection();
//        httpConn.setUseCaches(false);
//        httpConn.setDoOutput(true);
//        httpConn.setDoInput(true);
//        httpConn.setRequestMethod("POST");
//        outputStream = new BufferedOutputStream(httpConn.getOutputStream());
//    }
//
//    public String postImage(Bitmap image){
//        try{
//            httpConn.connect();
//            ByteArrayOutputStream jpg = new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.JPEG, 100, jpg);
//            outputStream.write(jpg.toByteArray());
//            outputStream.flush();
//        } catch (IOException e) {
//
//        }
//
//    }
//
//    // フォームフィールド追加
//    public void addField(String name, String value) {
//        writer.append("--" + boundary).append(LINE_FEED);
//        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
//                .append(LINE_FEED);
//        writer.append("Content-Type: text/plain; charset=" + charset).append(
//                LINE_FEED);
//        writer.append(LINE_FEED);
//        writer.append(value).append(LINE_FEED);
//        writer.flush();
//    }
//
//    // ファイル追加
////    public void addFile(String name, File uploadFile)
////            throws IOException {
////        String fileName = uploadFile.getName();
////        writer.append("--" + boundary).append(LINE_FEED);
////        writer.append(
////                        "Content-Disposition: form-data; name=\"" + name
////                                + "\"; filename=\"" + fileName + "\"")
////                .append(LINE_FEED);
////        writer.append(
////                        "Content-Type: "
////                                + URLConnection.guessContentTypeFromName(fileName))
////                .append(LINE_FEED);
////        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
////        writer.append(LINE_FEED);
////        writer.flush();
////
////        FileInputStream inputStream = new FileInputStream(uploadFile);
////        byte[] buffer = new byte[4096];
////        int bytesRead = -1;
////        while ((bytesRead = inputStream.read(buffer)) != -1) {
////            outputStream.write(buffer, 0, bytesRead);
////        }
////        outputStream.flush();
////        inputStream.close();
////        writer.append(LINE_FEED);
////        writer.flush();
////    }
//
//    public void addImage(String name, Bitmap iamage)
//            throws IOException {
////        String fileName = uploadFile.getName();
////        writer.append("--" + boundary).append(LINE_FEED);
////        writer.append(
////                        "Content-Disposition: form-data; name=\"" + name
////                                + "\"; filename=\"" + fileName + "\"")
////                .append(LINE_FEED);
////        writer.append(
////                        "Content-Type: "
////                                + URLConnection.guessContentTypeFromName(fileName))
////                .append(LINE_FEED);
////        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
////        writer.append(LINE_FEED);
////        writer.flush();
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//    }
//
//    // ヘッダー追加
//    public void addHeader(String name, String value) {
//        writer.append(name + ": " + value).append(LINE_FEED);
//        writer.flush();
//    }
//
//    // 実行
//    public List<String> post() throws IOException {
//        List<String> response = new ArrayList<String>();
//        writer.append(LINE_FEED).flush();
//        writer.append("--" + boundary + "--").append(LINE_FEED);
//        writer.close();
//
//        // checks server's status code first
//        int status = httpConn.getResponseCode();
//        if (status == HttpURLConnection.HTTP_OK) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    httpConn.getInputStream()));
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                response.add(line);
//            }
//            reader.close();
//            httpConn.disconnect();
//        } else {
//            throw new IOException("Send Fail: " + status);
//        }
//        return response;
//    }
//}