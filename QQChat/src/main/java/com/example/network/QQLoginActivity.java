package com.example.network;

import com.example.network.thread.ClientThread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class QQLoginActivity extends AppCompatActivity implements OnClickListener {
    private final static String TAG = "QQLoginActivity";
    private static Context mContext;
    //账号编辑框
    private EditText et_name;
    //密码编辑框
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_qq_login);
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_forget).setOnClickListener(this);
        findViewById(R.id.tv_law).setOnClickListener(this);
        mContext = getApplicationContext();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            String uid = et_name.getText().toString().trim();
            String password = et_password.getText().toString().trim();
            if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(password)) { // 未输入昵称
                Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_LONG).show();
            } else { // 已输入昵称
                // 设置当前用户昵称的全局变量
                MainApplication.getInstance().setNickName(et_name.getText().toString());
                // 向后端服务器发送登录消息
                MainApplication.getInstance().sendAction(ClientThread.LOGIN, "", "", password);
            }
        } else if (v.getId() == R.id.tv_register) {
            startActivity(new Intent(QQLoginActivity.this, QQRegisterActivity.class));
        } else if (v.getId() == R.id.tv_forget) {
            Toast.makeText(getApplicationContext(), "尚未完成", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.tv_law) {
            showLawDialog();
        }
    }

    private void showLawDialog(){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(QQLoginActivity.this);
        normalDialog.setIcon(R.drawable.login);
        normalDialog.setTitle("服务条款");
        normalDialog.setMessage("Designed by 吴亦飞\n2019年6月11日\n计算机171班\n201710311151");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    // 定义一个得到登录结果的广播接收器
    public class LoginReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Log.d(TAG, "onReceive");
                // 从意图中解包得到登录的结果
                String content = intent.getStringExtra(ClientThread.CONTENT);
                if (mContext != null && content != null && content.length() > 0) {
                    // 处理信息并且以弹出对话框的形式展示处理结果
                    showResultDialog(content);
                }
            }
        }
    }

    private void showResultDialog(String content){
        int pos = content.indexOf(ClientThread.SPLIT_LINE);
        String head = content.substring(0, pos); // 消息头部
        String body = content.substring(pos + 1); // 消息主体
        String[] splitArray = head.split(ClientThread.SPLIT_ITEM);
        if (splitArray[0].equals(ClientThread.LOGIN)) { // 是登录结果
            if (splitArray[1].equals(ClientThread.SUCCESS)) {
                Log.d(TAG, "注册成功");
                // 跳转到聊天主页面
                startActivity(new Intent(this, QQChatActivity.class));
                // 关闭当前页面
                finish();
            } else if (splitArray[1].equals(ClientThread.FAILED)) {
                Log.d(TAG, "注册失败");
                Toast.makeText(getApplicationContext(), "登录失败：用户名或密码错误！", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 适配Android9.0开始
    @Override
    public void onStart() {
        super.onStart();
        // 从Android9.0开始，系统不再支持静态广播，应用广播只能通过动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 创建一个好友列表的广播接收器
            loginReceiver = new LoginReceiver();
            // 注册广播接收器，注册之后才能正常接收广播
            registerReceiver(loginReceiver, new IntentFilter(ClientThread.ACTION_LOGIN));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 注销广播接收器，注销之后就不再接收广播
            unregisterReceiver(loginReceiver);
        }
    }

    // 声明一个好友列表的广播接收器
    private LoginReceiver loginReceiver;
    // 适配Android9.0结束

}
