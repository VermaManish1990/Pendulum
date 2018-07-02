package com.pend.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IExitPollVotingDialogCallBack;
import com.pend.interfaces.IMirrorVotingDialogCallBack;
import com.pend.interfaces.IWebServices;
import com.pend.models.ExitPollVoteResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.ui.IScreen;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class ExitPollVotingDialogFragment extends DialogFragment implements IScreen,View.OnClickListener {

    private static final String TAG = ExitPollVotingDialogFragment.class.getSimpleName();
    private static final String ARG_EXIT_POLL_ID = "ARG_EXIT_POLL_ID";
    private static final String ARG_MIRROR_ID = "ARG_MIRROR_ID";
    private IExitPollVotingDialogCallBack mIExitPollVotingDialogCallBack;
    private RadioGroup mRgVote;
    private Context mContext;
    private int mMirrorId;
    private int mExitPollId;
    private boolean mIsAdmire;
    private boolean mIsHate;
    private boolean mIsCanTSay;

    public static ExitPollVotingDialogFragment newInstance(int mirrorId,int exitPollId) {
        ExitPollVotingDialogFragment fragment = new ExitPollVotingDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MIRROR_ID, mirrorId);
        args.putInt(ARG_EXIT_POLL_ID, exitPollId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMirrorId = getArguments().getInt(ARG_MIRROR_ID, 0);
            mExitPollId = getArguments().getInt(ARG_EXIT_POLL_ID, 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mIExitPollVotingDialogCallBack = (IExitPollVotingDialogCallBack) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_voting_popup, container, false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        initUI(view);
        setInitialData();
        return view;
    }

    private void initUI(View view) {

        mRgVote = view.findViewById(R.id.rg_vote);
        ((TextView)view.findViewById(R.id.tv_what_you_feel_text)).setText(getString(R.string.what_you_feel_about_exit_poll));
        view.findViewById(R.id.bt_okay).setOnClickListener(this);
        view.findViewById(R.id.bt_cancel).setOnClickListener(this);
    }

    private void setInitialData() {

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_EXIT_POLL_VOTE_CODE:
                if (status) {
                    ExitPollVoteResponseModel exitPollVoteResponseModel = (ExitPollVoteResponseModel) serviceResponse;
                    if (exitPollVoteResponseModel != null && exitPollVoteResponseModel.status) {
                        LoggerUtil.d(TAG, exitPollVoteResponseModel.statusCode);

                        if (exitPollVoteResponseModel.Data != null && exitPollVoteResponseModel.Data.voteData != null) {
                            mIExitPollVotingDialogCallBack.onVotingOrUnVotingClick(exitPollVoteResponseModel.Data.voteData);
                            this.dismiss();
                        }
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
//            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }

        ((BaseActivity) mContext).showProgressDialog();

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(mContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_EXIT_POLL_VOTE_CODE:

                JsonObject requestObject = RequestPostDataUtil.exitPollVoteApiRegParam(userId, mMirrorId,mExitPollId, mIsAdmire, mIsHate, mIsCanTSay);
                String request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<ExitPollVoteResponseModel>(IWebServices.REQUEST_EXIT_POLL_VOTE_URL,
                        NetworkUtil.getHeaders(mContext), request, ExitPollVoteResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(ExitPollVoteResponseModel response) {
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
                        case R.id.rb_admire:
                            mIsAdmire = true;
                            mIsHate = false;
                            mIsCanTSay = false;
                            break;

                        case R.id.rb_hate:
                            mIsAdmire = false;
                            mIsHate = true;
                            mIsCanTSay = false;
                            break;

                        case R.id.rb_can_t_say:
                            mIsAdmire = false;
                            mIsHate = false;
                            mIsCanTSay = true;
                            break;
                    }
                    getData(IApiEvent.REQUEST_EXIT_POLL_VOTE_CODE);
                }
                break;

            case R.id.bt_cancel:
                this.dismiss();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
