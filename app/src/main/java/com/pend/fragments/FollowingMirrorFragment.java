package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.pend.BaseActivity;
import com.pend.BaseFragment;
import com.pend.R;
import com.pend.adapters.FollowingMirrorAdapter;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetFollowingMirrorResponseModel;
import com.pend.models.GetTrendingAndIntroducedMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.util.ArrayList;


public class FollowingMirrorFragment extends BaseFragment {

    private Context mContext;
    private View mRootView;
    private GridView mGridViewFollowingMirror;
    private final String TAG = FollowingMirrorFragment.class.getSimpleName();
    private int mPageNumber;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FollowingMirrorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowingMirrorFragment newInstance() {
        FollowingMirrorFragment fragment = new FollowingMirrorFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//        }
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
        View view = inflater.inflate(R.layout.fragment_following_mirror, container, false);

        initUI(view);
        setInitialData();

        getData(IApiEvent.REQUEST_GET_FOLLOWING_CODE);
        return view;
    }

    @Override
    protected void initUI(View view) {
        mRootView = view.findViewById(R.id.root_view);
        mGridViewFollowingMirror = view.findViewById(R.id.grid_view_following_mirror);
    }

    @Override
    protected void setInitialData() {
        ArrayList<GetFollowingMirrorResponseModel.GetFollowingMirrorDetails> mirrorList = new ArrayList<>();
        mGridViewFollowingMirror.setAdapter(new FollowingMirrorAdapter(mContext, mirrorList));

        /*
          On Click event for Single Gridview Item
          */
        mGridViewFollowingMirror.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            }
        });

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_GET_FOLLOWING_CODE:
                if (status) {
                    GetFollowingMirrorResponseModel followingMirrorResponseModel = (GetFollowingMirrorResponseModel) serviceResponse;
                    if (followingMirrorResponseModel != null && followingMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, followingMirrorResponseModel.statusCode);

                        FollowingMirrorAdapter followingMirrorAdapter = (FollowingMirrorAdapter) mGridViewFollowingMirror.getAdapter();
                        ArrayList<GetFollowingMirrorResponseModel.GetFollowingMirrorDetails> mirrorList = followingMirrorAdapter.getMirrorList();
                        mirrorList.addAll(followingMirrorResponseModel.Data.mirrorList);
                        followingMirrorAdapter.setMirrorList(mirrorList);
                        followingMirrorAdapter.notifyDataSetChanged();

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
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }

        ((BaseActivity) mContext).showProgressDialog();

        switch (actionID) {
            case IApiEvent.REQUEST_GET_FOLLOWING_CODE:

                mPageNumber = 1;
                String followingMirrorUrl = IWebServices.REQUEST_GET_FOLLOWING_URL + Constants.PARAM_USER_ID + "=" + SharedPrefUtils.getUserId(mContext)
                        + "&" + Constants.PARAM_PAGE_NUMBER + "=" + String.valueOf(mPageNumber);
//                        + "&" + Constants.PARAM_SEARCH_TEXT + "=" + String.valueOf("search text");
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
}
