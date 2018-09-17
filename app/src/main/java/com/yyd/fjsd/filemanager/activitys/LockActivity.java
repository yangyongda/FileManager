package com.yyd.fjsd.filemanager.activitys;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.db.DBUtils;

import java.util.List;

public class LockActivity extends AppCompatActivity {

    private PatternLockView mPatternLockView;
    private DBUtils mDBUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        mDBUtils = new DBUtils(this);
        initView();
    }

    private void initView(){
        mPatternLockView = findViewById(R.id.lock_view);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                Cursor mCursor = mDBUtils.Query();
                mCursor.moveToFirst();
                String DataBasepattern = mCursor.getString(mCursor.getColumnIndex("dot"));
                //Log.v("yang", " " + DataBasepattern);
                if (DataBasepattern != null && !DataBasepattern.equals("") &&
                        PatternLockUtils.patternToString(mPatternLockView, pattern).equals(DataBasepattern)) {
                    Intent main = new Intent(LockActivity.this, MainActivity.class);
                    startActivity(main);
                    finish();
                }else{
                    mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }
            }

            @Override
            public void onCleared() {

            }
        });
    }
}
