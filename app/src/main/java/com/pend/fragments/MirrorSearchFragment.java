package com.pend.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.pend.adapters.SearchInNewsFeedAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IMirrorFragmentCallBack;
import com.pend.models.GetTrendingAndIntroducedMirrorResponseModel;
import com.pend.util.LoggerUtil;

import java.util.ArrayList;
import java.util.Objects;


public class MirrorSearchFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = MirrorSearchFragment.class.getSimpleName();
    private static final String ARG_MIRROR_LIST = "ARG_MIRROR_LIST";
    private BaseActivity mContext;
    private RecyclerView mRecyclerViewMirror;
    private TextView mTvSeeMore;
    private View mRlNoResult;
    private View mRlMirrorData;
    private IMirrorFragmentCallBack mIMirrorFragmentCallBack;
    private ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mSearchDataList;
    private View mRootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MirrorSearchFragment.
     */
    public static MirrorSearchFragment newInstance(ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> searchDataList) {
        MirrorSearchFragment fragment = new MirrorSearchFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MIRROR_LIST, searchDataList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchDataList = (ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails>)
                    getArguments().getSerializable(ARG_MIRROR_LIST);
        }
    }

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
        View view = inflater.inflate(R.layout.fragment_mirror_search, container, false);

        initUI(view);
        setInitialData();

        return view;
    }

    @Override
    protected void initUI(View view) {

        mRootView = view.findViewById(R.id.root_view);
        mRecyclerViewMirror = view.findViewById(R.id.recycler_view_mirror);
        mTvSeeMore = view.findViewById(R.id.tv_see_more);
        mRlNoResult = view.findViewById(R.id.rl_no_result);
        mRlMirrorData = view.findViewById(R.id.rl_mirror_data);
        view.findViewById(R.id.bt_create_mirror).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        if (mSearchDataList != null) {

            if (mSearchDataList.size() > 0) {
                mRlMirrorData.setVisibility(View.VISIBLE);
                mRlNoResult.setVisibility(View.GONE);
            } else {
                mRlMirrorData.setVisibility(View.GONE);
                mRlNoResult.setVisibility(View.VISIBLE);
            }
            mRecyclerViewMirror.setLayoutManager(new LinearLayoutManager(mContext));
//            SearchInNewsFeedAdapter mirrorSearchAdapter = new SearchInNewsFeedAdapter(mContext, this, mSearchDataList);
//            mRecyclerViewMirror.setAdapter(mirrorSearchAdapter);
        }
    }

    public void setSearchDataList(ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> searchDataList) {
        mSearchDataList = searchDataList;

        if (mSearchDataList != null && mSearchDataList.size() > 0) {

            mRlMirrorData.setVisibility(View.VISIBLE);
            mRlNoResult.setVisibility(View.GONE);
            SearchInNewsFeedAdapter searchInNewsFeedAdapter = (SearchInNewsFeedAdapter) mRecyclerViewMirror.getAdapter();
//            searchInNewsFeedAdapter.setSearchDataList(mSearchDataList);
            searchInNewsFeedAdapter.notifyDataSetChanged();
        } else {
            mRlMirrorData.setVisibility(View.GONE);
            mRlNoResult.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {

    }

    @Override
    public void onAuthError() {

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
