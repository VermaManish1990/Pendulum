package com.pend.activity.mirror;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.SearchMirrorAdapter;
import com.pend.fragments.CreateMirrorDialogFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.SearchMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class SearchMirrorListingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SearchMirrorListingActivity.class.getSimpleName();
    private View mRootView;
    private RecyclerView mRecyclerViewSearch;
    private String mSearchText;
    private ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> mSearchDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_mirror_listing);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.SEARCH_TEXT_KEY)) {
                mSearchText = localBundle.getString(Constants.SEARCH_TEXT_KEY, null);
            }
        }

        initUI();
        setInitialData();

        getData(IApiEvent.REQUEST_SEARCH_MIRROR_CODE);
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mRecyclerViewSearch = findViewById(R.id.recycler_view_search);

        findViewById(R.id.tv_create_mirror).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {
        mSearchDataList = new ArrayList<>();

        mRecyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        SearchMirrorAdapter searchMirrorAdapter = new SearchMirrorAdapter(this, mSearchDataList);
        mRecyclerViewSearch.setAdapter(searchMirrorAdapter);

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_SEARCH_MIRROR_CODE:
                if (status) {
                    SearchMirrorResponseModel searchMirrorResponseModel = (SearchMirrorResponseModel) serviceResponse;
                    if (searchMirrorResponseModel != null && searchMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, searchMirrorResponseModel.statusCode);

                        if (searchMirrorResponseModel.Data != null && searchMirrorResponseModel.Data.searchData != null) {
                            SearchMirrorAdapter searchMirrorAdapter = (SearchMirrorAdapter) mRecyclerViewSearch.getAdapter();
                            mSearchDataList.addAll(searchMirrorResponseModel.Data.searchData);
                            searchMirrorAdapter.setSearchDataList(mSearchDataList);
                            searchMirrorAdapter.notifyDataSetChanged();
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
            case IApiEvent.REQUEST_SEARCH_MIRROR_CODE:

                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    String searchMirrorUrl = IWebServices.REQUEST_SEARCH_MIRROR_URL + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;
                    RequestManager.addRequest(new GsonObjectRequest<SearchMirrorResponseModel>(searchMirrorUrl, NetworkUtil.getHeaders(this),
                            null, SearchMirrorResponseModel.class, new VolleyErrorListener(this, actionID)) {

                        @Override
                        protected void deliverResponse(SearchMirrorResponseModel response) {
                            updateUi(true, actionID, response);

                        }
                    });
                } else {
                    LoggerUtil.d(TAG, getString(R.string.search_text_is_not_valid));
                }
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
                DialogFragment createMirrorDialogFragment = new CreateMirrorDialogFragment();
                createMirrorDialogFragment.show(getSupportFragmentManager(), "CreateMirrorDialogFragment");
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
