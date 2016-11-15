package com.example.cheng.myyunxin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

import static android.R.id.message;

public class LiaoTianActivity extends AppCompatActivity implements View.OnClickListener {

    private long last = 0;
    private Button mButTuichu;
    private Button mButton;
    private EditText mEtContent;
    private EditText mEtMubiaoID;
    private TextView tv_shou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liao_tian);
        initView();
        statechange();
        TongBustate();

        shouMsg();

    }

    public void statechange() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode>() {
                    public void onEvent(StatusCode status) {
                        Log.i("tmd", "用户当前状态  User status changed to: " + status);
                        if (status.toString().equals("NET_BROKEN")) {
                            startActivity(new Intent(LiaoTianActivity.this, MainActivity.class));
                            finish();
                        }
                        if (status.wontAutoLogin()) {
                            // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                            Toast.makeText(LiaoTianActivity.this, "登录失败请重新登录", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LiaoTianActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                }, true);
    }

    public void TongBustate() {
        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(new Observer<LoginSyncStatus>() {
            @Override
            public void onEvent(LoginSyncStatus status) {
                if (status == LoginSyncStatus.BEGIN_SYNC) {
//                    LogUtil.i(TAG, "login sync data begin");
                    Log.i("tmd", "onEvent: login sync data begin");
                } else if (status == LoginSyncStatus.SYNC_COMPLETED) {
//                    LogUtil.i(TAG, "login sync data completed");
                    Log.i("tmd", "onEvent: login sync data completed");
                }
            }
        }, true);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long during = currentTime - last;
        if (during < 2000) {
            finish();
        } else {
            Toast.makeText(LiaoTianActivity.this, "再按一次退出！", Toast.LENGTH_SHORT).show();
        }
        last = currentTime;
    }

    private void initView() {
        mButTuichu = (Button) findViewById(R.id.but_tuichu);

        mButTuichu.setOnClickListener(this);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mEtContent.setOnClickListener(this);
        mEtMubiaoID = (EditText) findViewById(R.id.et_mubiaoID);
        mEtMubiaoID.setOnClickListener(this);
        tv_shou = (TextView) findViewById(R.id.tv_shou);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_tuichu:
                NIMClient.getService(AuthService.class).logout();
                Toast.makeText(LiaoTianActivity.this, "当前状态为离线状态！！！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button:
                // 创建文本消息
                IMMessage message = MessageBuilder.createTextMessage(
                        mEtMubiaoID.getText().toString(), // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                        SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                        mEtContent.getText().toString() // 文本内容
                );
// 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
                NIMClient.getService(MsgService.class).sendMessage(message, true);
                msgState();

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mymenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(LiaoTianActivity.this, "123132312", Toast.LENGTH_SHORT).show();


        return super.onOptionsItemSelected(item);
    }

    public void shouMsg() {
        Observer<List<IMMessage>> incomingMessageObserver =
                new Observer<List<IMMessage>>() {
                    @Override
                    public void onEvent(List<IMMessage> messages) {
                        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < messages.size(); i++) {
                            sb.append(messages.get(i).getContent());
                        }
                        tv_shou.setText(sb.toString());
                    }
                };
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
    }
    //监听消息发送状态
    public void msgState() {
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(new Observer<IMMessage>() {
            @Override
            public void onEvent(IMMessage imMessage) {

                Toast.makeText(LiaoTianActivity.this,imMessage.getStatus().toString(),Toast.LENGTH_SHORT).show();
            }
        },true);
    }

}
