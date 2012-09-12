/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expro.future.card;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商户主页面
 *
 * @author MaYichao
 */
public class ShopMain extends Activity {

    private static final String DEBUG_TAG = "EFC";
    private JSONObject shop = null;
    private ListView portListView = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.shop_main);
        //载入商户信息.
        Intent it = getIntent();
        try {
            initView(it);
        } catch (JSONException ex) {
            Toast.makeText(this, "解析数据失败", Toast.LENGTH_SHORT);
            throw new RuntimeException("解析数据失败", ex);
        }
    }

    public void goBack(View v) {
        finish();
    }

    /**
     * 初始化画面
     *
     * @param it
     */
    private void initView(Intent it) throws JSONException {
        shop = new JSONObject(it.getStringExtra("shop"));

        TextView titleTxt = (TextView) findViewById(R.id.shopName);
        titleTxt.setText(shop.getString("short_name"));
        TextView descTxt = (TextView) findViewById(R.id.shopText);
        descTxt.setText(shop.getString("full_name"));
        ImageView imageView = (ImageView) findViewById(R.id.shopImage);
        Utils.downloadImageViewInBackground(this, imageView, Utils.getImageUrl(shop.getString("charter_img_path")));
        
        portListView = (ListView)findViewById(R.id.shopPortList);
        
        //载入门店信息.
        new AsyncTask<Long, Object, JSONArray>() {

            @Override
            protected JSONArray doInBackground(Long... arg0) {
                //查询门店信息.
                try {
                    String url = Utils.HOST + "stores/listPublic/" + arg0[0].longValue();
                    Log.d(DEBUG_TAG, "查询门店数据:"+url);
                    String s = Utils.getTextFromUrl(url);
                    JSONObject json = new JSONObject(s);
                    return json.getJSONArray("stores");
                } catch (JSONException ex) {
                    Log.w(DEBUG_TAG, "查询门店失败.",ex);
                    Toast.makeText(ShopMain.this, "查询门店数据失败.", Toast.LENGTH_SHORT).show();
                    cancel(true);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(JSONArray result) {
                portListView.setAdapter(new ShopPortListAdapter(result));
            }
            
            
        }.execute(shop.getLong("_id"));
    }

    /**
     * 点击申请按钮.
     *
     * @param v
     */
    public void clickReq(View v) {
        //先检查是否登录.未登录先登录
        startActivity(new Intent(this, LoginAct.class));

        //TODO 登录的,发出申请.
    }
    
    
    /** 门店列表显示适配器 */
    private class ShopPortListAdapter extends BaseAdapter{
        JSONArray portArray;
        public ShopPortListAdapter(JSONArray portArray){
            this.portArray = portArray;
        }
        public int getCount() {
            return portArray.length();
        }

        public Object getItem(int arg0) {
            return getJson(arg0);
        }
        
        private JSONObject getJson(int i){
            try {
                return portArray.getJSONObject(i);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

        public long getItemId(int arg0) {
            try {
                JSONObject json = getJson(arg0);
                return json.getLong("_id");
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {
            try {
                if (arg1 == null) {
                    arg1 = Utils.createView(ShopMain.this, R.layout.shop_port_item, null);
                }
                if (arg0 == 0) {
                    //第一行;
                    arg1.setBackgroundResource(R.drawable.list_above_nor);
                }else if(arg0 == getCount() - 1){
                    //最后一行
                    arg1.setBackgroundColor(R.drawable.list_below_nor);
                }
                final JSONObject port = getJson(arg0);
                //调整显示信息.
                //店名:
                ((TextView)arg1.findViewById(R.id.name)).setText(port.getString("name"));
                //电话
                ((TextView)arg1.findViewById(R.id.phone)).setText(port.getString("district_code"));
                ((Button)arg1.findViewById(R.id.callBtn)).setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
                        try {
                            callPhone(port.getString("district_code"));
                        } catch (JSONException ex) {
                            Log.d(DEBUG_TAG, "拨号失败",ex);
                        }
                    }
                });
                //地址
                ((TextView)arg1.findViewById(R.id.address)).setText(port.getString("address"));
                
                
                return arg1;
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            }
        }
        
    }
    
    /** 打电话 */
    public void callPhone(String number){
        Uri u = Uri.parse("tel:"+number);
        Intent call = new Intent(Intent.ACTION_CALL, u);
        startActivity(call);
    }
}
