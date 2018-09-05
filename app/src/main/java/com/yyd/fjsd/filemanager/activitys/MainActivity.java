package com.yyd.fjsd.filemanager.activitys;


import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.asynctask.CopyFileTask;
import com.yyd.fjsd.filemanager.asynctask.LoadFileListTask;
import com.yyd.fjsd.filemanager.bean.MyFile;
import com.yyd.fjsd.filemanager.bean.Type;
import com.yyd.fjsd.filemanager.fragment.FileListFragment;
import com.yyd.fjsd.filemanager.fragment.MainFragment;
import com.yyd.fjsd.filemanager.utils.FileUtil;
import com.yyd.fjsd.filemanager.utils.RunStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mToggle;
    public LinearLayout mPaste_Layout;
    private Button mPaste;
    private Button mCancel;
    private ArrayList<Type> mTypes;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0x123:
                    ArrayList<MyFile> myFileList = (ArrayList<MyFile>)msg.obj;
                    //FileListFragment
                    FileListFragment fileListFragment = FileListFragment.newInstance(myFileList, 0);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.primary_content, fileListFragment);
                    transaction.commit();
                    break;
            }
        }
    };
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
        mPaste_Layout = findViewById(R.id.paste_layout);
        mPaste = findViewById(R.id.paste);
        mCancel = findViewById(R.id.cancel);

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
                        MyApplication.getInstance().currPath = FileUtil.getInterPath(); //保存当前路径
                        new LoadFileListTask(MainActivity.this, mHandler).execute(FileUtil.getInterPath());
                        /*
                        List<File> list = FileUtil.getFileList(FileUtil.getInterPath()); //获取root列表
                        ArrayList<MyFile> myFileList = FileUtil.FileToMyFile(list);
                        //FileListFragment
                        FileListFragment fileListFragment = FileListFragment.newInstance(myFileList, 0);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.primary_content, fileListFragment);
                        transaction.commit();
                        */
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

        //Paste Button
        mPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyApplication.getInstance().getSelectedList().size() > 0){
                    Iterator<String> iterator =  MyApplication.getInstance().getSelectedList().values().iterator();
                    Iterator<Integer> keyIterator =  MyApplication.getInstance().getSelectedList().keySet().iterator();
                    //当前路径与拷贝的文件的路径不能一样，一样就等于没拷贝了
                    if(!new File(MyApplication.getInstance().getSelectedList().get(keyIterator.next())).getParent().equals(MyApplication.getInstance().currPath)){
                        new CopyFileTask(MainActivity.this).execute(iterator);
                    }else{
                        Toast.makeText(MainActivity.this, "请选择其他路径，要黏贴的文件在当前目录下", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        //Cancel Button
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().runStatus = RunStatus.NORMAL_MODE;
                mPaste_Layout.setVisibility(View.INVISIBLE);
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
    public void onBackPressed() {
        if(MyApplication.getInstance().prePath.size() > 0
                && (MyApplication.getInstance().runStatus == RunStatus.NORMAL_MODE
                || MyApplication.getInstance().runStatus == RunStatus.COPY_MODE
                || MyApplication.getInstance().runStatus == RunStatus.CUT_MODE)){
            //获取最近的路径
            int size = MyApplication.getInstance().prePath.size();
            String lastPath = MyApplication.getInstance().prePath.get(size - 1);
            MyApplication.getInstance().prePath.remove(size - 1);
            MyApplication.getInstance().currPath = lastPath;    //保存当前路径

            //获取最近路径的位置
            int position = MyApplication.getInstance().prePosition.get(size - 1);
            MyApplication.getInstance().prePosition.remove(size - 1);


            List<File> list = FileUtil.getFileList(lastPath);
            ArrayList<MyFile> myFileList = FileUtil.FileToMyFile(list);
            //FileListFragment
            FileListFragment fileListFragment = FileListFragment.newInstance(myFileList, position);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.primary_content, fileListFragment);
            transaction.commit();
        }else{
            super.onBackPressed();
        }
    }

}
