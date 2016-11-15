package com.example.cheng.myyunxin.MyServe;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.cheng.myyunxin.MainActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;

/**
 * Created by a452542253 on 2016/11/14.
 */

public class StateService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
    public class MyBinder extends Binder {

        public  boolean stateischange(){

            NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                    new Observer<StatusCode>() {
                        public void onEvent(StatusCode status) {
                            Log.i("tag", "User status changed to: " + status);
                            if (status.wontAutoLogin()) {
                                // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                                Toast.makeText(getApplicationContext(),"自动登录失败！！！",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        }
                    }, true);

            return false;
        }
    }
}
