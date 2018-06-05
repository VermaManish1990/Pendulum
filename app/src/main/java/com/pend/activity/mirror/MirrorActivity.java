package com.pend.activity.mirror;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.MirrorViewPagerAdapter;
import com.pend.fragments.FollowingMirrorFragment;
import com.pend.fragments.IntroducedMirrorFragment;
import com.pend.fragments.TrendingMirrorFragment;

public class MirrorActivity extends BaseActivity {

    private View mRootView;

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
        ViewPager viewPagerMirror = findViewById(R.id.view_pager_mirror);
        setupViewPager(viewPagerMirror);
        tabLayoutMirror.setupWithViewPager(viewPagerMirror, true);
    }

    /**
     * Method is used to setting up View Pager
     */
    private void setupViewPager(ViewPager viewPager) {
        MirrorViewPagerAdapter adapter = new MirrorViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TrendingMirrorFragment(), getString(R.string.trending));
        adapter.addFragment(new FollowingMirrorFragment(), getString(R.string.following));
        adapter.addFragment(new IntroducedMirrorFragment(), getString(R.string.introduced));
        viewPager.setAdapter(adapter);
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
