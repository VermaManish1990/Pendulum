package com.pend.activity.contest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.mirror.SearchInNewsFeedActivity;
import com.pend.adapters.FragmentViewPagerAdapter;
import com.pend.fragments.FollowingContestFragment;
import com.pend.fragments.IntroducedContestFragment;
import com.pend.fragments.TrendingContestFragment;
import com.pend.util.LoggerUtil;

public class ContestActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ContestActivity.class.getSimpleName();
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {
        mRootView = findViewById(R.id.root_view);

        findViewById(R.id.custom_search_view).setOnClickListener(this);

        TabLayout tabLayoutContest = findViewById(R.id.tab_layout_contest);
        ViewPager viewPagerContest = findViewById(R.id.view_pager_contest);

        setupViewPager(viewPagerContest);
        tabLayoutContest.setupWithViewPager(viewPagerContest, true);

    }

    /**
     * Method is used to setting up View Pager
     */
    private void setupViewPager(ViewPager viewPager) {
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TrendingContestFragment(), getString(R.string.trending));
        adapter.addFragment(new FollowingContestFragment(), getString(R.string.following));
        adapter.addFragment(new IntroducedContestFragment(), getString(R.string.introduced));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void setInitialData() {


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
            case R.id.custom_search_view:
                Intent intent = new Intent(ContestActivity.this, SearchInNewsFeedActivity.class);
                startActivity(intent);
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
