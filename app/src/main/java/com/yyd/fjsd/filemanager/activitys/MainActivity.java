package com.yyd.fjsd.filemanager.activitys;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.adapters.PictureAdapter;
import com.yyd.fjsd.filemanager.asynctask.CopyFileTask;
import com.yyd.fjsd.filemanager.asynctask.LoadFileListTask;
import com.yyd.fjsd.filemanager.bean.MyFile;
import com.yyd.fjsd.filemanager.bean.Type;
import com.yyd.fjsd.filemanager.fragment.ApkFragment;
import com.yyd.fjsd.filemanager.fragment.DocumentFragment;
import com.yyd.fjsd.filemanager.fragment.FileListFragment;
import com.yyd.fjsd.filemanager.fragment.MainFragment;
import com.yyd.fjsd.filemanager.fragment.MusicFragment;
import com.yyd.fjsd.filemanager.fragment.PictureFragment;
import com.yyd.fjsd.filemanager.fragment.VideoFragment;
import com.yyd.fjsd.filemanager.fragment.ZipFragment;
import com.yyd.fjsd.filemanager.utils.FileUtil;
import com.yyd.fjsd.filemanager.utils.FragmentList;
import com.yyd.fjsd.filemanager.utils.RunStatus;
import com.yyd.fjsd.filemanager.utils.TypeConstant;

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
    // 第一次按下返回键的时间
    private long firstPressedTime;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch(msg.what){
                case 0x123:
                    ArrayList<MyFile> myFileList = (ArrayList<MyFile>)msg.obj;
                    //FileListFragment
                    FileListFragment fileListFragment = FileListFragment.newInstance(myFileList, 0);
                    transaction.replace(R.id.primary_content, fileListFragment);
                    transaction.commit();
                    break;
                case TypeConstant.PICTURE:
                    MyApplication.getInstance().fragment_page = FragmentList.PICTURE_FRAGMENT;
                    ArrayList<String> pictureFiles = (ArrayList<String>)msg.obj;
                    //PictureFragment
                    PictureFragment pictureFragment = PictureFragment.newInstance(pictureFiles);
                    transaction.replace(R.id.primary_content, pictureFragment);
                    transaction.commit();
                    break;
                case TypeConstant.MUSIC:
                    MyApplication.getInstance().fragment_page = FragmentList.MUSIC_FRAGMENT;
                    ArrayList<String> musicFiles = (ArrayList<String>)msg.obj;
                    //MusicFragment
                    MusicFragment musicFragment = MusicFragment.newInstance(musicFiles);
                    transaction.replace(R.id.primary_content, musicFragment);
                    transaction.commit();
                    break;
                case TypeConstant.VIDEO:
                    MyApplication.getInstance().fragment_page = FragmentList.VIDEO_FRAGMENT;
                    ArrayList<String> videoFiles = (ArrayList<String>)msg.obj;
                    //VideoFragment
                    VideoFragment videoFragment = VideoFragment.newInstance(videoFiles);
                    transaction.replace(R.id.primary_content, videoFragment);
                    transaction.commit();
                    break;
                case TypeConstant.DOCUMENT:
                    MyApplication.getInstance().fragment_page = FragmentList.DOCUMENT_FRAGMENT;
                    ArrayList<String> documentFiles = (ArrayList<String>)msg.obj;
                    //DocumentFragment
                    DocumentFragment documentFragment = DocumentFragment.newInstance(documentFiles);
                    transaction.replace(R.id.primary_content, documentFragment);
                    transaction.commit();
                    break;
                case TypeConstant.APK:
                    MyApplication.getInstance().fragment_page = FragmentList.APK_FRAGMENT;
                    ArrayList<String> apkFiles = (ArrayList<String>)msg.obj;
                    //ApkFragment
                    ApkFragment apkFragment = ApkFragment.newInstance(apkFiles);
                    transaction.replace(R.id.primary_content, apkFragment);
                    transaction.commit();
                    break;
                case TypeConstant.ZIP:
                    MyApplication.getInstance().fragment_page = FragmentList.ZIP_FRAGMENT;
                    ArrayList<String> zipFiles = (ArrayList<String>)msg.obj;
                    //ApkFragment
                    ZipFragment zipFragment = ZipFragment.newInstance(zipFiles);
                    transaction.replace(R.id.primary_content, zipFragment);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void initData() {
        mTypes = new ArrayList<>();

        String[] types = getResources().getStringArray(R.array.types);
        Type picture = new Type(R.drawable.picture, types[0]);
        mTypes.add(picture);
        Type music = new Type(R.drawable.music, types[1]);
        mTypes.add(music);
        Type movie = new Type(R.drawable.video, types[2]);
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
                    case R.id.home:
                        MyApplication.getInstance().prePath.clear(); //返回主页后清除
                        MyApplication.getInstance().fragment_page = FragmentList.MAIN_FRAGMENT;
                        invalidateOptionsMenu(); //刷新OptionsMenu
                        MainFragment fragment = MainFragment.newInstance(mTypes);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.primary_content, fragment);
                        transaction.commit();
                        break;
                    case R.id.nav_local:
                        MyApplication.getInstance().fragment_page = FragmentList.FILELIST_FRAGMENT;
                        invalidateOptionsMenu(); //刷新OptionsMenu
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
                        Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(setting);
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
                        Toast.makeText(MainActivity.this, R.string.choose_other_directory, Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if(MyApplication.getInstance().fragment_page == FragmentList.FILELIST_FRAGMENT){
            menu.getItem(0).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_folder:
                View view = getLayoutInflater().inflate(R.layout.rename_layout, null);
                final EditText newFolder = view.findViewById(R.id.rename);
                new AlertDialog.Builder(this)
                        .setTitle(R.string.new_folder)
                        .setView(view)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String name = newFolder.getText().toString();
                                if(name != null && !name.equals("")){
                                    String folderName = MyApplication.getInstance().currPath + File.separator + name;
                                    if(FileUtil.newFolder(folderName)){
                                        MyApplication.getInstance().runStatus = RunStatus.NORMAL_MODE;
                                        //清空选择列表
                                        MyApplication.getInstance().getSelectedList().clear();
                                        //刷新列表
                                        List<File> list = FileUtil.getFileList(MyApplication.getInstance().currPath);
                                        ArrayList<MyFile> myFileList = FileUtil.FileToMyFile(list);
                                        //FileListFragment
                                        FileListFragment fileListFragment = FileListFragment.newInstance(myFileList, 0);
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                                        transaction.replace(R.id.primary_content, fileListFragment);
                                        transaction.commit();
                                    }else {
                                        Toast.makeText(MainActivity.this, R.string.new_folder_fail, Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(MainActivity.this, R.string.input_folder_name, Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.setting:
                Intent setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(setting);
                break;

        }
        return true;
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
        }else if(MyApplication.getInstance().fragment_page == FragmentList.PICTURE_FRAGMENT ||
                MyApplication.getInstance().fragment_page == FragmentList.MUSIC_FRAGMENT ||
                MyApplication.getInstance().fragment_page == FragmentList.VIDEO_FRAGMENT ||
                MyApplication.getInstance().fragment_page == FragmentList.DOCUMENT_FRAGMENT ||
                MyApplication.getInstance().fragment_page == FragmentList.APK_FRAGMENT ||
                MyApplication.getInstance().fragment_page == FragmentList.ZIP_FRAGMENT ) {
            //返回主页
            MyApplication.getInstance().fragment_page = FragmentList.MAIN_FRAGMENT;
            invalidateOptionsMenu(); //刷新OptionsMenu
            MainFragment fragment = MainFragment.newInstance(mTypes);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.primary_content, fragment);
            transaction.commit();

        }else{
            if (System.currentTimeMillis() - firstPressedTime < 2000) {
                super.onBackPressed();
            } else {
                Toast.makeText(MainActivity.this, R.string.repeat_once_exit, Toast.LENGTH_SHORT).show();
                firstPressedTime = System.currentTimeMillis();
            }
        }
    }

    public Handler getHandler(){
        return mHandler;
    }

}
