package com.example.gattest.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.gattest.R;
import com.example.gattest.constant.HttpConstant;
import com.example.gattest.request.GetCodeReqBean;
import com.example.gattest.request.LoginReqBean;
import com.example.gattest.utils.HttpUtils;



/**
 * @author shangwei
 * 登录
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText uname = null;
    private EditText upswd = null;
    private CheckBox checkboxButton = null;
    private Button login = null;
    private TextView register;
    SharedPreferences sp = null;
    private String name;
    private String pswd;
    private LinearLayout linearLayout_change;
    private Button button_change;
    private Button button_getCode;
    private TextView textView_change;
    private EditText editText_1;
    private EditText editText_2;
    private EditText editText_3;
    private EditText editText_4;
    private Handler handler;

    // 构建Runnable对象，在runnable中更新界面
    Runnable   runnableUi = new Runnable() {
        @Override
        public void run() {
            //更新界面
            login.setVisibility(View.VISIBLE);
            button_getCode.setVisibility(View.GONE);
        }
    };

    @Override
      protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_login);
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        init();
        register=(TextView)findViewById(R.id.textview_register);
        register.setOnClickListener(v -> {
            Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

      }

     public void init() {
         uname = (EditText) findViewById(R.id.login_edit_account);
         upswd = (EditText) findViewById(R.id.login_edit_pwd);
         checkboxButton = (CheckBox) findViewById(R.id.Login_Remember);
         login = (Button) findViewById(R.id.login_btn_login);
         linearLayout_change = (LinearLayout) findViewById(R.id.login_layout_code);
         button_change = (Button) findViewById(R.id.change);
         textView_change = (TextView) findViewById(R.id.text_change);
         button_getCode = (Button) findViewById(R.id.login_btn_getCode);
         handler = new Handler();
         editText_1 = (EditText) findViewById(R.id.et_inputPickupNum1);
         editText_2 = (EditText) findViewById(R.id.et_inputPickupNum2);
         editText_3 = (EditText) findViewById(R.id.et_inputPickupNum3);
         editText_4 = (EditText) findViewById(R.id.et_inputPickupNum4);
         if (sp.getBoolean("checkboxBoolean", false))
         {
             uname.setText(sp.getString("uname", null));
             upswd.setText(sp.getString("upswd", null));
             checkboxButton.setChecked(true);
         }
         login.setOnClickListener(this);
         button_change.setOnClickListener(this);
         button_getCode.setOnClickListener(this);
    }

     @Override
     public void onClick(View v) {
         if (v == login) {
             name = uname.getText().toString();
             pswd = upswd.getText().toString();
             LoginReqBean reqBean = new LoginReqBean();
             if (name.trim().equals("")) {
                 Toast.makeText(this, "请您输入账号！", Toast.LENGTH_SHORT).show();
                 return;
             }
             reqBean.setUserTel(name);
             if (pswd.trim().equals("")) {
                 if (upswd.getVisibility() == View.GONE){
                     // 进行验证码登录


                 }else {
                     Toast.makeText(this, "请您输入密码！", Toast.LENGTH_SHORT).show();
                 }
                 return;
             }else {
                 reqBean.setPassword(pswd);
             }
             login(reqBean);
         }
         if (v == button_change){
             if (upswd.getVisibility() == View.VISIBLE ) {
                 upswd.setVisibility(View.GONE);
                 linearLayout_change.setVisibility(View.VISIBLE);
                 button_getCode.setVisibility(View.VISIBLE);
                 login.setVisibility(View.GONE);
                 textView_change.setText("使用密码登录");
             }else {
                 upswd.setVisibility(View.VISIBLE);
                 linearLayout_change.setVisibility(View.GONE);
                 button_getCode.setVisibility(View.GONE);
                 login.setVisibility(View.VISIBLE);
                 textView_change.setText("使用验证码登录");
             }
         }
         if ( v == button_getCode){
             name = uname.getText().toString();
             GetCodeReqBean reqBean = new GetCodeReqBean();
             reqBean.setTplId(0);
             reqBean.setTelNum(name);
             reqBean.setStatus(0);
             sendLoginCode(reqBean);
         }
     }

     private void login(LoginReqBean reqBean) {
          new Thread(()->{
              String json = JSON.toJSONString(reqBean);
              String returnMsg = null;
              try {
                  HttpUtils utils = new HttpUtils();
                  returnMsg = utils.post(HttpConstant.URL + "/dream/login", json);
              }catch (Exception e){
                  Log.e("login exception",e.toString());
                  Looper.prepare();
                  Toast.makeText(this,"网络连接异常，请检查网络",Toast.LENGTH_SHORT).show();
                  Looper.loop();

              }
              if (returnMsg != null && returnMsg.contains("true")){
                  boolean CheckBoxLogin = checkboxButton.isChecked();
                  if (CheckBoxLogin) {
                      SharedPreferences.Editor editor = sp.edit();
                      editor.putString("uname", name);
                      editor.putString("upswd", pswd);
                      editor.putBoolean("checkboxBoolean", true);
                      editor.commit();
                  } else {
                      SharedPreferences.Editor editor = sp.edit();
                      editor.putString("uname", null);
                      editor.putString("upswd", null);
                      editor.putBoolean("checkboxBoolean", false);
                      editor.commit();
                      }
                  //Intent跳转
                  Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                  startActivity(intent);
                  finish();
              }else {
                  //解决在子线程中调用Toast的异常情况处理
                  Looper.prepare();
                  Toast.makeText(this,"账号或者密码错误",Toast.LENGTH_SHORT).show();
                  Looper.loop();
              }
          }).start();

     }

     private boolean sendLoginCode(GetCodeReqBean reqBean){
        new Thread(()->{
            String json = JSON.toJSONString(reqBean);
            String returnMsg = null;
            try {
                HttpUtils utils = new HttpUtils();
                returnMsg = utils.post(HttpConstant.URL + "/dream/login/getCode", json);
            }catch (Exception e){
                Log.e("login exception",e.toString());
                Looper.prepare();
                Toast.makeText(this,"网络连接异常，请检查网络",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            if (returnMsg == null || returnMsg.contains("flase")){
                Looper.prepare();
                Toast.makeText(this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            if (returnMsg != null && returnMsg.contains("true")){
                handler.post(runnableUi);
            }
        }).start();

        return false;

    }

}


