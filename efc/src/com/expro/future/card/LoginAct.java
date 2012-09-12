/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expro.future.card;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录画面
 *
 * @author MaYichao
 */
public class LoginAct extends Activity {

    private EditText etName;
    private EditText etPw;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        etName = (EditText) findViewById(R.id.login_name);
        etPw = (EditText) findViewById(R.id.login_pw);

    }

    public void goBack(View v) {
        finish();
    }

    public void doLogin(View v) {
        //收集用户名与密码
        String username = etName.getText().toString();
        String password = etPw.getText().toString();
        //密码加密
        password = Utils.passwordCrypto(password);
        
        JSONObject req = new JSONObject();
        try {
            //req.put("cellphone", username);
//            req.put("password", password);
            req.put("cellphone", "18912345678");
            req.put("password", "$2a$10$vsjb1rWQY53dlS4VoHsr9u.6YRcpBNOhi9ncdYFRR8ge3LDGgHJTK");
            req.put("org", 1);
            //发出登录申请
            String result = Utils.postToUrl(Utils.HOST + "signin", req.toString());
            JSONObject json = new JSONObject(result);
            Log.d("EFC", json.toString());
            //保存用户信息到后台服务中.
            Toast.makeText(this, "登录成功.", Toast.LENGTH_LONG);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        //成功
        //失败
    }
}
