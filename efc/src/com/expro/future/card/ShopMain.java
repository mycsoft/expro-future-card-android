/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expro.future.card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
}
