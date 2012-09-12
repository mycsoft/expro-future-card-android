/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expro.future.card;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商户列表
 *
 * @author MaYichao
 */
public class SellerList extends Activity {

    private static final String DEBUG_TAG = "EFC";
    private GridView gridView;
    private static final String HOST = "http://192.168.0.115:10080/";
    private static final String IMG_HOST = "http://storage.aliyun.com/";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.shop_list);

        gridView = (GridView) findViewById(R.id.grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //点击打开商户页面.
                Intent i = new Intent(SellerList.this, ShopMain.class);
                i.putExtra("shop", ((JSONObject)arg0.getAdapter().getItem(arg2)).toString());
                startActivity(i);
            }
        });

        // 取出商户列表

        // Gets the URL from the UI's text field.
        String stringUrl = HOST + "merchants/listActive";
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageText().execute(stringUrl);
//            Toast.makeText(this, stringUrl, RESULT_OK);
        } else {
//            textView.setText("No network connection available.");
            Toast.makeText(this, stringUrl + "连接失败", Toast.LENGTH_LONG).show();
        }

    }

    public void goBack(View v) {
        finish();
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a 
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageText extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
//            try {
//                return downloadUrl(urls[0]);
//            } catch (IOException e) {
//                return "Unable to retrieve web page. URL may be invalid.";
//            }
            return Utils.getTextFromUrl(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String result) {
//            textView.setText(result);
//            Toast.makeText(SellerList.this, result, Toast.LENGTH_LONG).show();
            Log.d(DEBUG_TAG, "json:" + result.length());
            try {
                //创建Json对象
                JSONObject json = new JSONObject(result);
//                JSONObject json = new JSONObject("{\"merchants\":[{\"id\":1}]}");
                JSONArray shopList = json.getJSONArray("merchants");
//                for (int i = 0; i < shopList.length(); i++) {
//                    JSONObject shop = shopList.getJSONObject(i);
//                    TextView tv = new TextView(SellerList.this);
//                    tv.setText(shop.getString("short_name"));
//                    gridView.addView(tv);
//                }
                ShopAdapter adapter = new ShopAdapter(SellerList.this, shopList);
                gridView.setAdapter(adapter);
            } catch (JSONException ex) {
                Log.w(DEBUG_TAG, ex.getMessage());
                ex.printStackTrace();
//                Logger.getLogger(SellerList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private class ShopAdapter extends BaseAdapter {

        private Context context;
        private JSONArray shopList;

        public ShopAdapter(Context c, JSONArray shopList) {
            context = c;
            this.shopList = shopList;
        }

        public int getCount() {
//            return shopList.length();
            return 10;
        }

        public Object getItem(int arg0) {
            return getJSON(arg0);
        }

        public long getItemId(int arg0) {
            try {
                return getJSON(arg0).getLong("_id");
            } catch (JSONException ex) {
                return 0;
            }
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {
            try {
                if (arg1 == null) {
                    arg1 = Utils.createView(SellerList.this, R.layout.shop_list_shop, null);
//                    arg1 = getLayoutInflater().inflate(R.layout.shop_list_shop, arg2);
//                    arg1 = new TextView(arg2.getContext());
                }
//                TextView tv = (TextView) arg1;
                JSONObject shop = getJSON(arg0);
                TextView tv = (TextView) arg1.findViewById(R.id.shopText);
                tv.setText(shop.getString("short_name"));
                ImageView iv = (ImageView) arg1.findViewById(R.id.shopIcon);
                //iv.setImageBitmap(Utils.getImage(context, IMG_HOST+ shop.getString("logo_img_path")));
                downloadShopIcon(context, iv, IMG_HOST + shop.getString("logo_img_path"));
                return arg1;
//            } catch (IOException ex) {
//                Log.i(DEBUG_TAG, "图片下载失败");
//                throw new RuntimeException("图片下载失败",ex);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

        private JSONObject getJSON(int i) {
            try {
                return shopList.getJSONObject(0);
            } catch (JSONException ex) {
                return null;
            }
        }
    }

    /**
     * 后台下载图片
     */
    private void downloadShopIcon(Context c, ImageView imageView, String url) {
        new AsyncTask<Object, Object, Bitmap>() {

            ImageView iv;
            Context c;

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
        }.execute(c, imageView, url);
    }
}
