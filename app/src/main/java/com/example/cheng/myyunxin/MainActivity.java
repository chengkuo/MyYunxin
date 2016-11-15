package com.example.cheng.myyunxin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button mButZhuce;
    private Button mButDenglu;
    SharedPreferences.Editor ed;
    private SharedPreferences pre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        pre = getSharedPreferences("usermsg",MODE_PRIVATE);
        ed = pre.edit();
//        if (pre.getString("account",null)!=null && pre.getString("password",null)!=null){
//            mEtAccount.setText(pre.getString("account",null));
//            mEtPassword.setText(pre.getString("password",null));
//            doLogin();
//        }



    }

    public void doLogin() {
        LoginInfo info = new LoginInfo(mEtAccount.getText().toString().toLowerCase(), mEtPassword.getText().toString()); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo loginInfo) {
                        ed.putString("account",mEtAccount.getText().toString().toLowerCase());
                        ed.putString("password",mEtPassword.getText().toString());
                        ed.commit();
                        startActivity(new Intent(MainActivity.this,LiaoTianActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailed(int i) {
                        Log.i("tmd", "onFailed: 登录失败"+i);
                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

    private void initView() {
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mButZhuce = (Button) findViewById(R.id.but_zhuce);
        mButDenglu = (Button) findViewById(R.id.but_denglu);

        mButZhuce.setOnClickListener(this);
        mButDenglu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_zhuce:

                break;
            case R.id.but_denglu:
                doLogin();
                break;
        }
    }


}
