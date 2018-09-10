package com.pend.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.pend.BaseActivity;
import com.pend.BaseFragment;
import com.pend.R;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.activity.mirror.MirrorDetailsActivity;
import com.pend.adapters.FollowingMirrorAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IMirrorFragmentCallBack;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetFollowingMirrorResponseModel;
import com.pend.util.GridPaginationScrollListener;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;


public class FollowingMirrorFragment extends BaseFragment implements View.OnClickListener {

    private BaseActivity mContext;
    private View mRootView;
    private GridView mGridViewFollowingMirror;
    private final String TAG = FollowingMirrorFragment.class.getSimpleName();
    private ArrayList<GetFollowingMirrorResponseModel.GetFollowingMirrorDetails> mMirrorList;
    private int mPageNumber;
    private View mDataNotAvailableView;
    private boolean mIsLoading;
    private boolean mIsHasNextPage;
    private String mSearchText;
    private IMirrorFragmentCallBack mIMirrorFragmentCallBack;

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
        View view = inflater.inflate(R.layout.fragment_following_mirror, container, false);

        initUI(view);
        setInitialData();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSearchText = "";
        mPageNumber = 1;
        mIsLoading = false;
        mIsHasNextPage = false;
        if (!((MirrorActivity) mContext).mIsUpdateRequired)
            getData(IApiEvent.REQUEST_GET_FOLLOWING_CODE);
    }

    @Override
    protected void initUI(View view) {
        mRootView = view.findViewById(R.id.root_view);
        mDataNotAvailableView = view.findViewById(R.id.fl_data_not_available);
        mGridViewFollowingMirror = view.findViewById(R.id.grid_view_following_mirror);

        view.findViewById(R.id.bt_create_mirror).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        mSearchText = null;
        mPageNumber = 1;
        mIsLoading = false;
        mIsHasNextPage = false;
        mMirrorList = new ArrayList<>();

        mGridViewFollowingMirror.setOnScrollListener(new GridPaginationScrollListener() {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mPageNumber += 1; //Increment page index to load the next one
                getData(IApiEvent.REQUEST_GET_FOLLOWING_CODE);
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
        mGridViewFollowingMirror.setAdapter(new FollowingMirrorAdapter(mContext, mMirrorList));

        /*
          On Click event for Single Grid View Item
          */
        mGridViewFollowingMirror.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();

                GetFollowingMirrorResponseModel.GetFollowingMirrorDetails mirrorDetails = mMirrorList.get(position);
                Intent intent = new Intent(mContext, MirrorDetailsActivity.class);
                intent.putExtra(Constants.MIRROR_ID_KEY, mirrorDetails.mirrorID);
                startActivity(intent);
            }
        });

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
        getData(IApiEvent.REQUEST_GET_FOLLOWING_CODE);
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
        getData(IApiEvent.REQUEST_GET_FOLLOWING_CODE);
    }


    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_FOLLOWING_CODE:
                if (status) {
                    GetFollowingMirrorResponseModel followingMirrorResponseModel = (GetFollowingMirrorResponseModel) serviceResponse;
                    if (followingMirrorResponseModel != null && followingMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, followingMirrorResponseModel.statusCode);

                        if (followingMirrorResponseModel.Data != null && followingMirrorResponseModel.Data.mirrorList != null) {

                            mDataNotAvailableView.setVisibility(View.GONE);
                            mGridViewFollowingMirror.setVisibility(View.VISIBLE);

                            mIsHasNextPage = !followingMirrorResponseModel.Data.hasNextPage;

                            FollowingMirrorAdapter followingMirrorAdapter = (FollowingMirrorAdapter) mGridViewFollowingMirror.getAdapter();
                            if (mPageNumber == 1) {
                                mMirrorList.clear();
                            }
                            mMirrorList.addAll(followingMirrorResponseModel.Data.mirrorList);
                            followingMirrorAdapter.setMirrorList(mMirrorList);
                            followingMirrorAdapter.notifyDataSetChanged();
                        } else {
                            mDataNotAvailableView.setVisibility(View.VISIBLE);
                            mGridViewFollowingMirror.setVisibility(View.GONE);
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        mDataNotAvailableView.setVisibility(View.VISIBLE);
                        mGridViewFollowingMirror.setVisibility(View.GONE);
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    mDataNotAvailableView.setVisibility(View.VISIBLE);
                    mGridViewFollowingMirror.setVisibility(View.GONE);
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
        } else {
            return;
        }

        switch (actionID) {
            case IApiEvent.REQUEST_GET_FOLLOWING_CODE:

                String followingMirrorUrl;
                if (mSearchText != null) {

                    mSearchText = OtherUtil.replaceWithPattern(mSearchText, " ");
                    mSearchText = mSearchText.replaceAll(" ", "%20");

                    followingMirrorUrl = IWebServices.REQUEST_GET_FOLLOWING_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(mContext)
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber)
                            + "&" + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;
                } else {
                    followingMirrorUrl = IWebServices.REQUEST_GET_FOLLOWING_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(mContext)
                            + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
                }

                RequestManager.addRequest(new GsonObjectRequest<GetFollowingMirrorResponseModel>(followingMirrorUrl,
                        NetworkUtil.getHeaders(mContext), null, GetFollowingMirrorResponseModel.class,
                        new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(GetFollowingMirrorResponseModel response) {
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
        switch (view.getId()){
            case R.id.bt_create_mirror:
                mIMirrorFragmentCallBack.onCreateMirrorClick();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
