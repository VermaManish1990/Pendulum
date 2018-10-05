package com.pend.fragments;

import android.content.Context;
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
import com.pend.adapters.ContestAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.ContestResponseModel;
import com.pend.models.GetContestsResponseModel;
import com.pend.models.GetTrendingAndIntroducedMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;

public class TrendingContestFragment extends BaseFragment {

    private static final String TAG = TrendingContestFragment.class.getSimpleName();
    private View mRootView;
    private TextView mTvDataNotAvailable;
    private RecyclerView mRecyclerViewTrending;
    private BaseActivity mContext;
    private int mPageNumber;
    private String mSearchText;
    private boolean mIsHasNextPage;
    private boolean mIsLoading;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (BaseActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending_contest, container, false);

        initUI(view);
        setInitialData();
        return view;
    }

    @Override
    protected void initUI(View view) {

        mRootView = view.findViewById(R.id.root_view);
        mTvDataNotAvailable = view.findViewById(R.id.tv_data_not_available);
        mRecyclerViewTrending = view.findViewById(R.id.recycler_view_trending);
    }

    @Override
    protected void setInitialData() {

        mSearchText = "";
        mPageNumber = 1;
        mIsLoading = false;
        mIsHasNextPage = false;

        mRecyclerViewTrending.setVisibility(View.VISIBLE);
        mTvDataNotAvailable.setVisibility(View.GONE);
        ArrayList<ContestResponseModel.ContestDetails> contestDetailsList = new ArrayList<>();
        contestDetailsList.add(new ContestResponseModel.ContestDetails(1, 40, 60, 0));
        contestDetailsList.add(new ContestResponseModel.ContestDetails(2, 40, 25, 35));
        contestDetailsList.add(new ContestResponseModel.ContestDetails(1, 70, 30, 0));
        contestDetailsList.add(new ContestResponseModel.ContestDetails(2, 60, 10, 30));

        contestDetailsList.addAll(contestDetailsList);
        contestDetailsList.addAll(contestDetailsList);
        contestDetailsList.addAll(contestDetailsList);

        mRecyclerViewTrending.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewTrending.setAdapter(new ContestAdapter(mContext, contestDetailsList));
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_INTRODUCED_CONTESTS_CODE:
                if (status) {
                    GetContestsResponseModel contestsResponseModel =
                            (GetContestsResponseModel) serviceResponse;
                    if (contestsResponseModel != null && contestsResponseModel.status) {
                        LoggerUtil.d(TAG, contestsResponseModel.statusCode);

                        if (contestsResponseModel.Data != null && contestsResponseModel.Data.contestList != null) {

                            mTvDataNotAvailable.setVisibility(View.GONE);
                            mRecyclerViewTrending.setVisibility(View.VISIBLE);

                            mIsHasNextPage = !contestsResponseModel.Data.hasNextPage;

                        } else {
                            mTvDataNotAvailable.setVisibility(View.VISIBLE);
                            mRecyclerViewTrending.setVisibility(View.GONE);
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        mTvDataNotAvailable.setVisibility(View.VISIBLE);
                        mRecyclerViewTrending.setVisibility(View.GONE);
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    mTvDataNotAvailable.setVisibility(View.VISIBLE);
                    mRecyclerViewTrending.setVisibility(View.GONE);
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
    public void getData(int actionID) {
        if (mContext != null && !mContext.isDestroyed() && !mContext.isFinishing() && isAdded()) {

            if (!ConnectivityUtils.isNetworkEnabled(mContext)) {
                Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
                return;
            }
            mContext.showProgressDialog();
        } else {
            return;
        }

        switch (actionID) {
            case IApiEvent.REQUEST_GET_INTRODUCED_CONTESTS_CODE:

                String trendingMirrorUrl;
                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    trendingMirrorUrl = IWebServices.REQUEST_GET_INTRODUCED_CONTESTS_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(mContext)
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber)
                            + "&" + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;
                } else {
                    trendingMirrorUrl = IWebServices.REQUEST_GET_INTRODUCED_CONTESTS_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(mContext)
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
                }

                RequestManager.addRequest(new GsonObjectRequest<GetContestsResponseModel>(trendingMirrorUrl,
                        NetworkUtil.getHeaders(mContext), null, GetContestsResponseModel.class,
                        new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetContestsResponseModel response) {
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
}
