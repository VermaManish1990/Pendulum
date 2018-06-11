package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseFragment;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.adapters.TrendingAndIntroducedMirrorAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetTrendingAndIntroducedMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;


public class IntroducedMirrorFragment extends BaseFragment {

    private final String TAG = IntroducedMirrorFragment.class.getSimpleName();
    private Context mContext;
    private RecyclerView mRecyclerViewIntroduced;
    private View mRootView;
    private int mPageNumber;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IntroducedMirrorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntroducedMirrorFragment newInstance() {
        IntroducedMirrorFragment fragment = new IntroducedMirrorFragment();
        Bundle args = new Bundle();
//        args.putSerializable(ARG_MIRROR_LIST, mirrorList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mMirrorList = (ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails>) getArguments().getSerializable(ARG_MIRROR_LIST);
        }else {
            mMirrorList = new ArrayList<>();
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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
        mRecyclerViewIntroduced = view.findViewById(R.id.recycler_view_introduced);
    }

    @Override
    protected void setInitialData() {

        ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mirrorList = new ArrayList<>();
        mRecyclerViewIntroduced.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewIntroduced.setAdapter(new TrendingAndIntroducedMirrorAdapter(mContext, mirrorList));
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_INTRODUCED_CODE:
                if (status) {
                    GetTrendingAndIntroducedMirrorResponseModel getTrendingAndIntroducedMirrorResponseModel =
                            (GetTrendingAndIntroducedMirrorResponseModel) serviceResponse;
                    if (getTrendingAndIntroducedMirrorResponseModel != null && getTrendingAndIntroducedMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, getTrendingAndIntroducedMirrorResponseModel.statusCode);

                        TrendingAndIntroducedMirrorAdapter trendingAndIntroducedMirrorAdapter = (TrendingAndIntroducedMirrorAdapter) mRecyclerViewIntroduced.getAdapter();
                        ArrayList<GetTrendingAndIntroducedMirrorResponseModel.GetTrendingAndIntroducedMirrorDetails> mirrorList =
                                trendingAndIntroducedMirrorAdapter.getMirrorList();

                        mirrorList.addAll(getTrendingAndIntroducedMirrorResponseModel.Data.mirrorList);
                        trendingAndIntroducedMirrorAdapter.setMirrorList(mirrorList);
                        trendingAndIntroducedMirrorAdapter.notifyDataSetChanged();

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
        ((BaseActivity) mContext).removeProgressDialog();
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {
        if (!ConnectivityUtils.isNetworkEnabled(mContext)) {
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            return;
        }

        ((BaseActivity) mContext).showProgressDialog();

        switch (actionID) {
            case IApiEvent.REQUEST_GET_INTRODUCED_CODE:

                //TODO Search text

                mPageNumber = 1;
                String introducedMirrorUrl = IWebServices.REQUEST_GET_INTRODUCED_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(mContext)
                        + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
//                        + "&" + Constants.PARAM_SEARCH_TEXT + "=" + String.valueOf("search text");
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
}
