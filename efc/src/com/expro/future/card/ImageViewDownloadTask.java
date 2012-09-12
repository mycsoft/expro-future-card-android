/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expro.future.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;

/**
 *
 * @author MaYichao
 */
public class ImageViewDownloadTask extends AsyncTask<Object, Object, Bitmap> {

    private static final String DEBUG_TAG = "EFC";
    private ImageView iv;
    private Context c;

    @Override
    protected Bitmap doInBackground(Object... arg0) {
        c = (Context) arg0[0];
        iv = (ImageView) arg0[1];
        try {
            //return downloadIcon((String)arg0[2]);
            return Utils.getImage(c, (String) arg0[2]);
        } catch (IOException ex) {
            Log.i(DEBUG_TAG, "后台图片下载失败.");
            throw new RuntimeException("图片下载失败", ex);
        }
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        iv.setImageBitmap(result);
    }
    /**
     * 执行下载
     * @param c
     * @param imageView
     * @param url 
     */
    public AsyncTask<Object, Object, Bitmap> execute(Context c,ImageView imageView,String url){
        return super.execute(c,imageView,url);
    }
}