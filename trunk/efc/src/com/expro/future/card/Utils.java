/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expro.future.card;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;

/**
 *
 * @author MaYichao
 */
public class Utils {

    private static final String DEBUG_TAG = "EFC";
    public static final String HOST = "http://192.168.0.115:10080/";
    public static final String IMG_HOST = "http://storage.aliyun.com/";

    /**
     * 根据Layout文件创建一个view
     */
    public static View createView(Activity act, int id, ViewGroup g) {
        return act.getLayoutInflater().inflate(id, g);
    }

    /**
     * 从网络取得图片
     */
    public static Bitmap getImage(Context c, String url) throws IOException {
        InputStream is = null;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            is = conn.getInputStream();
            //获取Bitmap的引用
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static String getTextFromUrl(String url) {
        try {
            return downloadUrl(url);
        } catch (IOException ex) {
            return null;
        }
    }

    public static String postToUrl(String url, String req) {
        try {
            return downloadUrl(url, "POST", req);
        } catch (IOException ex) {
            return null;
        }
    }

    private static String downloadUrl(String myurl) throws IOException {
        return downloadUrl(myurl, "GET", null);
    }

    private static String downloadUrl(String myurl, String type, String req) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /*
                     * milliseconds
                     */);
            conn.setConnectTimeout(15000 /*
                     * milliseconds
                     */);
            conn.setRequestMethod(type);


            if (req != null) {
                conn.setDoOutput(true);
            }
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            //POST 状态下,需要通过流传入数据.
            if (req != null) {
                OutputStream output = conn.getOutputStream();
                output.write(req.getBytes());
                output.close();
            }
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            StringBuilder sb = new StringBuilder();

            String contentAsString = null;
//            while ((contentAsString = readIt(is, len)) != null){
            contentAsString = readIt(is, len);
//                sb.append(contentAsString);
//            }
//            return sb.toString();
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    // Reads an InputStream and converts it to a String.

    private static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];

        StringBuilder sb = new StringBuilder();
        int f;
        do {
            f = reader.read(buffer);
            sb.append(buffer);
        } while (f != -1);
        return sb.toString();
    }

    /**
     * 从后台下载一个ImageView图片
     *
     * @param context
     * @param imageView
     * @param url
     */
    public static void downloadImageViewInBackground(Context context, ImageView imageView, String url) {
        new ImageViewDownloadTask().execute(context, imageView, url);
    }

    /**
     * 取得图片的完整url
     *
     * @param subUrl 相关url.
     * @return
     */
    public static String getImageUrl(String subUrl) {
        return IMG_HOST + subUrl;
    }
    
    /**
     * 将密码用crypto 方式加密
     * @param password
     * @return 
     */
    public static String passwordCrypto(String password){
//        try {
//            SecretKeyFactory.getInstance("11");
//            Cipher c = Cipher.getInstance(password);
//            return new String(c.doFinal());
//            c.init(Cipher.DECRYPT_MODE, null);
        
            return DESUtils.encrypt(password, DESUtils.getSecretKey("10"));
            
//        } catch (GeneralSecurityException ex) {
//            Log.i(DEBUG_TAG, "密码加密失败", ex);
////            ex.printStackTrace();
//            return password;
//        } 
    }
    
    
    
}
