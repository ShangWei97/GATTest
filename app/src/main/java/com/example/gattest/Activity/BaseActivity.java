package com.example.gattest.Activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gattest.ActivityCollector;

/**
 * Created by 商蔚 on 2017/7/15.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
