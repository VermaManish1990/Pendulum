package com.pend.activity.login;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.home.HomeActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.adapters.ProfileViewPagerAdapter;
import com.pend.adapters.TimeSheetAdapter;
import com.pend.fragments.CommentsDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetPostDetailsResponseModel;
import com.pend.models.GetPostsResponseModel;
import com.pend.models.UserProfileResponseModel;
import com.pend.models.UserTimeSheetResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.PaginationScrollListener;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, ProfileViewPagerAdapter.IProfileViewPagerAdapterCallBack,
        TimeSheetAdapter.ITimeSheetAdapterCallBack,CommentsDialogFragment.ICommentsDialogCallBack {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ViewPager mViewpagerProfile;
    private TabLayout mTabLayout;
    private View mRootView;

    private UserProfileResponseModel mUserProfileResponseModel;
    private ArrayList<UserTimeSheetResponseModel.UserTimeSheetDetails> mTimeSheetDetailsList;
    private TextView mTvName;
    private TextView mTvAge;
    private TextView mTvCity;
    private TextView mTvToken;
    private int mPageNumber;
    private RecyclerView mRecyclerViewTimeSheet;
    private boolean mIsHasNextPage;
    private boolean mIsLoading;
    private boolean mIsUpdateRequired;
    private ImageView mIvLargeProfile;
    private View mRlLargeView;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;
    private ImageView mIvProfile;
    private ImageView mIvMessage;
    private int mUserId;
    private boolean mIsOtherProfile;
    private View mRlSettingAndEditView;
    private int mMirrorId;
    private int mPostId;
    private GetPostDetailsResponseModel mPostDetailsResponseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mIsOtherProfile = false;
        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.USER_ID_KEY)) {
                mUserId = localBundle.getInt(Constants.USER_ID_KEY, 0);
            }

            if (localBundle.containsKey(Constants.IS_OTHER_PROFILE)) {
                mIsOtherProfile = localBundle.getBoolean(Constants.IS_OTHER_PROFILE, false);
            }
        }

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_GET_USER_PROFILE_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mRlLargeView = findViewById(R.id.rl_large_view);
        mIvLargeProfile = findViewById(R.id.iv_large_profile);
        mViewpagerProfile = findViewById(R.id.viewpager_profile);
        mTabLayout = findViewById(R.id.tab_layout);
        mTvName = findViewById(R.id.tv_name);
        mTvAge = findViewById(R.id.tv_age);
        mTvCity = findViewById(R.id.tv_city);
        mTvToken = findViewById(R.id.tv_token);
        mIvMessage = findViewById(R.id.iv_message);
        mRlSettingAndEditView = findViewById(R.id.rl_setting_and_edit);
        mRecyclerViewTimeSheet = findViewById(R.id.recycler_view_time_sheet);

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);
        mIvProfile = quarterView.findViewById(R.id.iv_profile);

        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_area).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
        mIvProfile.setOnClickListener(this);

        mIvMessage.setOnClickListener(this);
        findViewById(R.id.iv_setting).setOnClickListener(this);
        findViewById(R.id.iv_edit).setOnClickListener(this);
        findViewById(R.id.iv_close).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {
        mIsUpdateRequired = false;
        mIsHasNextPage = false;
        mIsLoading = false;
        mPageNumber = 1;
        mTimeSheetDetailsList = new ArrayList<>();

        mIvProfile.setImageDrawable(getResources().getDrawable(R.drawable.home_large));

        if (mIsOtherProfile) {
            mRlSettingAndEditView.setVisibility(View.GONE);
            mIvMessage.setVisibility(View.VISIBLE);
        } else {
            mRlSettingAndEditView.setVisibility(View.VISIBLE);
            mIvMessage.setVisibility(View.GONE);
        }

        mTabLayout.setupWithViewPager(mViewpagerProfile, true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewTimeSheet.setLayoutManager(linearLayoutManager);

        mRecyclerViewTimeSheet.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mPageNumber += 1; //Increment page index to load the next one
                getData(IApiEvent.REQUEST_GET_USER_TIME_SHEET_CODE);
            }

            @Override
            public boolean isLastPage() {
                return mIsHasNextPage;
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }
        });
        mRecyclerViewTimeSheet.setAdapter(new TimeSheetAdapter(this, mTimeSheetDetailsList));
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_USER_PROFILE_CODE:
                if (status) {
                    mUserProfileResponseModel = (UserProfileResponseModel) serviceResponse;
                    if (mUserProfileResponseModel != null && mUserProfileResponseModel.status) {
                        LoggerUtil.d(TAG, mUserProfileResponseModel.statusCode);

                        if (mUserProfileResponseModel.Data != null && mUserProfileResponseModel.Data.imageData != null && mUserProfileResponseModel.Data.imageData.size() > 0) {

                            mViewpagerProfile.setAdapter(new ProfileViewPagerAdapter(this, mUserProfileResponseModel.Data.imageData));
                        }

                        if (mUserProfileResponseModel.Data != null && mUserProfileResponseModel.Data.userData != null) {

                            mTvName.setText(mUserProfileResponseModel.Data.userData.userFullName != null ? mUserProfileResponseModel.Data.userData.userFullName : "");
                            mTvAge.setText(String.valueOf(mUserProfileResponseModel.Data.userData.userAge + " year " + mUserProfileResponseModel.Data.userData.userGender));
                            mTvCity.setText(mUserProfileResponseModel.Data.userData.cityName != null ? mUserProfileResponseModel.Data.userData.cityName : "");
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    OtherUtil.showErrorMessage(this, serviceResponse);
                }

                if (!mIsUpdateRequired) {
                    getData(IApiEvent.REQUEST_GET_USER_TIME_SHEET_CODE);
                } else {
                    mIsUpdateRequired = false;
                }
                break;

            case IApiEvent.REQUEST_GET_USER_TIME_SHEET_CODE:
                if (status) {
                    UserTimeSheetResponseModel userTimeSheetResponseModel = (UserTimeSheetResponseModel) serviceResponse;
                    if (userTimeSheetResponseModel != null && userTimeSheetResponseModel.status) {
                        LoggerUtil.d(TAG, userTimeSheetResponseModel.statusCode);

                        if (userTimeSheetResponseModel.Data != null && userTimeSheetResponseModel.Data.timeSheetData != null) {

                            mIsHasNextPage = !userTimeSheetResponseModel.Data.hasNextPage;

                            TimeSheetAdapter timeSheetAdapter = (TimeSheetAdapter) mRecyclerViewTimeSheet.getAdapter();
                            mTimeSheetDetailsList.addAll(userTimeSheetResponseModel.Data.timeSheetData);
                            timeSheetAdapter.setTimeSheetDetailsList(mTimeSheetDetailsList);
                            timeSheetAdapter.notifyDataSetChanged();

                            mTvToken.setText(String.valueOf(getString(R.string.token) + " " + userTimeSheetResponseModel.Data.timeSheetData.size()));
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    OtherUtil.showErrorMessage(this, serviceResponse);
                }

                mIsLoading = false;
                break;

            case IApiEvent.REQUEST_GET_POST_DETAIL_CODE:
                if (status) {
                    mPostDetailsResponseModel = (GetPostDetailsResponseModel) serviceResponse;
                    if (mPostDetailsResponseModel != null && mPostDetailsResponseModel.status) {
                        LoggerUtil.d(TAG, mPostDetailsResponseModel.statusCode);

                        if (mPostDetailsResponseModel.Data != null && mPostDetailsResponseModel.Data.postData != null) {

                            GetPostsResponseModel.GetPostsDetails postsDetails = new GetPostsResponseModel.GetPostsDetails();

                            postsDetails.postInfo = mPostDetailsResponseModel.Data.postData.postInfo;
                            postsDetails.isUnLike = mPostDetailsResponseModel.Data.postData.isUnLike;
                            postsDetails.isLike = mPostDetailsResponseModel.Data.postData.isLike;
                            postsDetails.imageURL = mPostDetailsResponseModel.Data.postData.imageURL;
                            postsDetails.imageName = mPostDetailsResponseModel.Data.postData.imageName;
                            postsDetails.commentUserImageURL = mPostDetailsResponseModel.Data.postData.commentUserImageURL;
                            postsDetails.commentCount = mPostDetailsResponseModel.Data.postData.commentCount;
                            postsDetails.commentText = mPostDetailsResponseModel.Data.postData.commentText;
                            postsDetails.commentUserFullName = mPostDetailsResponseModel.Data.postData.commentUserFullName;
                            postsDetails.commentUserImageName = mPostDetailsResponseModel.Data.postData.commentUserImageName;
                            postsDetails.postID = mPostDetailsResponseModel.Data.postData.postID;
                            postsDetails.unlikeCount = mPostDetailsResponseModel.Data.postData.unlikeCount;
                            postsDetails.likeCount = mPostDetailsResponseModel.Data.postData.likeCount;
                            postsDetails.createdDatetime = mPostDetailsResponseModel.Data.postData.createdDatetime;
                            postsDetails.userFullName = mPostDetailsResponseModel.Data.postData.userFullName;
                            postsDetails.userID = mPostDetailsResponseModel.Data.postData.userID;

                            CommentsDialogFragment commentsDialogFragment = CommentsDialogFragment.newInstance(postsDetails);
                            commentsDialogFragment.show(getSupportFragmentManager(), "CommentsDialogFragment");
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    OtherUtil.showErrorMessage(this, serviceResponse);
                }

                mIsLoading = false;
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
        removeProgressDialog();
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }
        showProgressDialog();

        if (!mIsOtherProfile) {
            try {
                mUserId = Integer.parseInt(SharedPrefUtils.getUserId(this));
            } catch (Exception e) {
                e.printStackTrace();
                mUserId = -1;
            }
        }

        switch (actionID) {
            case IApiEvent.REQUEST_GET_USER_PROFILE_CODE:

                String userProfileUrl = IWebServices.REQUEST_GET_USER_PROFILE_URL + Constants.PARAM_USER_ID + "=" + mUserId;
                RequestManager.addRequest(new GsonObjectRequest<UserProfileResponseModel>(userProfileUrl, NetworkUtil.getHeaders(this), null,
                        UserProfileResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(UserProfileResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_GET_USER_TIME_SHEET_CODE:

                String userTimeSheetUrl = IWebServices.REQUEST_GET_USER_TIME_SHEET_URL + Constants.PARAM_USER_ID + "=" + mUserId + "&" +
                        Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
                RequestManager.addRequest(new GsonObjectRequest<UserTimeSheetResponseModel>(userTimeSheetUrl, NetworkUtil.getHeaders(this), null,
                        UserTimeSheetResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(UserTimeSheetResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_GET_POST_DETAIL_CODE:

                String postDetailsUrl = IWebServices.REQUEST_GET_POST_DETAIL_URL + Constants.PARAM_USER_ID + "=" + mUserId + "&" +
                        Constants.PARAM_MIRROR_ID + "=" + mMirrorId + "&" +
                        Constants.PARAM_POST_ID + "=" + mPostId;
                RequestManager.addRequest(new GsonObjectRequest<GetPostDetailsResponseModel>(postDetailsUrl, NetworkUtil.getHeaders(this), null,
                        GetPostDetailsResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetPostDetailsResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    @Override
    public void onAuthError() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:

                if (mUserProfileResponseModel != null && mUserProfileResponseModel.Data != null && mUserProfileResponseModel.Data.userData != null) {

                    Intent intentSetting = new Intent(ProfileActivity.this, SettingActivity.class);
                    intentSetting.putExtra(Constants.USER_DETAILS_KEY, mUserProfileResponseModel.Data.userData);
                    startActivity(intentSetting);
                } else {
                    Snackbar.make(mRootView, getString(R.string.user_details_not_available), Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_edit:

                if (mUserProfileResponseModel != null && mUserProfileResponseModel.Data != null) {
                    Intent intentEdit = new Intent(ProfileActivity.this, EditMyProfileActivity.class);
                    intentEdit.putExtra(Constants.USER_DATA_MODEL_KEY, mUserProfileResponseModel.Data);
                    startActivity(intentEdit);
                } else {
                    Snackbar.make(mRootView, getString(R.string.user_profile_details_not_available), Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_message:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.iv_close:
                mRlLargeView.setVisibility(View.GONE);
                break;

            case R.id.iv_profile:
                hideReveal();
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                finish();
                break;

            case R.id.fl_mirror:
                hideReveal();
                Intent intentMirror = new Intent(this, MirrorActivity.class);
                startActivity(intentMirror);
                break;

            case R.id.fl_contest:
                hideReveal();
                Intent intentContest = new Intent(this, ContestActivity.class);
                startActivity(intentContest);
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

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsUpdateRequired) {
            getData(IApiEvent.REQUEST_GET_USER_PROFILE_CODE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsUpdateRequired = true;
    }

    @Override
    public void onImageClick(int position) {

        mRlLargeView.setVisibility(View.VISIBLE);

        Picasso.with(this)
                .load(mUserProfileResponseModel.Data.imageData.get(position).imageURL)
                .into(mIvLargeProfile);
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
        } else if (mRlLargeView.getVisibility() == View.VISIBLE) {
            mRlLargeView.setVisibility(View.GONE);
        } else {
            if (mIsOtherProfile) {
                super.onBackPressed();
            } else {
                Intent intentHome = new Intent(this, HomeActivity.class);
                intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                finish();
            }
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
    public void onLogDetailsClick(int position) {

        mMirrorId = mTimeSheetDetailsList.get(position).mirrorid;
        mPostId = mTimeSheetDetailsList.get(position).postID;

        if (mPostId != 0)
            getData(IApiEvent.REQUEST_GET_POST_DETAIL_CODE);
    }

    @Override
    public void onPostUpdate(GetPostsResponseModel.GetPostsDetails postDetails) {

    }
}
