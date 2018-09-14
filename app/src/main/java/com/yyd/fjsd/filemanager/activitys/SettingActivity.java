package com.yyd.fjsd.filemanager.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;


import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.db.DBUtils;

import java.util.List;

import skin.support.SkinCompatManager;

public class SettingActivity extends AppCompatActivity {

    private final int ADD_MODE = 1; //添加图形锁
    private final int DELETE_MODE = 2; //关闭图形锁

    private Toolbar mToolbar;
    private SwitchCompat mLock;
    private CardView mTheme;
    private CardView mModifyLock;
    private CardView mStorage;
    private CardView mAbout;
    private PatternLockView mPatternLockView;

    //private View lock_layout;
    private AlertDialog mAlertDialog;

    private DBUtils mDBUtils;
    private int theme;
    private int count = 0;
    private boolean isCancel = false;
    private int patternLock_Mode = ADD_MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mDBUtils = new DBUtils(this);
        initView();
    }

    private void initView(){
        mToolbar = findViewById(R.id.setting_toolbar);
        mTheme = findViewById(R.id.theme);
        mLock = findViewById(R.id.lock);
        mModifyLock = findViewById(R.id.modify_lock);
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

        //Lock
        if(mDBUtils.Query().getCount() != 0){
            mLock.setChecked(true);
            mModifyLock.setVisibility(View.VISIBLE);
        }

        mLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                count = 0;
                //PatternLockView 图形锁
                View lock_layout = getLayoutInflater().inflate(R.layout.dialog_lock_setting, null);
                mPatternLockView = lock_layout.findViewById(R.id.patter_lock_view);
                mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                    String patternTemp; //保存第一次的图形

                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onProgress(List<PatternLockView.Dot> progressPattern) {

                    }

                    @Override
                    public void onComplete(List<PatternLockView.Dot> pattern) {
                        //Log.d(getClass().getName(), "Pattern complete: " + PatternLockUtils.patternToString(mPatternLockView, pattern));

                        if(PatternLockUtils.patternToString(mPatternLockView, pattern).length() > 1) {
                            if(patternLock_Mode == ADD_MODE) {
                                if (count == 1) {
                                    if (PatternLockUtils.patternToString(mPatternLockView, pattern).equals(patternTemp)) {
                                        mDBUtils.insert(1, PatternLockUtils.patternToString(mPatternLockView, pattern));
                                        mModifyLock.setVisibility(View.VISIBLE);    //显示修改图形锁选项
                                        Toast.makeText(SettingActivity.this, "图形密码添加完成", Toast.LENGTH_SHORT).show();
                                        mAlertDialog.dismiss();
                                        mAlertDialog = null;
                                    } else {
                                        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                                        Toast.makeText(SettingActivity.this, "图形密码不一致，请重新绘制！", Toast.LENGTH_SHORT).show();
                                        count = 0;
                                    }
                                } else {
                                    count++;
                                    patternTemp = PatternLockUtils.patternToString(mPatternLockView, pattern);
                                    Toast.makeText(SettingActivity.this, "请再绘制一次", Toast.LENGTH_SHORT).show();
                                }
                            }else{

                                    Cursor mCursor = mDBUtils.Query();
                                    mCursor.moveToFirst();
                                    String DataBasepattern = mCursor.getString(mCursor.getColumnIndex("dot"));
                                    //Log.v("yang", " " + DataBasepattern);
                                    if (DataBasepattern != null && !DataBasepattern.equals("") &&
                                            PatternLockUtils.patternToString(mPatternLockView, pattern).equals(DataBasepattern)) {
                                        mDBUtils.delete(1);
                                        mModifyLock.setVisibility(View.GONE);
                                        mAlertDialog.dismiss();
                                        mAlertDialog = null;
                                    } else {
                                        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                                        Toast.makeText(SettingActivity.this, "图形密码不正确，请重新绘制！", Toast.LENGTH_SHORT).show();
                                    }

                            }
                            //mPatternLockView.clearPattern();
                        }else{
                            Toast.makeText(SettingActivity.this, "长度必须大于2",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCleared() {

                    }
                });
                if(isCancel == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this)
                            .setTitle("绘制图形锁")
                            .setView(lock_layout)
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    isCancel = true;
                                    if(mLock.isChecked()) {
                                        mLock.setChecked(false);
                                    }else{
                                        mLock.setChecked(true);
                                    }
                                }
                            });

                    mAlertDialog = builder.create();
                    mAlertDialog.setCanceledOnTouchOutside(false);
                    mAlertDialog.show();
                }else { //按取消按钮不显示Dialog
                    isCancel = false;
                }

                if(b){  //开启
                    patternLock_Mode = ADD_MODE;
                }else{  //关闭
                    //Log.v("yang", "hide");
                    patternLock_Mode = DELETE_MODE;
                }

            }
        });

        //ModifyLock
        mModifyLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 0;
                //PatternLockView 图形锁
                View lock_layout = getLayoutInflater().inflate(R.layout.dialog_lock_setting, null);
                mPatternLockView = lock_layout.findViewById(R.id.patter_lock_view);
                mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                    String patternTemp; //保存第一次的图形
                    boolean isOldPattern = true;

                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onProgress(List<PatternLockView.Dot> progressPattern) {

                    }

                    @Override
                    public void onComplete(List<PatternLockView.Dot> pattern) {
                        if(isOldPattern){
                            Cursor mCursor = mDBUtils.Query();
                            mCursor.moveToFirst();
                            String DataBasepattern = mCursor.getString(mCursor.getColumnIndex("dot"));
                            if (DataBasepattern != null && !DataBasepattern.equals("") &&
                                    PatternLockUtils.patternToString(mPatternLockView, pattern).equals(DataBasepattern)){
                                isOldPattern = false;
                                mPatternLockView.clearPattern();
                                mAlertDialog.setTitle("输入新图形");
                            }else{
                                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                                Toast.makeText(SettingActivity.this, "图形密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            if (count == 1) {
                                if (PatternLockUtils.patternToString(mPatternLockView, pattern).equals(patternTemp)) {
                                    mDBUtils.update(1, PatternLockUtils.patternToString(mPatternLockView, pattern));
                                    mModifyLock.setVisibility(View.VISIBLE);    //显示修改图形锁选项
                                    Toast.makeText(SettingActivity.this, "图形密码修改完成", Toast.LENGTH_SHORT).show();
                                    mAlertDialog.dismiss();
                                    mAlertDialog = null;
                                } else {
                                    mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                                    Toast.makeText(SettingActivity.this, "图形密码不一致，请重新绘制！", Toast.LENGTH_SHORT).show();
                                    count = 0;
                                }
                            } else {
                                count++;
                                patternTemp = PatternLockUtils.patternToString(mPatternLockView, pattern);
                                Toast.makeText(SettingActivity.this, "请再绘制一次", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCleared() {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("输入旧图形")
                        .setView(lock_layout)
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                mAlertDialog = builder.create();
                mAlertDialog.setCanceledOnTouchOutside(false);
                mAlertDialog.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
