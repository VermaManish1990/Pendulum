package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.adapters.SearchInNewsFeedAdapter;
import com.pend.adapters.TrendingAndIntroducedMirrorAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetPostsResponseModel;
import com.pend.models.SearchInNewsFeedResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.PaginationScrollListener;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.ui.IScreen;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;


public class ContestSearchDialogFragment extends DialogFragment implements IScreen, View.OnClickListener{

    private static final String TAG = ContestSearchDialogFragment.class.getSimpleName();
    private static final String ARG_SEARCH_TEXT = "ARG_SEARCH_TEXT";
    private BaseActivity mContext;
    private int mPageNumber;
    private String mSearchText;
    private boolean mIsHasNextPage;
    private boolean mIsLoading;
    private TextView mDataNotAvailableView;
    private RecyclerView mRecyclerViewContest;
    private ArrayList<SearchInNewsFeedResponseModel.MirrorDetails> mContestSearchList;

    public static ContestSearchDialogFragment newInstance(String searchText) {
        ContestSearchDialogFragment fragment = new ContestSearchDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_TEXT, searchText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);

        if (getArguments() != null) {
            mSearchText = getArguments().getString(ARG_SEARCH_TEXT,null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contest_search_dialog, container, false);

        initUi(view);

        setInitialData();

        getData(IApiEvent.REQUEST_SEARCH_NEWS_FEED_MIRROR_CODE);

        return view;
    }

    private void initUi(View view) {
        mDataNotAvailableView = view.findViewById(R.id.fl_data_not_available);
        mRecyclerViewContest = view.findViewById(R.id.recycler_view_contest);

        view.findViewById(R.id.bt_create_mirror).setOnClickListener(this);
    }

    protected void setInitialData() {

        mSearchText = null;
        mPageNumber = 1;
        mIsLoading = false;
        mIsHasNextPage = false;

        mContestSearchList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerViewContest.setLayoutManager(linearLayoutManager);

        mRecyclerViewContest.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
        mRecyclerViewContest.setAdapter(new SearchInNewsFeedAdapter(mContext, mContestSearchList));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (BaseActivity) context;
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

        switch (actionID) {
            case IApiEvent.REQUEST_SEARCH_NEWS_FEED_MIRROR_CODE:
                if (status) {
                    SearchInNewsFeedResponseModel searchInNewsFeedResponseModel = (SearchInNewsFeedResponseModel) serviceResponse;
                    if (searchInNewsFeedResponseModel != null && searchInNewsFeedResponseModel.status) {
                        LoggerUtil.d(TAG, searchInNewsFeedResponseModel.statusCode);

                        if (searchInNewsFeedResponseModel.Data != null && searchInNewsFeedResponseModel.Data.mirrorList != null) {

                            mDataNotAvailableView.setVisibility(View.GONE);
                            mRecyclerViewContest.setVisibility(View.VISIBLE);

                            mIsHasNextPage = !searchInNewsFeedResponseModel.Data.hasNextPage;

                            SearchInNewsFeedAdapter searchInNewsFeedAdapter = (SearchInNewsFeedAdapter) mRecyclerViewContest.getAdapter();
                            mContestSearchList.addAll(searchInNewsFeedResponseModel.Data.mirrorList);
                            searchInNewsFeedAdapter.notifyDataSetChanged();
                        } else {
                            mDataNotAvailableView.setVisibility(View.VISIBLE);
                            mRecyclerViewContest.setVisibility(View.GONE);
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        mDataNotAvailableView.setVisibility(View.VISIBLE);
                        mRecyclerViewContest.setVisibility(View.GONE);
                    }
                } else {
                    mDataNotAvailableView.setVisibility(View.VISIBLE);
                    mRecyclerViewContest.setVisibility(View.GONE);
                }

                mIsLoading = false;
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
//            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
                return;
            }
            mContext.showProgressDialog();
        } else {
            return;
        }

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_SEARCH_NEWS_FEED_MIRROR_CODE:

                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    String searchInNewsFeedUrl = IWebServices.REQUEST_SEARCH_NEWS_FEED_MIRROR_URL + Constants.PARAM_USER_ID + "=" + userId
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber)
                            + "&" + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;
                    RequestManager.addRequest(new GsonObjectRequest<SearchInNewsFeedResponseModel>(searchInNewsFeedUrl, NetworkUtil.getHeaders(mContext),
                            null, SearchInNewsFeedResponseModel.class, new VolleyErrorListener(this, actionID)) {

                        @Override
                        protected void deliverResponse(SearchInNewsFeedResponseModel response) {
                            updateUi(true, actionID, response);
                        }
                    });
                } else {
                    mContext.removeProgressDialog();
                }

                break;
        }

    }

    @Override
    public void onAuthError() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create_mirror:
//                mIMirrorFragmentCallBack.onCreateMirrorClick();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
