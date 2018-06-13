package com.pend.activity.mirror;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetExitPollListResponseModel;
import com.pend.models.GetExitPollMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class ExitPollScreenActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ExitPollScreenActivity.class.getSimpleName();
    private View mRootView;
    private ViewPager mViewpagerProfile;
    private TextView mTvCreatedBy;
    private TextView mTvWikiLink;
    private TextView mTvCategory;
    private int mPageNumber;
    private int mMirrorId;
    private RecyclerView mRecyclerViewExitPoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_poll_screen);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.MIRROR_ID_KEY)) {
                mMirrorId = localBundle.getInt(Constants.MIRROR_ID_KEY, 0);
            }
        }

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_GET_EXIT_POLL_MIRROR_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mViewpagerProfile = findViewById(R.id.viewpager_profile);
        mTvCategory = findViewById(R.id.tv_category);
        mTvWikiLink = findViewById(R.id.tv_wiki_link);
        mTvCreatedBy = findViewById(R.id.tv_created_by);
        mRecyclerViewExitPoll = findViewById(R.id.recycler_view_exit_poll);

        findViewById(R.id.tv_related_contest).setOnClickListener(this);
        findViewById(R.id.tv_reflections).setOnClickListener(this);
        findViewById(R.id.tv_share_on_facebook).setOnClickListener(this);

    }

    @Override
    protected void setInitialData() {


    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_EXIT_POLL_MIRROR_CODE:
                if (status) {
                    GetExitPollMirrorResponseModel exitPollMirrorResponseModel = (GetExitPollMirrorResponseModel) serviceResponse;
                    if (exitPollMirrorResponseModel != null && exitPollMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, exitPollMirrorResponseModel.statusCode);

                        if (exitPollMirrorResponseModel.Data != null && exitPollMirrorResponseModel.Data.mirrorData != null) {
                            GetExitPollMirrorResponseModel.GetExitPollMirrorDetails mirrorDetails = exitPollMirrorResponseModel.Data.mirrorData;
                            mTvCategory.setText(mirrorDetails.mirrorName != null ? mirrorDetails.mirrorName : "");
                            mTvWikiLink.setText(mirrorDetails.mirrorWikiLink != null ? mirrorDetails.mirrorWikiLink : "");
                            mTvCreatedBy.setText(mirrorDetails.userFullName != null ? mirrorDetails.userFullName : "");
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                }
                getData(IApiEvent.REQUEST_GET_EXIT_POLL_LIST_CODE);
                break;

            case IApiEvent.REQUEST_GET_EXIT_POLL_LIST_CODE:
                if (status) {
                    GetExitPollListResponseModel exitPollListResponseModel = (GetExitPollListResponseModel) serviceResponse;
                    if (exitPollListResponseModel != null && exitPollListResponseModel.status) {
                        LoggerUtil.d(TAG, exitPollListResponseModel.statusCode);


                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
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

        switch (actionID) {
            case IApiEvent.REQUEST_GET_EXIT_POLL_MIRROR_CODE:

                String exitPollMirrorUrl = IWebServices.REQUEST_GET_EXIT_POLL_MIRROR_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this) + "&" +
                        Constants.PARAM_MIRROR_ID + "=" + mMirrorId;
                RequestManager.addRequest(new GsonObjectRequest<GetExitPollMirrorResponseModel>(exitPollMirrorUrl, NetworkUtil.getHeaders(this), null,
                        GetExitPollMirrorResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetExitPollMirrorResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_GET_EXIT_POLL_LIST_CODE:

                //TODO Pagination

                mPageNumber = 1;
                String exitPollListUrl = IWebServices.REQUEST_GET_EXIT_POLL_LIST_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this) + "&" +
                        Constants.PARAM_MIRROR_ID + "=" + mMirrorId + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
                RequestManager.addRequest(new GsonObjectRequest<GetExitPollListResponseModel>(exitPollListUrl, NetworkUtil.getHeaders(this), null,
                        GetExitPollListResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetExitPollListResponseModel response) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_related_contest:
                break;

            case R.id.tv_reflections:

                Intent intentReflection = new Intent(ExitPollScreenActivity.this, ReflectionsActivity.class);
                intentReflection.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                startActivity(intentReflection);
                break;

            case R.id.tv_share_on_facebook:
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
