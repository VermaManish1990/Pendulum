package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pend.BaseFragment;
import com.pend.R;

public class TrendingContestFragment extends BaseFragment {

    private View mRootView;
    private TextView mTvDataNotAvailable;
    private RecyclerView mRecyclerViewTrending;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
        mRecyclerViewTrending.setLayoutManager(new LinearLayoutManager(mContext));
//        mRecyclerViewTrending.setAdapter();
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID) {

    }

    @Override
    public void onAuthError() {

    }
}
