package com.pend.activity.contest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.home.HomeActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.activity.mirror.SearchInNewsFeedActivity;
import com.pend.adapters.FragmentViewPagerAdapter;
import com.pend.arena.view.ArenaActivity;
import com.pend.fragments.FollowingContestFragment;
import com.pend.fragments.IntroducedContestFragment;
import com.pend.fragments.TrendingContestFragment;
import com.pend.interfaces.IContestVotingCallBack;
import com.pend.util.LoggerUtil;
import com.pend.util.SharedPrefUtils;
import com.pendulum.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ContestActivity extends BaseActivity implements View.OnClickListener, TextWatcher , IContestVotingCallBack {

    private static final String TAG = ContestActivity.class.getSimpleName();
    private static final int TRENDING_CONTEST = 0;
    private static final int FOLLOWING_CONTEST = 1;
    private static final int INTRODUCED_CONTEST = 2;

    private View mRootView;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;
    private ImageView mIvProfile;
    private EditText mEtSearch;
    private ImageView mIvSearch;
    private boolean mIsSearchData;
    private String mSearchTextFollowing;
    private boolean mCleanSearch;
    private String mSearchTextIntroduced;
    private String mSearchTextTrending;
    private FragmentViewPagerAdapter mAdapter;
    private ViewPager mViewPagerContest;

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

        ((ImageView) quarterView.findViewById(R.id.iv_contest)).setImageDrawable(getResources().getDrawable(R.drawable.home));
        ((TextView) quarterView.findViewById(R.id.tv_contest)).setText(String.valueOf(getResources().getString(R.string.home)));
        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        mIvProfile = quarterView.findViewById(R.id.iv_profile);
        quarterView.findViewById(R.id.fl_arena).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
        mIvProfile.setOnClickListener(this);

        View view = findViewById(R.id.custom_search_view);
        mEtSearch = view.findViewById(R.id.et_search);
        mIvSearch = view.findViewById(R.id.iv_search);
        mIvSearch.setOnClickListener(this);
        mEtSearch.addTextChangedListener(this);

        TabLayout tabLayoutContest = findViewById(R.id.tab_layout_contest);
        mViewPagerContest = findViewById(R.id.view_pager_contest);
        mViewPagerContest.setOffscreenPageLimit(3);

        setupViewPager(mViewPagerContest);
        tabLayoutContest.setupWithViewPager(mViewPagerContest, true);

    }

    /**
     * Method is used to setting up View Pager
     */
    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new TrendingContestFragment(), getString(R.string.trending));
        mAdapter.addFragment(new FollowingContestFragment(), getString(R.string.following));
        mAdapter.addFragment(new IntroducedContestFragment(), getString(R.string.introduced));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                // Check if this is the page you want.
                switch (position) {
                    case TRENDING_CONTEST:

                        mIsSearchData = true;
                        mEtSearch.setText(mSearchTextTrending);
                        if (StringUtils.isNullOrEmpty(mSearchTextTrending)) {
                            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                        } else {
                            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
                        }
                        break;

                    case FOLLOWING_CONTEST:

                        mIsSearchData = true;
                        mEtSearch.setText(mSearchTextFollowing);
                        if (StringUtils.isNullOrEmpty(mSearchTextFollowing)) {
                            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                        } else {
                            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
                        }
                        break;

                    case INTRODUCED_CONTEST:

                        mIsSearchData = true;
                        mEtSearch.setText(mSearchTextIntroduced);
                        if (StringUtils.isNullOrEmpty(mSearchTextIntroduced)) {
                            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                        } else {
                            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
                        }
                        break;
                }
            }
        });
        viewPager.setAdapter(mAdapter);
    }

    @Override
    protected void setInitialData() {

        String imageUrl = SharedPrefUtils.getProfileImageUrl(this);

        if (imageUrl != null && !imageUrl.equals("")) {
            Picasso.with(this)
                    .load(imageUrl)
                    .into(mIvProfile);
        }

        mIsSearchData = true;

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    onSearchClick();

                    return true;
                }
                return false;
            }
        });

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
                Drawable drawableImage = mIvSearch.getDrawable();
                Bitmap bitmapImage = null;
                if (drawableImage != null) {
                    bitmapImage = ((BitmapDrawable) drawableImage).getBitmap();
                }

                Drawable drawableCross = getDrawable(R.drawable.cross_white);
                Bitmap bitmapCross = null;
                if (drawableCross != null) {
                    bitmapCross = ((BitmapDrawable) drawableCross).getBitmap();
                }

                if (Objects.equals(bitmapImage, bitmapCross)) {
                    mCleanSearch = true;
                    mIsSearchData = false;
                }
                onSearchClick();
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

            case R.id.fl_arena:
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
    protected void onPause() {
        super.onPause();

//        mIsUpdateRequired = true;
        mIsSearchData = true;
        mSearchTextTrending = "";
        mSearchTextFollowing = "";
        mSearchTextIntroduced = "";
        mEtSearch.setText("");
        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (mIsUpdateRequired) {
//            mIsUpdateRequired = false;
//        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mIsSearchData = true;
        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));

        if (count == 0) {
            switch (mViewPagerContest.getCurrentItem()) {
                case TRENDING_CONTEST:
                    if (!s.toString().equalsIgnoreCase(mSearchTextTrending)) {
                        onSearchClick();
                    }
                    break;

                case FOLLOWING_CONTEST:
                    if (!s.toString().equalsIgnoreCase(mSearchTextFollowing)) {
                        onSearchClick();
                    }
                    break;
                case INTRODUCED_CONTEST:
                    if (!s.toString().equalsIgnoreCase(mSearchTextIntroduced)) {
                        onSearchClick();
                    }
                    break;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * Method is used to perform action on search click.
     */
    public void onSearchClick() {
        String searchText = mEtSearch.getText().toString().trim();
        int item = mViewPagerContest.getCurrentItem();


        switch (item) {
            case TRENDING_CONTEST:

                TrendingContestFragment trendingContestFragment = (TrendingContestFragment) mAdapter.getItem(item);
                if (trendingContestFragment != null) {
                    mSearchTextTrending = searchText;
                    if (mIsSearchData) {
                        mIsSearchData = false;
                        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
                        trendingContestFragment.searchMirrorData(searchText);
                    } else if (mCleanSearch) {
                        mCleanSearch = false;
                        mIsSearchData = true;
                        mEtSearch.setText("");
                        mSearchTextTrending = "";
                        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                        trendingContestFragment.cancelSearchMirrorData();
                    } else {
                        mIsSearchData = true;
                        mEtSearch.setText(mSearchTextTrending);
                        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                    }
                }
                break;

            case FOLLOWING_CONTEST:

                FollowingContestFragment followingContestFragment = (FollowingContestFragment) mAdapter.getItem(item);
                if (followingContestFragment != null) {
                    mSearchTextFollowing = searchText;
                    if (mIsSearchData) {
                        mIsSearchData = false;
                        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
                        followingContestFragment.searchMirrorData(searchText);
                    } else if (mCleanSearch) {
                        mCleanSearch = false;
                        mIsSearchData = true;
                        mEtSearch.setText("");
                        mSearchTextFollowing = "";
                        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                        followingContestFragment.cancelSearchMirrorData();
                    } else {
                        mIsSearchData = true;
                        mEtSearch.setText(mSearchTextFollowing);
                        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                    }
                }
                break;

            case INTRODUCED_CONTEST:

                IntroducedContestFragment introducedContestFragment = (IntroducedContestFragment) mAdapter.getItem(item);
                if (introducedContestFragment != null) {
                    mSearchTextIntroduced = searchText;

                    if (mIsSearchData) {
                        mIsSearchData = false;
                        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
                        introducedContestFragment.searchMirrorData(searchText);
                    } else if (mCleanSearch) {
                        mCleanSearch = false;
                        mIsSearchData = true;
                        mEtSearch.setText("");
                        mSearchTextIntroduced = "";
                        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                        introducedContestFragment.cancelSearchMirrorData();
                    } else {
                        mIsSearchData = true;
                        mEtSearch.setText(mSearchTextIntroduced);
                        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
                    }
                }
                break;
        }
    }

    @Override
    public void onVotingOrUnVotingClick() {
        mAdapter.notifyDataSetChanged();
    }
}
