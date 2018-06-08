package com.pend.activity.mirror;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
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
    private TextView mTvCantSayView;
    private TextView mTvHateView;
    private TextView mTvAdmireView;
    private TextView mTvCreatedBy;
    private TextView mTvWikiLink;
    private TextView mTvCategory;
    private int mPageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_poll_screen);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mViewpagerProfile = findViewById(R.id.viewpager_profile);
        mTvCategory = findViewById(R.id.tv_category);
        mTvWikiLink = findViewById(R.id.tv_wiki_link);
        mTvCreatedBy = findViewById(R.id.tv_created_by);
        mTvAdmireView = findViewById(R.id.tv_admire_view);
        mTvHateView = findViewById(R.id.tv_hate_view);
        mTvCantSayView = findViewById(R.id.tv_can_t_say_view);

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


                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    Snackbar.make(mRootView, getString(R.string.server_error_from_api), Snackbar.LENGTH_LONG).show();
                }
                getData(IApiEvent.REQUEST_GET_USER_TIME_SHEET_CODE);
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

        //TODO Change mirrorId
        int mirrorId = 1;

        switch (actionID) {
            case IApiEvent.REQUEST_GET_EXIT_POLL_MIRROR_CODE:

                String exitPollMirrorUrl = IWebServices.REQUEST_GET_EXIT_POLL_MIRROR_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this) + "&" +
                        Constants.PARAM_MIRROR_ID + "=" + mirrorId;
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
                        Constants.PARAM_MIRROR_ID + "=" + mirrorId + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
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
