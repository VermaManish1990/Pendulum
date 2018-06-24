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
import com.pend.interfaces.IWebServices;
import com.pend.models.GetTrendingAndIntroducedMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.PaginationScrollListener;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;


public class TrendingMirrorFragment extends BaseFragment implements TrendingAndIntroducedMirrorAdapter.ITrendingAndIntroducedMirrorAdapterCallBack {

    private final String TAG = TrendingMirrorFragment.class.getSimpleName();
    private ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mMirrorList;
    private RecyclerView mRecyclerViewTrending;
    private Context mContext;
    private View mRootView;
    private int mPageNumber;
    private TextView mTvDataNotAvailable;
    private boolean mIsLoading;
    private boolean mIsHasNextPage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trending_mirror, container, false);

        initUI(view);
        setInitialData();

        getData(IApiEvent.REQUEST_GET_TRENDING_CODE);
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

        mPageNumber = 1;
        mIsLoading = false;
        mIsHasNextPage = false;

        mMirrorList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerViewTrending.setLayoutManager(linearLayoutManager);

        mRecyclerViewTrending.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mPageNumber += 1; //Increment page index to load the next one
                getData(IApiEvent.REQUEST_GET_TRENDING_CODE);
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
        mRecyclerViewTrending.setAdapter(new TrendingAndIntroducedMirrorAdapter(mContext, this, mMirrorList));
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_TRENDING_CODE:
                if (status) {
                    GetTrendingAndIntroducedMirrorResponseModel trendingAndIntroducedMirrorResponseModel =
                            (GetTrendingAndIntroducedMirrorResponseModel) serviceResponse;
                    if (trendingAndIntroducedMirrorResponseModel != null && trendingAndIntroducedMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, trendingAndIntroducedMirrorResponseModel.statusCode);

                        if (trendingAndIntroducedMirrorResponseModel.Data != null && trendingAndIntroducedMirrorResponseModel.Data.mirrorList != null) {

                            mTvDataNotAvailable.setVisibility(View.GONE);
                            mRecyclerViewTrending.setVisibility(View.VISIBLE);

                            mIsHasNextPage = !trendingAndIntroducedMirrorResponseModel.Data.hasNextPage;

                            TrendingAndIntroducedMirrorAdapter trendingAndIntroducedMirrorAdapter =
                                    (TrendingAndIntroducedMirrorAdapter) mRecyclerViewTrending.getAdapter();

                            mMirrorList.addAll(trendingAndIntroducedMirrorResponseModel.Data.mirrorList);
                            trendingAndIntroducedMirrorAdapter.setMirrorList(mMirrorList);
                            trendingAndIntroducedMirrorAdapter.notifyDataSetChanged();
                        } else {
                            mTvDataNotAvailable.setVisibility(View.VISIBLE);
                            mRecyclerViewTrending.setVisibility(View.GONE);
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }

                mIsLoading = false;
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
        ((BaseActivity) mContext).removeProgressDialog();
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {
        if (!ConnectivityUtils.isNetworkEnabled(mContext)) {
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }

        ((BaseActivity) mContext).showProgressDialog();

        switch (actionID) {
            case IApiEvent.REQUEST_GET_TRENDING_CODE:

                String trendingMirrorUrl = IWebServices.REQUEST_GET_TRENDING_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(mContext)
                        + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);

//                        + "&" + Constants.PARAM_SEARCH_TEXT + "=" + String.valueOf("search text");
                RequestManager.addRequest(new GsonObjectRequest<GetTrendingAndIntroducedMirrorResponseModel>(trendingMirrorUrl,
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
}
