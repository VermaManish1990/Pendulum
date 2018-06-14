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
import com.pend.adapters.MirrorSearchAdapter;
import com.pend.models.SearchMirrorResponseModel;
import com.pend.util.LoggerUtil;

import java.util.ArrayList;


public class MirrorSearchFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = MirrorSearchFragment.class.getSimpleName();
    private Context mContext;
    private RecyclerView mRecyclerViewMirror;
    private TextView mTvSeeMore;
    private View mRlNoResult;
    private View mRlMirrorData;
    private IMirrorFragmentCallBack mIMirrorFragmentCallBack;
    private ArrayList<SearchMirrorResponseModel.SearchMirrorDetails> mSearchDataList;
    private View mRootView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MirrorSearchFragment.
     */
    public static MirrorSearchFragment newInstance() {
        MirrorSearchFragment fragment = new MirrorSearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

        mSearchDataList = new ArrayList<>();

        mRecyclerViewMirror.setLayoutManager(new LinearLayoutManager(mContext));
        MirrorSearchAdapter mirrorSearchAdapter = new MirrorSearchAdapter(mContext, mSearchDataList);
        mRecyclerViewMirror.setAdapter(mirrorSearchAdapter);
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
        switch (view.getId()){

            case R.id.bt_create_mirror:
                mIMirrorFragmentCallBack.onCreateMirrorClick();

                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    public interface IMirrorFragmentCallBack {
        void onCreateMirrorClick();
    }
}
