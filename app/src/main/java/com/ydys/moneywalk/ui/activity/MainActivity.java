package com.ydys.moneywalk.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.adapter.MyFragmentAdapter;
import com.ydys.moneywalk.ui.custom.HongBaoDialog;
import com.ydys.moneywalk.ui.custom.PrivacyDialog;
import com.ydys.moneywalk.ui.custom.ReceiveDoubleGoldDialog;
import com.ydys.moneywalk.ui.custom.ReceiveGoldDialog;
import com.ydys.moneywalk.ui.fragment.HomeFragment;
import com.ydys.moneywalk.ui.fragment.MakeMoneyFragment;
import com.ydys.moneywalk.ui.fragment.MyFragment;
import com.ydys.moneywalk.ui.fragment.TestFragment;

import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private long clickTime = 0;

    private final int[] TITLES = new int[]{R.string.tab_home_txt, R.string.tab_money_txt, R.string.tab_my_txt};

    private final int[] IMAGES = new int[]{R.drawable.tab_home, R.drawable.tab_money, R.drawable.tab_my};

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private MyFragmentAdapter adapter;

    PrivacyDialog privacyDialog;

    HongBaoDialog hongBaoDialog;

    ReceiveDoubleGoldDialog receiveDoubleGoldDialog;

    ReceiveGoldDialog receiveGoldDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new MakeMoneyFragment());
        mFragmentList.add(new MyFragment());

//        for (int i = 1; i < TITLES.length; i++) {
//            mFragmentList.add(TestFragment.newInstance(getResources().getString(TITLES[i])));
//        }

        setTabs(tabLayout, this.getLayoutInflater(), TITLES, IMAGES);
        adapter = new MyFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //绑定tab点击事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                Logger.i("tab pos --->" + pos);
                viewPager.setCurrentItem(pos);
                if (pos == 0) {
//                    if (mFragmentList.get(pos) instanceof HomeFragment) {
//                        HomeFragment.
//                    }
                }
                if (pos == 1) {

                }
                if (pos == 3) {
//                    if (mFragmentList.get(pos) instanceof MyFragment) {
//                        ((MyFragment) mFragmentList.get(pos)).setUserInfo();
//                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        privacyDialog = new PrivacyDialog(this, R.style.common_dialog);
        //privacyDialog.show();

        hongBaoDialog = new HongBaoDialog(this, R.style.common_dialog);
        //hongBaoDialog.show();

        receiveDoubleGoldDialog = new ReceiveDoubleGoldDialog(this, R.style.common_dialog);
//        receiveDoubleGoldDialog.show();

        receiveGoldDialog = new ReceiveGoldDialog(this, R.style.common_dialog);
        //receiveGoldDialog.show();
    }

    public void setTabs(TabLayout tabLayout, LayoutInflater layoutInflater, int[] titles, int[] images) {
        for (int i = 0; i < titles.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = layoutInflater.inflate(R.layout.tab_custom, null);
            tab.setCustomView(view);
            TextView tabText = view.findViewById(R.id.tv_tab);
            tabText.setText(titles[i]);
            ImageView tabImage = view.findViewById(R.id.iv_tab);
            tabImage.setImageResource(images[i]);
            tabLayout.addTab(tab);
        }
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            clickTime = System.currentTimeMillis();
            Toasty.normal(getApplicationContext(), "再按一次退出").show();
        } else {
            System.exit(0);
        }
    }
}
