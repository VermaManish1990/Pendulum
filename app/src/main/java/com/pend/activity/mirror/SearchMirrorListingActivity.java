package com.pend.activity.mirror;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.SearchMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class SearchMirrorListingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SearchMirrorListingActivity.class.getSimpleName();
    private View mRootView;
    private RecyclerView mRecyclerViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_mirror_listing);

        initUI();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mRecyclerViewSearch = findViewById(R.id.recycler_view_search);

        findViewById(R.id.tv_create_mirror).setOnClickListener(this);

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_SEARCH_MIRROR_CODE:
                if (status) {
                    SearchMirrorResponseModel searchMirrorResponseModel = (SearchMirrorResponseModel) serviceResponse;
                    if (searchMirrorResponseModel != null && searchMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, searchMirrorResponseModel.statusCode);


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
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            return;
        }
        showProgressDialog();

        switch (actionID) {
            case IApiEvent.REQUEST_SEARCH_MIRROR_CODE:

                //TODO search text
                String searchMirrorUrl = IWebServices.REQUEST_SEARCH_MIRROR_URL + Constants.PARAM_SEARCH_TEXT + "=" + String.valueOf("search text");
                RequestManager.addRequest(new GsonObjectRequest<SearchMirrorResponseModel>(searchMirrorUrl, NetworkUtil.getHeaders(this),
                        null, SearchMirrorResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(SearchMirrorResponseModel response) {
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
            case R.id.tv_create_mirror:
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
