package com.pend.activity.mirror;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pend.BaseActivity;
import com.pend.R;

public class MirrorActivity extends BaseActivity {

    private View mRootView;
    private ViewPager mViewPagerMirror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);

        initUI();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        TabLayout tabLayoutMirror = findViewById(R.id.tab_layout_mirror);
        mViewPagerMirror = findViewById(R.id.view_pager_mirror);

        tabLayoutMirror.setupWithViewPager(mViewPagerMirror, true);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }

    @Override
    public void onAuthError() {

    }
}
