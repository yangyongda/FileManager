package com.yyd.fjsd.filemanager.activitys;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;


import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;

import skin.support.SkinCompatManager;

public class SettingActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private CardView mTheme;
    private CardView mLock;
    private CardView mStorage;
    private CardView mAbout;

    private int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView(){
        mToolbar = findViewById(R.id.setting_toolbar);
        mTheme = findViewById(R.id.theme);
        mLock = findViewById(R.id.lock);
        mStorage = findViewById(R.id.storage);
        mAbout = findViewById(R.id.about);

        //Toolbar
        mToolbar.setTitle(R.string.setting);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });

        //Theme
        mTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取上次的主题
                int themeNo = MyApplication.getInstance().getSharedPreferencesHelper().getThemeNo();

                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("选择主题")
                        .setSingleChoiceItems(R.array.theme, themeNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                theme = i;
                            }
                        })
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MyApplication.getInstance().getSharedPreferencesHelper().setThemeNo(theme);
                                switch (theme){
                                    case 0:
                                        SkinCompatManager.getInstance().restoreDefaultTheme();
                                        break;
                                    case 1:
                                        SkinCompatManager.getInstance().loadSkin("light", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
                                        break;
                                    case 2:
                                        SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
                                        break;
                                }

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
