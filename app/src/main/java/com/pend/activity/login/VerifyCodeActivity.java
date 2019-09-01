package com.pend.activity.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.activity.home.HomeActivity;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.LoginResponseModel;
import com.pend.models.VerifyCodeResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class VerifyCodeActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private final String TAG = VerifyCodeActivity.class.getSimpleName();
    private Integer mUserID;
    private String mCode;
    private String mDetails;
    private EditText mEtCode;
    private boolean mIsChecked = true;
    private TextInputLayout mInputLayoutCode;
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        mUserID = getIntent().getIntExtra("userID",134);
        mDetails=getIntent().getStringExtra("sessionID");
        initUI();
    }

    @Override
    protected void initUI() {
        mRootView = findViewById(R.id.root_view);
        mEtCode = findViewById(R.id.et_code);

        findViewById(R.id.bt_verify_code).setOnClickListener(this);

        mInputLayoutCode = findViewById(R.id.input_code);

        mEtCode.addTextChangedListener(VerifyCodeActivity.this);

    }

    @Override
    public void updateUi(final boolean status, final int actionID, final Object serviceResponse) {

        switch (actionID) {
            case IApiEvent.REQUEST_VERIFY_OTP:
                if (status) {
                    VerifyCodeResponseModel verifyCodeResponseModel = (VerifyCodeResponseModel) serviceResponse;
                    if (verifyCodeResponseModel != null && verifyCodeResponseModel.status) {
                        LoggerUtil.d(TAG, verifyCodeResponseModel.statusCode);

                        if (verifyCodeResponseModel.Data != null) {


                            if (verifyCodeResponseModel.Data.Status != null&&verifyCodeResponseModel.Data.Status.equals("Success")) {

                                SharedPrefUtils.setUserLoggedIn(VerifyCodeActivity.this, true);
                               // SharedPrefUtils.setUserId(VerifyCodeActivity.this, String.valueOf(loginResponseModel.Data.userData.userID));
                                Intent intent = new Intent(VerifyCodeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        OtherUtil.showAlertDialog(getString(R.string.invalid_code), this, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    OtherUtil.showErrorMessage(this, serviceResponse);
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

        JsonObject requestObject;
        String request;

        switch (actionID) {
            case IApiEvent.REQUEST_VERIFY_OTP:

                requestObject = RequestPostDataUtil.verifyCodeApiRegParam(mDetails, mCode,mUserID);
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<VerifyCodeResponseModel>(IWebServices.REQUEST_VERIFY_OTP, NetworkUtil.getHeaders(this),
                        request, VerifyCodeResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(VerifyCodeResponseModel response) {
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
            case R.id.bt_verify_code:

                if (isAllFieldsValid()) {
                    mCode = mEtCode.getText().toString();
                    getData(IApiEvent.REQUEST_VERIFY_OTP);
                }
                break;



            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }
    /**
     * This method will check validation for Name.
     */
    private void checkValidationForVerifyCode() {
        if (mEtCode.getText().toString().trim().length() == 0) {
            mInputLayoutCode.setError(getString(R.string.please_enter_code));
            mInputLayoutCode.setErrorEnabled(true);
            mIsChecked = false;
        }
        if (mEtCode.getText().toString().trim().length() != 6) {
            mInputLayoutCode.setError(getString(R.string.please_enter_valid_code));
            mInputLayoutCode.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mInputLayoutCode.setErrorEnabled(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (mEtCode.hasFocus()) {
            checkValidationForVerifyCode();
        }

    }

    private boolean isAllFieldsValid() {
        mIsChecked = true;
        checkValidationForVerifyCode();

        return mIsChecked;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


}
