package com.pend.activity.mirror;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.home.HomeActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.adapters.FragmentViewPagerAdapter;
import com.pend.arena.view.ArenaActivity;
import com.pend.fragments.CreateMirrorDialogFragment;
import com.pend.fragments.FollowingMirrorFragment;
import com.pend.fragments.IntroducedMirrorFragment;
import com.pend.fragments.TrendingMirrorFragment;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IMirrorFragmentCallBack;
import com.pend.util.LoggerUtil;
import com.pend.util.SharedPrefUtils;
import com.squareup.picasso.Picasso;

public class MirrorActivity extends BaseActivity implements View.OnClickListener, IMirrorFragmentCallBack, TextWatcher {

    private static final String TAG = MirrorActivity.class.getSimpleName();
    private static final int TRENDING_MIRROR = 0;
    private static final int FOLLOWING_MIRROR = 1;
    private static final int INTRODUCED_MIRROR = 2;
    private View mRootView;
    private ViewPager mViewPagerMirror;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;
    private boolean mIsUpdateRequired;
    private EditText mEtSearch;
    private ImageView mIvSearch;
    private TrendingMirrorFragment mTrendingMirrorFragment;
    private FollowingMirrorFragment mFollowingMirrorFragment;
    private IntroducedMirrorFragment mIntroducedMirrorFragment;
    private boolean mIsSearchData;
    private ImageView mIvProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);

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

        ((ImageView) quarterView.findViewById(R.id.iv_mirror)).setImageDrawable(getResources().getDrawable(R.drawable.home));
        ((TextView) quarterView.findViewById(R.id.tv_mirror)).setText(String.valueOf(getResources().getString(R.string.home)));
        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        mIvProfile = quarterView.findViewById(R.id.iv_profile);
        quarterView.findViewById(R.id.fl_area).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
        mIvProfile.setOnClickListener(this);

        View view = findViewById(R.id.custom_search_view);
        mEtSearch = view.findViewById(R.id.et_search);
        mIvSearch = view.findViewById(R.id.iv_search);
        mIvSearch.setOnClickListener(this);
        mEtSearch.addTextChangedListener(this);

        TabLayout tabLayoutMirror = findViewById(R.id.tab_layout_mirror);
        mViewPagerMirror = findViewById(R.id.view_pager_mirror);

        setupViewPager(mViewPagerMirror);
        tabLayoutMirror.setupWithViewPager(mViewPagerMirror, true);
    }

    @Override
    protected void setInitialData() {

        String imageUrl = SharedPrefUtils.getProfileImageUrl(this);

        if (imageUrl != null && !imageUrl.equals("")) {
            Picasso.with(this)
                    .load(imageUrl)
                    .into(mIvProfile);
        }

        mIsUpdateRequired = false;
        mIsSearchData = true;

        mEtSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    onSearchClick();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Method is used to setting up View Pager.
     */
    private void setupViewPager(ViewPager viewPager) {

        mTrendingMirrorFragment = new TrendingMirrorFragment();
        mFollowingMirrorFragment = new FollowingMirrorFragment();
        mIntroducedMirrorFragment = new IntroducedMirrorFragment();

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mTrendingMirrorFragment, getString(R.string.trending));
        adapter.addFragment(mFollowingMirrorFragment, getString(R.string.following));
        adapter.addFragment(mIntroducedMirrorFragment, getString(R.string.introduced));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                switch (position) {
                    case TRENDING_MIRROR:

                            mIsSearchData = true;
                            mEtSearch.setText("");
                            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                            mTrendingMirrorFragment.cancelSearchMirrorData();
                        break;

                    case FOLLOWING_MIRROR:

                            mIsSearchData = true;
                            mEtSearch.setText("");
                            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                            mFollowingMirrorFragment.cancelSearchMirrorData();
                        break;

                    case INTRODUCED_MIRROR:

                            mIsSearchData = true;
                            mEtSearch.setText("");
                            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                            mIntroducedMirrorFragment.cancelSearchMirrorData();
                        break;
                }
            }
        });

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_search:

                onSearchClick();
                break;

            case R.id.iv_profile:
                hideReveal();
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                break;

            case R.id.fl_mirror:
                hideReveal();
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                finish();
                break;

            case R.id.fl_contest:
                hideReveal();
                Intent intentContest = new Intent(this, ContestActivity.class);
                startActivity(intentContest);
//                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();

                break;

            case R.id.fl_area:
                hideReveal();
                Intent intentArena = new Intent(this, ArenaActivity.class);
                startActivity(intentArena);
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

    @Override
    public void onCreateMirrorClick() {
        DialogFragment createMirrorDialogFragment = new CreateMirrorDialogFragment();
        createMirrorDialogFragment.show(getSupportFragmentManager(), "CreateMirrorDialogFragment");
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsUpdateRequired = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsUpdateRequired) {

            mIsUpdateRequired = false;
            setupViewPager(mViewPagerMirror);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mIsSearchData = true;
        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * Method is used to perform action on search click.
     */
    public void onSearchClick() {
        String searchText = mEtSearch.getText().toString().trim();
        int item = mViewPagerMirror.getCurrentItem();

        switch (item) {
            case TRENDING_MIRROR:
                if (mIsSearchData) {
                    mIsSearchData = false;
                    mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
                    mTrendingMirrorFragment.searchMirrorData(searchText);
                } else {
                    mIsSearchData = true;
                    mEtSearch.setText("");
                    mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                    mTrendingMirrorFragment.cancelSearchMirrorData();
                }
                break;

            case FOLLOWING_MIRROR:
                if (mIsSearchData) {
                    mIsSearchData = false;
                    mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
                    mFollowingMirrorFragment.searchMirrorData(searchText);
                } else {
                    mIsSearchData = true;
                    mEtSearch.setText("");
                    mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                    mFollowingMirrorFragment.cancelSearchMirrorData();
                }
                break;

            case INTRODUCED_MIRROR:
                if (mIsSearchData) {
                    mIsSearchData = false;
                    mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
                    mIntroducedMirrorFragment.searchMirrorData(searchText);
                } else {
                    mIsSearchData = true;
                    mEtSearch.setText("");
                    mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                    mIntroducedMirrorFragment.cancelSearchMirrorData();
                }
                break;
        }
    }
}
