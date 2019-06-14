package com.example.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.network.thread.ClientThread;

@SuppressLint("StaticFieldLeak")
public class QQRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "QQRegisterActivity";
    private static Context mContext;
    //账号编辑框
    private EditText mUser;
    //密码编辑框
    private EditText mPassword;
    private EditText mPasswordCheck;
    //三选一RadioButton
    private RadioGroup mRadioGroup;
    //完成注册按钮
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_register);
        mUser = findViewById(R.id.register_name);
        mPassword = findViewById(R.id.register_password);
        mPasswordCheck = findViewById(R.id.register_password_check);
        mRadioGroup = findViewById(R.id.register_gender);
        mButton = findViewById(R.id.register_btn);
        mButton.setOnClickListener(this);
        mContext = getApplicationContext();
    }

    @Override
    public void onClick(View v) {
        String name = mUser.getText().toString();
        String password = mPassword.getText().toString();
        String passwordCheck = mPasswordCheck.getText().toString();
        if (!password.equals(passwordCheck)) {
            Toast.makeText(getApplicationContext(), "两次输入的密码不一致", Toast.LENGTH_LONG).show();
        } else if (name.length() > 30) {
            Toast.makeText(getApplicationContext(), "用户名长度不可超过20个字符", Toast.LENGTH_LONG).show();
        } else if (password.length() > 20) {
            Toast.makeText(getApplicationContext(), "密码长度不可超过20个字符", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), name+"\n"+password+"\n"+((RadioButton)findViewById(mRadioGroup.getCheckedRadioButtonId())).getText().toString(), Toast.LENGTH_LONG).show();
            MainApplication.getInstance().setNickName(name);
            MainApplication.getInstance().sendAction(ClientThread.REGISTER, "", "", password);
        }
    }

    // 定义一个得到注册结果的广播接收器
    public class RegisterReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Log.d(TAG, "onReceive");
                // 从意图中解包得到注册的结果
                String content = intent.getStringExtra(ClientThread.CONTENT);
                if (mContext != null && content != null && content.length() > 0) {
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
        if (splitArray[0].equals(ClientThread.REGISTER)) { // 是注册结果
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(QQRegisterActivity.this);
            normalDialog.setIcon(R.drawable.login);
            if (splitArray[1].equals(ClientThread.SUCCESS)) {
                Log.d(TAG, "注册成功");
                normalDialog.setTitle("耶o(*￣▽￣*)ブ");
                normalDialog.setMessage("注册成功！");
            } else if (splitArray[1].equals(ClientThread.FAILED)) {
                Log.d(TAG, "注册失败");
                normalDialog.setTitle("呃(⊙﹏⊙)");
                normalDialog.setMessage("注册失败，用户名已被注册");
            }
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            // 显示
            normalDialog.show();
        }
    }

    // 适配Android9.0开始
    @Override
    public void onStart() {
        super.onStart();
        // 从Android9.0开始，系统不再支持静态广播，应用广播只能通过动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 创建一个好友列表的广播接收器
            registerReceiver = new RegisterReceiver();
            // 注册广播接收器，注册之后才能正常接收广播
            registerReceiver(registerReceiver, new IntentFilter(ClientThread.ACTION_REGISTER));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 注销广播接收器，注销之后就不再接收广播
            unregisterReceiver(registerReceiver);
        }
    }

    // 声明一个好友列表的广播接收器
    private RegisterReceiver registerReceiver;
    // 适配Android9.0结束

}
