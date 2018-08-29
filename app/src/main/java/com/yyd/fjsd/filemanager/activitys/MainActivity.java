package com.yyd.fjsd.filemanager.activitys;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;


import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.bean.MyFile;
import com.yyd.fjsd.filemanager.bean.Type;
import com.yyd.fjsd.filemanager.fragment.FileListFragment;
import com.yyd.fjsd.filemanager.fragment.MainFragment;
import com.yyd.fjsd.filemanager.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mToggle;
    private ArrayList<Type> mTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        mTypes = new ArrayList<>();

        String[] types = getResources().getStringArray(R.array.types);
        Type picture = new Type(R.drawable.picture, types[0]);
        mTypes.add(picture);
        Type music = new Type(R.drawable.music, types[1]);
        mTypes.add(music);
        Type movie = new Type(R.drawable.movie, types[2]);
        mTypes.add(movie);
        Type document = new Type(R.drawable.document, types[3]);
        mTypes.add(document);
        Type apk = new Type(R.drawable.apk, types[4]);
        mTypes.add(apk);
        Type zip = new Type(R.drawable.zip, types[5]);
        mTypes.add(zip);

    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);


        //Toolbar
        setSupportActionBar(mToolbar);

        //DrawerLayout
        //侧拉监听
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mToggle.syncState();    //同步图标和侧拉状态
        mDrawerLayout.addDrawerListener(mToggle);

        //NavigationView
        //点击监听
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_local:
                        List<File> list = FileUtil.getFileList(FileUtil.getInterPath()); //获取root列表
                        ArrayList<MyFile> myFileList = FileUtil.FileToMyFile(list);
                        //FileListFragment
                        FileListFragment fileListFragment = FileListFragment.newInstance(myFileList);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.primary_content, fileListFragment);
                        transaction.commit();
                        break;
                    case R.id.nav_setting:
                        break;
                    case R.id.nav_about:
                        break;
                }
                mDrawerLayout.closeDrawers(); //关闭侧拉
                return true;
            }
        });

        //设置初始的Fragment
        MainFragment fragment = MainFragment.newInstance(mTypes);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.primary_content, fragment);
        transaction.commit();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK
                && MyApplication.getInstance().prePath.size() > 0){
            //获取最近的路径
            int size = MyApplication.getInstance().prePath.size();
            String lastPath = MyApplication.getInstance().prePath.get(size - 1);
            MyApplication.getInstance().prePath.remove(size - 1);

            List<File> list = FileUtil.getFileList(lastPath);
            ArrayList<MyFile> myFileList = FileUtil.FileToMyFile(list);
            //FileListFragment
            FileListFragment fileListFragment = FileListFragment.newInstance(myFileList);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.primary_content, fileListFragment);
            transaction.commit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
