package com.pend.activity.contest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.home.HomeActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.activity.mirror.SearchInNewsFeedActivity;
import com.pend.adapters.FragmentViewPagerAdapter;
import com.pend.fragments.FollowingContestFragment;
import com.pend.fragments.IntroducedContestFragment;
import com.pend.fragments.TrendingContestFragment;
import com.pend.util.LoggerUtil;

public class ContestActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ContestActivity.class.getSimpleName();
    private View mRootView;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;

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

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);

        ((TextView) quarterView.findViewById(R.id.tv_contest)).setText(String.valueOf(getResources().getString(R.string.home)));
        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        quarterView.findViewById(R.id.iv_profile).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_area).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_search_view:
                Intent intent = new Intent(ContestActivity.this, SearchInNewsFeedActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_profile:
                hideReveal();
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                break;

            case R.id.fl_mirror:
                hideReveal();
                Intent intentMirror = new Intent(this, MirrorActivity.class);
                startActivity(intentMirror);
                break;

            case R.id.fl_contest:
                hideReveal();
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                finish();
                break;

            case R.id.fl_area:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.fl_menu_view:
                mFlMenuView.setVisibility(View.GONE);
                showReveal();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void showReveal() {
        int cx = 0;
        int cy = mRlQuarterView.getHeight();
        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mRlQuarterView, cx, cy, 0, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRlQuarterView.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void hideReveal() {

        int cx = 0;
        int cy = mRlQuarterView.getHeight();
        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mRlQuarterView, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRlQuarterView.setVisibility(View.GONE);
                mFlMenuView.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (mRlQuarterView.getVisibility() == View.VISIBLE) {
            hideReveal();
        } else {
            Intent intentHome = new Intent(this, HomeActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentHome);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Rect outRect = new Rect();
        mFlQuarterBlackView.getGlobalVisibleRect(outRect);

        if (mRlQuarterView.getVisibility() == View.VISIBLE && !outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
            hideReveal();
        }
        return super.dispatchTouchEvent(event);
    }
}
