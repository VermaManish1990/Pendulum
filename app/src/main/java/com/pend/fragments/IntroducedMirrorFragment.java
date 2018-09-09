package com.pend.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.BaseFragment;
import com.pend.R;
import com.pend.activity.mirror.MirrorDetailsActivity;
import com.pend.adapters.TrendingAndIntroducedMirrorAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IMirrorFragmentCallBack;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetTrendingAndIntroducedMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.PaginationScrollListener;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;


public class IntroducedMirrorFragment extends BaseFragment implements TrendingAndIntroducedMirrorAdapter.ITrendingAndIntroducedMirrorAdapterCallBack, View.OnClickListener {

    private final String TAG = IntroducedMirrorFragment.class.getSimpleName();
    private ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mMirrorList;
    private BaseActivity mContext;
    private RecyclerView mRecyclerViewIntroduced;
    private View mRootView;
    private int mPageNumber;
    private IMirrorFragmentCallBack mIMirrorFragmentCallBack;
    private TextView mTvDataNotAvailable;
    private boolean mIsHasNextPage;
    private boolean mIsLoading;
    private String mSearchText;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (BaseActivity) context;
        mIMirrorFragmentCallBack = (IMirrorFragmentCallBack) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_introduced_mirror, container, false);

        initUI(view);
        setInitialData();

        getData(IApiEvent.REQUEST_GET_INTRODUCED_CODE);
        return view;
    }

    @Override
    protected void initUI(View view) {
        mRootView = view.findViewById(R.id.root_view);
        mTvDataNotAvailable = view.findViewById(R.id.tv_data_not_available);
        mRecyclerViewIntroduced = view.findViewById(R.id.recycler_view_introduced);
        view.findViewById(R.id.bt_create_mirror).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        mSearchText = null;
        mIsHasNextPage = false;
        mIsLoading = false;
        mPageNumber = 1;
        mMirrorList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerViewIntroduced.setLayoutManager(linearLayoutManager);

        mRecyclerViewIntroduced.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mPageNumber += 1; //Increment page index to load the next one
                getData(IApiEvent.REQUEST_GET_INTRODUCED_CODE);
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
        mRecyclerViewIntroduced.setAdapter(new TrendingAndIntroducedMirrorAdapter(mContext, this, mMirrorList));
    }

    public void searchMirrorData(String searchText) {
        mSearchText = searchText;
        mPageNumber = 1;
        mIsLoading = false;
        mIsHasNextPage = false;

        if (mMirrorList != null) {
            mMirrorList.clear();
        } else {
            mMirrorList = new ArrayList<>();
        }
        getData(IApiEvent.REQUEST_GET_INTRODUCED_CODE);
    }

    public void cancelSearchMirrorData() {
        mSearchText = "";
        mPageNumber = 1;
        mIsLoading = false;
        mIsHasNextPage = false;

        if (mMirrorList != null) {
            mMirrorList.clear();
        } else {
            mMirrorList = new ArrayList<>();
        }
        getData(IApiEvent.REQUEST_GET_INTRODUCED_CODE);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_INTRODUCED_CODE:
                if (status) {
                    GetTrendingAndIntroducedMirrorResponseModel trendingAndIntroducedMirrorResponseModel =
                            (GetTrendingAndIntroducedMirrorResponseModel) serviceResponse;
                    if (trendingAndIntroducedMirrorResponseModel != null && trendingAndIntroducedMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, trendingAndIntroducedMirrorResponseModel.statusCode);

                        if (trendingAndIntroducedMirrorResponseModel.Data != null && trendingAndIntroducedMirrorResponseModel.Data.mirrorList != null) {

                            mTvDataNotAvailable.setVisibility(View.GONE);
                            mRecyclerViewIntroduced.setVisibility(View.VISIBLE);

                            mIsHasNextPage = !trendingAndIntroducedMirrorResponseModel.Data.hasNextPage;

                            TrendingAndIntroducedMirrorAdapter trendingAndIntroducedMirrorAdapter =
                                    (TrendingAndIntroducedMirrorAdapter) mRecyclerViewIntroduced.getAdapter();

                            mMirrorList.addAll(trendingAndIntroducedMirrorResponseModel.Data.mirrorList);
                            trendingAndIntroducedMirrorAdapter.setMirrorList(mMirrorList);
                            trendingAndIntroducedMirrorAdapter.notifyDataSetChanged();

//                            mIntroducedMirrorFragmentCallBack.onTrendingMirrorResultUpdated(mMirrorList);
                        } else {
                            mTvDataNotAvailable.setVisibility(View.VISIBLE);
                            mRecyclerViewIntroduced.setVisibility(View.GONE);
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        mTvDataNotAvailable.setVisibility(View.VISIBLE);
                        mRecyclerViewIntroduced.setVisibility(View.GONE);
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    mTvDataNotAvailable.setVisibility(View.VISIBLE);
                    mRecyclerViewIntroduced.setVisibility(View.GONE);
                }

                mIsLoading = false;
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }

        if (mContext != null && !mContext.isDestroyed() && !mContext.isFinishing() && isAdded()) {

            mContext.removeProgressDialog();
        }
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {

        if (mContext != null && !mContext.isDestroyed() && !mContext.isFinishing() && isAdded()) {

            if (!ConnectivityUtils.isNetworkEnabled(mContext)) {
                Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
                return;
            }
            mContext.showProgressDialog();
        }else {
            return;
        }

        switch (actionID) {
            case IApiEvent.REQUEST_GET_INTRODUCED_CODE:

                String introducedMirrorUrl;
                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    introducedMirrorUrl = IWebServices.REQUEST_GET_INTRODUCED_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(mContext)
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber)
                            + "&" + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;
                } else {
                    introducedMirrorUrl = IWebServices.REQUEST_GET_INTRODUCED_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(mContext)
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
                }

                RequestManager.addRequest(new GsonObjectRequest<GetTrendingAndIntroducedMirrorResponseModel>(introducedMirrorUrl,
                        NetworkUtil.getHeaders(mContext), null, GetTrendingAndIntroducedMirrorResponseModel.class,
                        new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetTrendingAndIntroducedMirrorResponseModel response) {
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
    public void onMirrorClick(int position) {
        GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails mirrorDetails = mMirrorList.get(position);
        Intent intent = new Intent(mContext, MirrorDetailsActivity.class);
        intent.putExtra(Constants.MIRROR_ID_KEY, mirrorDetails.mirrorID);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create_mirror:
                mIMirrorFragmentCallBack.onCreateMirrorClick();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
