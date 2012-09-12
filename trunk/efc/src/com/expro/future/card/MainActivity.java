package com.expro.future.card;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void clickSellerBtn(View btn) {
        startActivity(new Intent(this, SellerList.class));
    }
    public void clickUserCenter(View btn) {
        startActivity(new Intent(this, UserCenterAct.class));
    }
}
