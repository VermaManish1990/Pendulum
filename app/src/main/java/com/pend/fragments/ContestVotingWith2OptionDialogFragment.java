package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IContestVotingCallBack;
import com.pend.interfaces.IWebServices;
import com.pend.models.ContestVoteResponseModel;
import com.pend.models.GetContestsResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.ui.IScreen;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;


public class ContestVotingWith2OptionDialogFragment extends DialogFragment implements View.OnClickListener , IScreen{

    private static final String TAG = ContestVotingWith3OptionDialogFragment.class.getSimpleName();
    private static final String ARG_CONTEST_DETAILS = "ARG_CONTEST_DETAILS";
    private static final String ARG_IS_VOTED = "ARG_IS_VOTED";

    private View mUnVoteView;
    private RadioButton mRbOption1;
    private RadioButton mRbOption2;
    private BaseActivity mContext;
    private RadioGroup mRgVote;
    private boolean mIsOption1;
    private boolean mIsOption2;
    private GetContestsResponseModel.GetContestDetails mContestDetails;
    private IContestVotingCallBack mIContestVotingCallBack;
    private boolean mIsVoted;


    public static ContestVotingWith2OptionDialogFragment newInstance(boolean isVoted,GetContestsResponseModel.GetContestDetails contestDetails) {
        ContestVotingWith2OptionDialogFragment fragment = new ContestVotingWith2OptionDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_VOTED, isVoted);
        args.putSerializable(ARG_CONTEST_DETAILS, contestDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mIsVoted = getArguments().getBoolean(ARG_IS_VOTED,false);
            mContestDetails = (GetContestsResponseModel.GetContestDetails) getArguments().getSerializable(ARG_CONTEST_DETAILS);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (BaseActivity) context;
//        mIContestVotingCallBack = (IContestVotingCallBack) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contest_voting_with_2_option_dialog, container, false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        initUI(view);
        setInitialData();
        return view;
    }

    private void setInitialData() {

        if(mIsVoted){
            mUnVoteView.setVisibility(View.VISIBLE);
        }else {
            mUnVoteView.setVisibility(View.GONE);
        }

        if(mContestDetails!=null){
            mRbOption1.setText(mContestDetails.option1MirrorName != null ? mContestDetails.option1MirrorName : "");
            mRbOption2.setText(mContestDetails.option2MirrorName != null ? mContestDetails.option2MirrorName : "");
        }
    }

    private void initUI(View view) {

        mUnVoteView = view.findViewById(R.id.un_vote_view);
        mRgVote = view.findViewById(R.id.rg_vote);
        mRbOption1 = view.findViewById(R.id.rb_option1);
        mRbOption2 = view.findViewById(R.id.rb_option2);

        view.findViewById(R.id.bt_okay).setOnClickListener(this);
        view.findViewById(R.id.bt_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_un_vote).setOnClickListener(this);

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_CONTESTS_VOTE_CODE:
                if (status) {
                    ContestVoteResponseModel contestVoteResponseModel = (ContestVoteResponseModel) serviceResponse;
                    if (contestVoteResponseModel != null && contestVoteResponseModel.status) {
                        LoggerUtil.d(TAG, contestVoteResponseModel.statusCode);

                        if (contestVoteResponseModel.Data != null && contestVoteResponseModel.Data.voteData != null) {
//                            mIContestVotingCallBack.onVotingOrUnVotingClick();
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                this.dismiss();
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
//            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
                return;
            }
            mContext.showProgressDialog();
        }else {
            return;
        }

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_CONTESTS_VOTE_CODE:

                JsonObject requestObject = RequestPostDataUtil.contestVoteApiRegParam(userId, mContestDetails.contestID, mContestDetails.contestTypeID,
                        mIsOption1, mIsOption2,false);
                String request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<ContestVoteResponseModel>(IWebServices.REQUEST_CONTESTS_VOTE_URL,
                        NetworkUtil.getHeaders(mContext), request, ContestVoteResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(ContestVoteResponseModel response) {
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
        switch (view.getId()) {
            case R.id.bt_okay:
                if (!(mRgVote.getCheckedRadioButtonId() == -1)) {
                    switch (mRgVote.getCheckedRadioButtonId()) {
                        case R.id.rb_option1:
                            mIsOption1 = true;
                            mIsOption2 = false;
                            break;

                        case R.id.rb_option2:
                            mIsOption1 = false;
                            mIsOption2 = true;
                            break;

                        case R.id.rb_option3:
                            mIsOption1 = false;
                            mIsOption2 = false;
                            break;
                    }
                    getData(IApiEvent.REQUEST_CONTESTS_VOTE_CODE);
                }
                break;

            case R.id.bt_cancel:
                this.dismiss();
                break;

            case R.id.tv_un_vote:
                mIsOption1 = false;
                mIsOption2 = false;
                getData(IApiEvent.REQUEST_CONTESTS_VOTE_CODE);
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
