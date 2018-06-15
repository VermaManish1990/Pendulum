package com.pend.activity.mirror;

import android.support.v4.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pend.BaseActivity;
import com.pend.R;
import com.pend.fragments.ContestSearchFragment;
import com.pend.fragments.CreateMirrorDialogFragment;
import com.pend.fragments.MirrorSearchFragment;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.SearchMirrorResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class SearchInNewsFeedActivity extends BaseActivity implements View.OnClickListener, TextWatcher, MirrorSearchFragment.IMirrorFragmentCallBack {

    private static final String TAG = SearchInNewsFeedActivity.class.getSimpleName();
    private View mRootView;
    private Button mBtMirror;
    private Button mBtContest;
    private EditText mEtSearch;
    private String mSearchText;
    private boolean mIsMirror = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_in_news_feed);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mBtMirror = findViewById(R.id.bt_mirror);
        mBtContest = findViewById(R.id.bt_contest);
        View customSearchView = findViewById(R.id.custom_search_view);
        mEtSearch = customSearchView.findViewById(R.id.et_search);

        mBtMirror.setOnClickListener(this);
        mBtContest.setOnClickListener(this);
        mEtSearch.addTextChangedListener(this);
    }

    @Override
    protected void setInitialData() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBtMirror.setBackground(getDrawable(R.drawable.custom_blue_button));
        }
        MirrorSearchFragment mirrorSearchFragment = new MirrorSearchFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container, mirrorSearchFragment);
        transaction.commit();
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_SEARCH_MIRROR_CODE:
                if (status) {
                    SearchMirrorResponseModel searchMirrorResponseModel = (SearchMirrorResponseModel) serviceResponse;
                    if (searchMirrorResponseModel != null && searchMirrorResponseModel.status) {
                        LoggerUtil.d(TAG, searchMirrorResponseModel.statusCode);

                        if (searchMirrorResponseModel.Data != null && searchMirrorResponseModel.Data.searchData != null) {

                            //TODO Update Fragment
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
        removeProgressDialog();
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }

        showProgressDialog();

        switch (actionID) {
            case IApiEvent.REQUEST_SEARCH_MIRROR_CODE:

                String searchMirrorUrl = IWebServices.REQUEST_SEARCH_MIRROR_URL + Constants.PARAM_SEARCH_TEXT + "=" + mSearchText;

                RequestManager.addRequest(new GsonObjectRequest<SearchMirrorResponseModel>(searchMirrorUrl,
                        NetworkUtil.getHeaders(this), null, SearchMirrorResponseModel.class,
                        new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(SearchMirrorResponseModel response) {
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (mEtSearch.hasFocus()) {
            mSearchText = mEtSearch.getText().toString().trim();
            getData(IApiEvent.REQUEST_SEARCH_MIRROR_CODE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_mirror:

                mIsMirror = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBtMirror.setBackground(getDrawable(R.drawable.custom_blue_button));
                    mBtContest.setBackground(getDrawable(R.drawable.custom_blue_border));
                }

                MirrorSearchFragment mirrorSearchFragment = new MirrorSearchFragment();
                FragmentTransaction transactionMirror = getSupportFragmentManager().beginTransaction();
                transactionMirror.replace(R.id.fl_container, mirrorSearchFragment);
                transactionMirror.commit();
                break;

            case R.id.bt_contest:

                mIsMirror = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBtContest.setBackground(getDrawable(R.drawable.custom_blue_button));
                    mBtMirror.setBackground(getDrawable(R.drawable.custom_blue_border));
                }

                ContestSearchFragment contestSearchFragment = new ContestSearchFragment();
                FragmentTransaction transactionContest = getSupportFragmentManager().beginTransaction();
                transactionContest.replace(R.id.fl_container, contestSearchFragment);
                transactionContest.commit();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    @Override
    public void onCreateMirrorClick() {
        DialogFragment createMirrorDialogFragment = new CreateMirrorDialogFragment();
        createMirrorDialogFragment.show(getSupportFragmentManager(), "CreateMirrorDialogFragment");
    }
}
