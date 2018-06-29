package com.pend.fragments;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioGroup;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.MirrorVoteResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.ui.IScreen;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class MirrorUnVotingDialogFragment extends DialogFragment implements IScreen, View.OnClickListener {

    private static final String TAG = MirrorUnVotingDialogFragment.class.getSimpleName();
    private static final String ARG_MIRROR_ID = "ARG_MIRROR_ID";
    private RadioGroup mRgVote;
    private Context mContext;
    private int mMirrorId;
    private boolean mIsAdmire;
    private boolean mIsHate;
    private boolean mIsCanTSay;

    public static MirrorUnVotingDialogFragment newInstance(int mirrorId) {
        MirrorUnVotingDialogFragment fragment = new MirrorUnVotingDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MIRROR_ID, mirrorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMirrorId = getArguments().getInt(ARG_MIRROR_ID,0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_unvote_popup, container, false);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        initUI(view);
        setInitialData();
        return view;
    }

    private void initUI(View view) {

        mRgVote = view.findViewById(R.id.rg_vote);
        view.findViewById(R.id.bt_okay).setOnClickListener(this);
        view.findViewById(R.id.bt_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_un_vote).setOnClickListener(this);
    }

    private void setInitialData() {

        mIsAdmire = false;
        mIsHate = false;
        mIsCanTSay = false;
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_MIRROR_VOTE_CODE:
                if (status) {
                    MirrorVoteResponseModel mirrorVoteResponseModel = (MirrorVoteResponseModel) serviceResponse;
                    if (mirrorVoteResponseModel != null && mirrorVoteResponseModel.status) {
                        LoggerUtil.d(TAG, mirrorVoteResponseModel.statusCode);

                        if (mirrorVoteResponseModel.Data != null && mirrorVoteResponseModel.Data.voteData != null) {
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
            case IApiEvent.REQUEST_MIRROR_VOTE_CODE:

                JsonObject requestObject = RequestPostDataUtil.mirrorVoteApiRegParam(userId, mMirrorId, mIsAdmire, mIsHate, mIsCanTSay);
                String request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<MirrorVoteResponseModel>(IWebServices.REQUEST_MIRROR_VOTE_URL,
                        NetworkUtil.getHeaders(mContext), request, MirrorVoteResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(MirrorVoteResponseModel response) {
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
                    getData(IApiEvent.REQUEST_MIRROR_VOTE_CODE);
                }
                break;

            case R.id.bt_cancel:
                this.dismiss();
                break;

            case R.id.tv_un_vote:
                mIsAdmire = false;
                mIsHate = false;
                mIsCanTSay = false;
                getData(IApiEvent.REQUEST_MIRROR_VOTE_CODE);
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
}
