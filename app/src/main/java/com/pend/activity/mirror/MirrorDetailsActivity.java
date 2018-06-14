package com.pend.activity.mirror;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.jjoe64.graphview.GraphView;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetMirrorDetailsResponseModel;
import com.pend.models.GetMirrorGraphResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;
import com.squareup.picasso.Picasso;

public class MirrorDetailsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MirrorDetailsActivity.class.getSimpleName();
    private View mRootView;
    private ImageView mIvProfile;
    private GraphView mGraphView;
    private TextView mTvName;
    private ProgressBar mProgressBarProfile;
    private RecyclerView mRecyclerViewPost;
    private int mMirrorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror_details);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.MIRROR_ID_KEY)) {
                mMirrorId = localBundle.getInt(Constants.MIRROR_ID_KEY, 0);
            }
        }

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mIvProfile = findViewById(R.id.iv_profile);
        mGraphView = findViewById(R.id.graph_view);
        mTvName = findViewById(R.id.tv_name);
        mProgressBarProfile = findViewById(R.id.progress_bar__profile);
        mRecyclerViewPost = findViewById(R.id.recycler_view_post);

        findViewById(R.id.iv_create_post).setOnClickListener(this);
        mIvProfile.setOnClickListener(this);
        mProgressBarProfile.setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE:
                if (status) {
                    GetMirrorDetailsResponseModel mirrorDetailsResponseModel = (GetMirrorDetailsResponseModel) serviceResponse;
                    if (mirrorDetailsResponseModel != null && mirrorDetailsResponseModel.status) {
                        LoggerUtil.d(TAG, mirrorDetailsResponseModel.statusCode);

                        if (mirrorDetailsResponseModel.Data != null && mirrorDetailsResponseModel.Data.mirrorData != null) {

                            GetMirrorDetailsResponseModel.MirrorData mirrorData = mirrorDetailsResponseModel.Data.mirrorData;

                            mTvName.setText(mirrorData.mirrorName != null ? mirrorData.mirrorName : "");

                            if (mirrorData.imageURL != null && !mirrorData.imageURL.equals("")) {

                                Picasso.with(this)
                                        .load(mirrorData.imageURL != null ? mirrorData.imageURL : "")
                                        .into(mIvProfile);
                            }
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }

                getData(IApiEvent.REQUEST_GET_MIRROR_GRAPH_DATA_CODE);

                break;

            case IApiEvent.REQUEST_GET_MIRROR_GRAPH_DATA_CODE:
                if (status) {
                    GetMirrorGraphResponseModel mirrorGraphResponseModel = (GetMirrorGraphResponseModel) serviceResponse;
                    if (mirrorGraphResponseModel != null && mirrorGraphResponseModel.status) {
                        LoggerUtil.d(TAG, mirrorGraphResponseModel.statusCode);

                        if (mirrorGraphResponseModel.Data != null && mirrorGraphResponseModel.Data.graphData != null) {

                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
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

        switch (actionID) {
            case IApiEvent.REQUEST_GET_MIRROR_DETAILS_CODE:

                String mirrorDetailsUrl = IWebServices.REQUEST_GET_MIRROR_DETAILS_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
                        + "&" + Constants.PARAM_MIRROR_ID + "=" + String.valueOf(mMirrorId);
                RequestManager.addRequest(new GsonObjectRequest<GetMirrorDetailsResponseModel>(mirrorDetailsUrl, NetworkUtil.getHeaders(this),
                        null, GetMirrorDetailsResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetMirrorDetailsResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_GET_MIRROR_GRAPH_DATA_CODE:

                String mirrorGraphDataUrl = IWebServices.REQUEST_GET_MIRROR_GRAPH_DATA_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(this)
                        + "&" + Constants.PARAM_MIRROR_ID + "=" + String.valueOf(mMirrorId)
                        + "&" + Constants.PARAM_MONTH + "=" + String.valueOf("1")
                        + "&" + Constants.PARAM_YEAR + "=" + String.valueOf("2018");
                RequestManager.addRequest(new GsonObjectRequest<GetMirrorGraphResponseModel>(mirrorGraphDataUrl, NetworkUtil.getHeaders(this),
                        null, GetMirrorGraphResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetMirrorGraphResponseModel response) {
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

            case R.id.iv_create_post:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                break;

            case R.id.iv_profile:

                Intent intent = new Intent(MirrorDetailsActivity.this, ExitPollScreenActivity.class);
                intent.putExtra(Constants.MIRROR_ID_KEY, mMirrorId);
                startActivity(intent);

                break;

            case R.id.progress_bar__profile:
                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;

        }
    }
}
