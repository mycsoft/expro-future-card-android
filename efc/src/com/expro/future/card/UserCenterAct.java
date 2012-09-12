/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.expro.future.card;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 *
 * @author MaYichao
 */
public class UserCenterAct extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.user_center);
    }
    
    public void goBack(View v) {
        finish();
    }
}
