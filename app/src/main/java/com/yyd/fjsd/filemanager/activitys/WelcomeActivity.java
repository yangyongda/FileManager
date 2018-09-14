package com.yyd.fjsd.filemanager.activitys;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.yyd.fjsd.filemanager.MyApplication;
import com.yyd.fjsd.filemanager.R;
import com.yyd.fjsd.filemanager.db.DBUtils;

public class WelcomeActivity extends AppIntro2 {

    private DBUtils mDBUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBUtils = new DBUtils(this);

        if(MyApplication.getInstance().getSharedPreferencesHelper().getFirstStart()) {
            SliderPage sliderPageOne = new SliderPage();
            sliderPageOne.setTitle("欢迎使用");
            sliderPageOne.setDescription("DaDa FileManager");
            sliderPageOne.setImageDrawable(R.drawable.filemanager);
            sliderPageOne.setBgColor(getResources().getColor(R.color.colorPrimaryDark));
            addSlide(AppIntroFragment.newInstance(sliderPageOne));

            SliderPage sliderPageTwo = new SliderPage();
            sliderPageTwo.setTitle("感谢遇到你");
            sliderPageTwo.setDescription("我住长江头，君住长江尾");
            sliderPageTwo.setImageDrawable(R.drawable.slide2);
            sliderPageTwo.setBgColor(getResources().getColor(R.color.colorPrimaryDark));
            addSlide(AppIntroFragment.newInstance(sliderPageTwo));

            SliderPage sliderPageThree = new SliderPage();
            sliderPageThree.setTitle("进入主页");
            sliderPageThree.setDescription("美人卷珠帘，深坐颦蛾眉");
            sliderPageThree.setImageDrawable(R.drawable.slide3);
            sliderPageThree.setBgColor(getResources().getColor(R.color.colorPrimaryDark));
            addSlide(AppIntroFragment.newInstance(sliderPageThree));

            showSkipButton(false);
        }else{
            if(mDBUtils.Query().getCount() != 0){
                Intent intent = new Intent(WelcomeActivity.this, LockActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
