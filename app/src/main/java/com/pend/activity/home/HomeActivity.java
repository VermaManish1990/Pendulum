package com.pend.activity.home;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.activity.mirror.MirrorDetailsActivity;
import com.pend.activity.mirror.SearchMirrorListingActivity;
import com.pend.adapters.HomePostsAdapter;
import com.pend.adapters.SearchInNewsFeedAdapter;
import com.pend.arena.LocationResponse;
import com.pend.arena.LocationVM;
import com.pend.arena.api.ApiClient;
import com.pend.arena.api.URL;
import com.pend.arena.view.ArenaActivity;
import com.pend.fragments.CommentsDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.AddAndUpdateCommentResponseModel;
import com.pend.models.GetPostsResponseModel;
import com.pend.models.PostLikeResponseModel;
import com.pend.models.SearchInNewsFeedResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.PaginationScrollListener;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;


public class HomeActivity extends BaseActivity implements View.OnClickListener, HomePostsAdapter.IHomePostsAdapterCallBack,
        CommentsDialogFragment.ICommentsDialogCallBack, SearchInNewsFeedAdapter.IMirrorSearchAdapterCallBack, TextWatcher,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private RecyclerView mRecyclerViewPost;
    private ArrayList<GetPostsResponseModel.GetPostsDetails> mPostsDetailsList;
    private ArrayList<SearchInNewsFeedResponseModel.MirrorDetails> mMirrorList;
    private int mPageNumber = 1;
    private int mMirrorId;
    private TextView mTvDataNotAvailable;
    private View mRootView;
    private View mRlQuarterView;
    private View mFlMenuView;
    private View mFlQuarterBlackView;
    private boolean mIsHasNextPage;
    private boolean mIsLoading;
    private int mPostId;
    private boolean mIsLike;
    private boolean mIsUnLike;
    private String mCommentText;
    private boolean mIsUpdateRequired;
    private EditText mEtSearch;
    private ImageView mIvProfile;
    private RecyclerView mRecyclerViewMirror;
    private boolean mIsSearchData;
    private ImageView mIvSearch;
    private String mSearchText;
    private long UPDATE_INTERVAL = 1000 *60 * 15; /*  min */
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleApiClient gac;
    private LocationRequest locationRequest;
    private static boolean isFirstLaunch = true;
    private int mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());

        mUserId = -1;
        try {
            mUserId = Integer.parseInt(SharedPrefUtils.getUserId(HomeActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_GET_POSTS_CODE);
        isGooglePlayServicesAvailable();

        if(!isLocationEnabled()&&isFirstLaunch)
            showAlert();

        locationRequest =  LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        gac = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mRecyclerViewPost = findViewById(R.id.recycler_view_post);
        mRecyclerViewMirror = findViewById(R.id.recycler_view_mirror);
        mTvDataNotAvailable = findViewById(R.id.tv_data_not_available);

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);

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
        mIsUpdateRequired = false;
        mIsHasNextPage = false;
        mIsLoading = false;
        mPageNumber = 1;
        mPostsDetailsList = new ArrayList<>();
        mMirrorList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewPost.setLayoutManager(linearLayoutManager);
        mRecyclerViewPost.setVisibility(View.VISIBLE);
        mRecyclerViewPost.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mPageNumber += 1; //Increment page index to load the next one
                getData(IApiEvent.REQUEST_GET_POSTS_CODE);
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
        mRecyclerViewPost.setAdapter(new HomePostsAdapter(this, mPostsDetailsList));

        LinearLayoutManager linearLayoutManager1Mirror = new LinearLayoutManager(this);
        mRecyclerViewMirror.setLayoutManager(linearLayoutManager1Mirror);
        mRecyclerViewMirror.addOnScrollListener(new PaginationScrollListener(linearLayoutManager1Mirror) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mPageNumber += 1; //Increment page index to load the next one
                getData(IApiEvent.REQUEST_SEARCH_NEWS_FEED_MIRROR_CODE);
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
        mRecyclerViewMirror.setAdapter(new SearchInNewsFeedAdapter(this, mMirrorList));

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    onSearchClick();

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_POSTS_CODE:
                if (status) {
                    GetPostsResponseModel postsResponseModel = (GetPostsResponseModel) serviceResponse;
                    if (postsResponseModel != null && postsResponseModel.status) {
                        LoggerUtil.d(TAG, postsResponseModel.statusCode);

                        if (postsResponseModel.Data != null && postsResponseModel.Data.postList != null) {

                            mTvDataNotAvailable.setVisibility(View.GONE);
                            mRecyclerViewMirror.setVisibility(View.GONE);
                            mRecyclerViewPost.setVisibility(View.VISIBLE);

                            mIsHasNextPage = !postsResponseModel.Data.hasNextPage;

                            HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
                            mPostsDetailsList.addAll(postsResponseModel.Data.postList);
                            homePostsAdapter.setPostsDetailsList(mPostsDetailsList);
                            homePostsAdapter.notifyDataSetChanged();
                        } else {
                            mTvDataNotAvailable.setVisibility(View.VISIBLE);
                            mRecyclerViewPost.setVisibility(View.GONE);
                            mRecyclerViewMirror.setVisibility(View.GONE);
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        mTvDataNotAvailable.setVisibility(View.VISIBLE);
                        mRecyclerViewPost.setVisibility(View.GONE);
                        mRecyclerViewMirror.setVisibility(View.GONE);
                    }
                } else {
                    mPostsDetailsList.clear();
//                    HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
//                    homePostsAdapter.setPostsDetailsList(mPostsDetailsList);
//                    homePostsAdapter.notifyDataSetChanged();
//                    OtherUtil.showErrorMessage(this, serviceResponse);

                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }

                mIsLoading = false;
                break;

            case IApiEvent.REQUEST_SEARCH_NEWS_FEED_MIRROR_CODE:
                if (status) {
                    SearchInNewsFeedResponseModel searchInNewsFeedResponseModel = (SearchInNewsFeedResponseModel) serviceResponse;
                    if (searchInNewsFeedResponseModel != null && searchInNewsFeedResponseModel.status) {
                        LoggerUtil.d(TAG, searchInNewsFeedResponseModel.statusCode);

                        if (searchInNewsFeedResponseModel.Data != null && searchInNewsFeedResponseModel.Data.mirrorList != null) {

                            mTvDataNotAvailable.setVisibility(View.GONE);
                            mRecyclerViewPost.setVisibility(View.GONE);
                            mRecyclerViewMirror.setVisibility(View.VISIBLE);

                            mIsHasNextPage = !searchInNewsFeedResponseModel.Data.hasNextPage;

                            SearchInNewsFeedAdapter searchInNewsFeedAdapter = (SearchInNewsFeedAdapter) mRecyclerViewMirror.getAdapter();
                            mMirrorList.addAll(searchInNewsFeedResponseModel.Data.mirrorList);
                            searchInNewsFeedAdapter.setSearchDataList(mMirrorList);
                            searchInNewsFeedAdapter.notifyDataSetChanged();
                        } else {
                            mTvDataNotAvailable.setVisibility(View.VISIBLE);
                            mRecyclerViewPost.setVisibility(View.GONE);
                            mRecyclerViewMirror.setVisibility(View.GONE);
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        mTvDataNotAvailable.setVisibility(View.VISIBLE);
                        mRecyclerViewPost.setVisibility(View.GONE);
                        mRecyclerViewMirror.setVisibility(View.GONE);
                    }
                } else {

                    // When record not found in Pendulum database then it search from Wikipedia.
                    if (mSearchText != null && !mSearchText.equals("") && mPageNumber == 1) {
                        Intent intent = new Intent(HomeActivity.this, SearchMirrorListingActivity.class);
                        intent.putExtra(Constants.SEARCH_TEXT_KEY, mSearchText);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }

                mIsLoading = false;
                break;

            case IApiEvent.REQUEST_POST_LIKE_CODE:
                if (status) {
                    PostLikeResponseModel postLikeResponseModel = (PostLikeResponseModel) serviceResponse;
                    if (postLikeResponseModel != null && postLikeResponseModel.status) {
                        LoggerUtil.d(TAG, postLikeResponseModel.statusCode);

                        if (postLikeResponseModel.Data != null && postLikeResponseModel.Data.likeData != null) {

                            int position = 0;
                            for (GetPostsResponseModel.GetPostsDetails postsDetails : mPostsDetailsList) {
                                if (postsDetails.postID == postLikeResponseModel.Data.likeData.postID) {

                                    postsDetails.isLike = postLikeResponseModel.Data.likeData.isLike;
                                    postsDetails.isUnLike = postLikeResponseModel.Data.likeData.isUnLike;
                                    postsDetails.likeCount = postLikeResponseModel.Data.likeData.likeCount;
                                    postsDetails.unlikeCount = postLikeResponseModel.Data.likeData.unlikeCount;

                                    position = mPostsDetailsList.indexOf(postsDetails);
                                    break;
                                }
                            }
                            HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
//                            homePostsAdapter.setPostsDetailsList(mPostsDetailsList);
                            homePostsAdapter.notifyItemChanged(position, mPostsDetailsList.get(position));
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_REMOVE_POST_CODE:
                if (status) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

                        int index = 0;
                        for (GetPostsResponseModel.GetPostsDetails postsDetails : mPostsDetailsList) {
                            if (mPostId == postsDetails.postID) {
                                index = mPostsDetailsList.indexOf(postsDetails);
                            }
                        }
                        mPostsDetailsList.remove(index);
                        HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
                        homePostsAdapter.setPostsDetailsList(mPostsDetailsList);
                        homePostsAdapter.notifyDataSetChanged();

                        Snackbar.make(mRootView, getString(R.string.post_remove_successfully), Snackbar.LENGTH_LONG).show();

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_ADD_COMMENT_CODE:
                if (status) {
                    AddAndUpdateCommentResponseModel addAndUpdateCommentResponseModel = (AddAndUpdateCommentResponseModel) serviceResponse;
                    if (addAndUpdateCommentResponseModel != null && addAndUpdateCommentResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdateCommentResponseModel.statusCode);

                        if (addAndUpdateCommentResponseModel.Data != null && addAndUpdateCommentResponseModel.Data.commentData != null) {

                            int position = 0;
                            for (GetPostsResponseModel.GetPostsDetails tempPostDetails : mPostsDetailsList) {
                                if (tempPostDetails.postID == addAndUpdateCommentResponseModel.Data.commentData.postID) {

                                    tempPostDetails.commentCount += 1;
                                    tempPostDetails.commentUserImageName = addAndUpdateCommentResponseModel.Data.commentData.imageName;
                                    tempPostDetails.commentUserImageURL = addAndUpdateCommentResponseModel.Data.commentData.commentUserImageURL;
                                    tempPostDetails.commentUserFullName = addAndUpdateCommentResponseModel.Data.commentData.userFullName;
                                    tempPostDetails.commentText = addAndUpdateCommentResponseModel.Data.commentData.commentText;
                                    position = mPostsDetailsList.indexOf(tempPostDetails);
                                    break;
                                }
                            }

                            HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
                            homePostsAdapter.notifyItemChanged(position);
                            Snackbar.make(mRootView, getString(R.string.add_comment_successfully), Snackbar.LENGTH_LONG).show();
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
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

        JsonObject jsonObject;
        String request;

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(HomeActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_GET_POSTS_CODE:

                //TODO Change mirrorId.
                mMirrorId = 147;
                String getPostsUrl = IWebServices.REQUEST_GET_POSTS_URL + Constants.PARAM_USER_ID + "=" + userId
                        + "&" + Constants.PARAM_MIRROR_ID + "=" + mMirrorId
                        + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
                RequestManager.addRequest(new GsonObjectRequest<GetPostsResponseModel>(getPostsUrl, NetworkUtil.getHeaders(this),
                        null, GetPostsResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetPostsResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_SEARCH_NEWS_FEED_MIRROR_CODE:

                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    String searchInNewsFeedUrl = IWebServices.REQUEST_SEARCH_NEWS_FEED_MIRROR_URL + Constants.PARAM_USER_ID + "=" + userId
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber)
                            + "&" + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;
                    RequestManager.addRequest(new GsonObjectRequest<SearchInNewsFeedResponseModel>(searchInNewsFeedUrl, NetworkUtil.getHeaders(this),
                            null, SearchInNewsFeedResponseModel.class, new VolleyErrorListener(this, actionID)) {

                        @Override
                        protected void deliverResponse(SearchInNewsFeedResponseModel response) {
                            updateUi(true, actionID, response);
                        }
                    });
                } else {
                    removeProgressDialog();
                }

                break;

            case IApiEvent.REQUEST_POST_LIKE_CODE:

                jsonObject = RequestPostDataUtil.postLikeApiRegParam(userId, mPostId, mIsLike, mIsUnLike);
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<PostLikeResponseModel>(IWebServices.REQUEST_POST_LIKE_URL, NetworkUtil.getHeaders(this),
                        request, PostLikeResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(PostLikeResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_REMOVE_POST_CODE:

                jsonObject = RequestPostDataUtil.removePostApiRegParam(userId, mPostId, mMirrorId);
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<BaseResponseModel>(IWebServices.REQUEST_REMOVE_POST_URL, NetworkUtil.getHeaders(this),
                        request, BaseResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(BaseResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_ADD_COMMENT_CODE:

                jsonObject = RequestPostDataUtil.addCommentApiRegParam(userId, mPostId, mCommentText);
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<AddAndUpdateCommentResponseModel>(IWebServices.REQUEST_ADD_COMMENT_URL, NetworkUtil.getHeaders(this),
                        request, AddAndUpdateCommentResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(AddAndUpdateCommentResponseModel response) {
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

            case R.id.iv_search:

                onSearchClick();
                break;

            case R.id.iv_profile:

                hideReveal();

                if (mUserId == -1) {
                    OtherUtil.showAlertDialog(getString(R.string.guest_user_message), this, (dialog, which) -> dialog.dismiss());
                } else {
                    Intent intentProfile = new Intent(this, ProfileActivity.class);
                    startActivity(intentProfile);
                }
                break;

            case R.id.fl_mirror:
                hideReveal();
                Intent intentMirror = new Intent(HomeActivity.this, MirrorActivity.class);
                startActivity(intentMirror);
                break;

            case R.id.fl_contest:
                hideReveal();
                Intent intentContest = new Intent(HomeActivity.this, ContestActivity.class);
                startActivity(intentContest);
//                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
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

    public void searchMirrorData() {
        mPageNumber = 1;
        mIsLoading = false;
        mIsUpdateRequired = false;
        mIsHasNextPage = false;

        if (mMirrorList != null) {
            mMirrorList.clear();
        } else {
            mMirrorList = new ArrayList<>();
        }
        mRecyclerViewMirror.setVisibility(View.VISIBLE);
        mRecyclerViewPost.setVisibility(View.GONE);
        getData(IApiEvent.REQUEST_SEARCH_NEWS_FEED_MIRROR_CODE);
    }

    public void cancelSearchMirrorData() {
        mSearchText = "";
        mPageNumber = 1;
        mIsLoading = false;
        mIsUpdateRequired = false;
        mIsHasNextPage = false;

        if (mPostsDetailsList != null) {
            mPostsDetailsList.clear();
        } else {
            mPostsDetailsList = new ArrayList<>();
        }
        mRecyclerViewMirror.setVisibility(View.GONE);
        mRecyclerViewPost.setVisibility(View.VISIBLE);
        getData(IApiEvent.REQUEST_GET_POSTS_CODE);
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
            super.onBackPressed();
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
    public void onCommentClick(int position) {
        CommentsDialogFragment commentsDialogFragment = CommentsDialogFragment.newInstance(mPostsDetailsList.get(position));
        commentsDialogFragment.show(getSupportFragmentManager(), "CommentsDialogFragment");
    }

    @Override
    public void onMenuClick(final int position, View view) {
        PopupMenu popup = new PopupMenu(this, view, Gravity.END);
        popup.getMenuInflater().inflate(R.menu.post_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent;

                switch (item.getTitle().toString()) {
                    case Constants.UPDATE_POST:
                        intent = new Intent(HomeActivity.this, CreatePostActivity.class);
                        intent.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                        intent.putExtra(Constants.POST_DETAILS_KEY, mPostsDetailsList.get(position));
                        startActivity(intent);
                        return true;

                    case Constants.REMOVE_POST:
                        mPostId = mPostsDetailsList.get(position).postID;
                        getData(IApiEvent.REQUEST_REMOVE_POST_CODE);
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onSendClick(int position, String commentText) {
        mCommentText = commentText;
        mPostId = mPostsDetailsList.get(position).postID;
        getData(IApiEvent.REQUEST_ADD_COMMENT_CODE);
    }

    @Override
    public void onUserProfileClick(int position, int userId) {
        try {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            if (!(userId == Integer.parseInt(SharedPrefUtils.getUserId(this)))) {

                intent.putExtra(Constants.USER_ID_KEY, userId);
                intent.putExtra(Constants.IS_OTHER_PROFILE, true);
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLikeOrDislikeClick(int position, boolean isLike, boolean isUnLike) {

        mPostId = mPostsDetailsList.get(position).postID;
        mIsLike = isLike;
        mIsUnLike = isUnLike;

        getData(IApiEvent.REQUEST_POST_LIKE_CODE);
    }

    @Override
    public void onPostUpdate(GetPostsResponseModel.GetPostsDetails postDetails) {
        int position = 0;
        for (GetPostsResponseModel.GetPostsDetails tempPostDetails : mPostsDetailsList) {
            if (tempPostDetails.postID == postDetails.postID) {

                tempPostDetails.isUnLike = postDetails.isUnLike;
                tempPostDetails.isLike = postDetails.isLike;
                tempPostDetails.likeCount = postDetails.likeCount;
                tempPostDetails.unlikeCount = postDetails.unlikeCount;

                tempPostDetails.commentCount = postDetails.commentCount;
                tempPostDetails.commentUserImageName = postDetails.commentUserImageName;
                tempPostDetails.commentUserImageURL = postDetails.commentUserImageURL;
                tempPostDetails.commentUserFullName = postDetails.commentUserFullName;
                tempPostDetails.commentText = postDetails.commentText;
                position = mPostsDetailsList.indexOf(tempPostDetails);
                break;
            }
        }

        HomePostsAdapter homePostsAdapter = (HomePostsAdapter) mRecyclerViewPost.getAdapter();
        homePostsAdapter.notifyItemChanged(position);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mIsUpdateRequired) {
            mIsSearchData = true;
            mEtSearch.setText("");
            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
            cancelSearchMirrorData();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        mIsUpdateRequired = true;
    }

    @Override
    public void onMirrorClick(SearchInNewsFeedResponseModel.MirrorDetails mirrorDetails) {

        Intent intent = new Intent(this, MirrorDetailsActivity.class);
        intent.putExtra(Constants.MIRROR_ID_KEY, mirrorDetails.mirrorID);
        startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mIsSearchData = true;
        mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));

        if (count == 0) {
            onSearchClick();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * Method is used to perform action on search click.
     */
    public void onSearchClick() {
        mSearchText = mEtSearch.getText().toString().trim();

        if (mIsSearchData) {
            mIsSearchData = false;
            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.cross_white));
            searchMirrorData();
        } else {
            mIsSearchData = true;
            mEtSearch.setText("");
            mIvSearch.setImageDrawable(getResources().getDrawable(R.drawable.search));
            cancelSearchMirrorData();
        }
    }



/** get user location & update on server **/


    void updateUserLocation(LocationVM vm)
    {

        GetDataService service = ApiClient.getClient().create(GetDataService.class);
        Call<LocationResponse> call = service.userLocation(vm);
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {

            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {

                //Toast.makeText(HomeActivity.this, "Unable to find location", Toast.LENGTH_SHORT).show();
               }
        });
    }



@Override
protected void onStart() {
    gac.connect();
    super.onStart();
}

    @Override
    protected void onStop() {
        gac.disconnect();
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            updateUI(location);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
        Log.d(TAG, "onConnected");

        Location ll = LocationServices.FusedLocationApi.getLastLocation(gac);
        Log.d(TAG, "LastLocation: " + (ll == null ? "NO LastLocation" : ll.toString()));

        LocationServices.FusedLocationApi.requestLocationUpdates(gac, locationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(HomeActivity.this, "Permission was granted!", Toast.LENGTH_LONG).show();

                    try{
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                gac, locationRequest, this);
                    } catch (SecurityException e) {
                        Toast.makeText(HomeActivity.this, "SecurityException:\n" + e.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                   // Toast.makeText(HomeActivity.this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(HomeActivity.this, "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
        Log.d("DDD", connectionResult.toString());
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
       // Toast.makeText(getApplicationContext(),"lat:"+loc.getLatitude()+
        //  "lng:"+loc.getLongitude(),Toast.LENGTH_LONG).show();

        double longitude = loc.getLongitude();
        double latitude = loc.getLatitude();

        Integer userID = Integer.parseInt(SharedPrefUtils.getUserId(this));

        if (userID != null && longitude != 0.0) {
            LocationVM locationVM = new LocationVM();
            locationVM.setLatitude(Double.toString(latitude));
            locationVM.setLongitude(Double.toString(longitude));
            locationVM.setUserID(userID);
            updateUserLocation(locationVM);

            SharedPrefUtils.setLocation(HomeActivity.this, new LatLng(latitude, longitude));

        }


    }

    private boolean isLocationEnabled() {
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isGooglePlayServicesAvailable() {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        Log.d(TAG, "This device is supported.");
        return true;
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                     isFirstLaunch=false;

                    }
                });
        dialog.show();
    }


    public interface GetDataService {

        @POST(URL.USER_LOCATION)
        Call<LocationResponse> userLocation(@Body LocationVM locationVM);
    }



}
