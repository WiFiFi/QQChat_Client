package com.example.network;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.network.thread.ClientThread;

public class QQFindActivity extends AppCompatActivity implements OnClickListener{
    private final static String TAG = "QQFindActivity";
    RelativeLayout photo, saoyisao;
    Button btn_logout;

    private final String IMAGE_TYPE = "image/*";
    public static final int IMAGE_REQUEST_CODE = 0x102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_find);
        // 从布局文件中获取名叫tl_head的工具栏
        Toolbar tl_head = findViewById(R.id.tl_head);
        // 设置工具栏的标题文本
        tl_head.setTitle(getResources().getString(R.string.menu_third));
        // 设置工具栏的标题文本的颜色
        tl_head.setTitleTextColor(getResources().getColor(R.color.white));
        // 使用tl_head替换系统自带的ActionBar
        setSupportActionBar(tl_head);
        // 给tl_head设置导航图标的点击监听器
        // setNavigationOnClickListener必须放到setSupportActionBar之后，不然不起作用
        tl_head.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        photo = findViewById(R.id.layout_xiangce);
        saoyisao = findViewById(R.id.layout_saoyisao);
        btn_logout = findViewById(R.id.btn_logout);
        photo.setOnClickListener(this);
        saoyisao.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_xiangce:
                openAlbum();
                break;
            case R.id.layout_saoyisao:
                startActivity(new Intent(QQFindActivity.this, QQCameraActivity.class));
                break;
            case R.id.btn_logout:
                MainApplication.getInstance().sendAction(ClientThread.LOGOUT, "", "");
                startActivity(new Intent(QQFindActivity.this, QQLoginActivity.class));
                finish();
        }
    }

    public void openAlbum(){
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(IMAGE_TYPE);
        if (Build.VERSION.SDK_INT <19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

}
