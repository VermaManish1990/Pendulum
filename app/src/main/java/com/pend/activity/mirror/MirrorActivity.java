package com.pend.activity.mirror;

import android.content.Intent;
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
import com.pend.util.LoggerUtil;

public class MirrorActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MirrorActivity.class.getSimpleName();
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

        View customSearchView = findViewById(R.id.custom_search_view);
        customSearchView.findViewById(R.id.et_search).setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_search:
                Intent intent = new Intent(MirrorActivity.this, SearchInNewsFeedActivity.class);
                startActivity(intent);
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
