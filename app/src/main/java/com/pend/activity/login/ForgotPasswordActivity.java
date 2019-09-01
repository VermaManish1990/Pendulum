package com.pend.activity.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
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

public class ForgotPasswordActivity  extends BaseActivity implements View.OnClickListener, TextWatcher {
    private final String TAG = VerifyCodeActivity.class.getSimpleName();
    private String mPassword;
    private String mPhone;
    private EditText mEtPassword,mEtConfirmPassword;
    private TextInputLayout mInputPassword,mInputConfirmPassword;
    private View mRootView;
    boolean mIsChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mPhone=getIntent().getStringExtra("phone");
        initUI();
    }

    @Override
    protected void initUI() {
        mRootView = findViewById(R.id.root_view);
        mEtPassword = findViewById(R.id.et_password);
        mEtConfirmPassword = findViewById(R.id.et_confirm_password);

        findViewById(R.id.bt_set_password).setOnClickListener(this);

        mInputConfirmPassword = findViewById(R.id.input_confirm_password);
        mInputPassword = findViewById(R.id.input_password);

        mEtPassword.addTextChangedListener(ForgotPasswordActivity.this);
        mEtConfirmPassword.addTextChangedListener(ForgotPasswordActivity.this);

    }

    @Override
    public void updateUi(final boolean status, final int actionID, final Object serviceResponse) {

        switch (actionID) {
            case IApiEvent.REQUEST_FORGOT_PASSWORD:
                if (status) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

                        if (baseResponseModel.status == true) {

                               Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

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
            case IApiEvent.REQUEST_FORGOT_PASSWORD:

                requestObject = RequestPostDataUtil.forgotPasswordApiRegParam(mPhone, mPassword);
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<VerifyCodeResponseModel>(IWebServices.REQUEST_FORGOT_PASSWORD, NetworkUtil.getHeaders(this),
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
            case R.id.bt_set_password:

                if (isAllFieldsValid()) {
                    mPassword = mEtPassword.getText().toString();
                    getData(IApiEvent.REQUEST_FORGOT_PASSWORD);
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
    private void checkValidationForPassword() {
        if (mEtPassword.getText().toString().trim().length() == 0) {
            mInputPassword.setError(getString(R.string.please_enter_password));
            mInputPassword.setErrorEnabled(true);
            mIsChecked = false;
        }
        if (mEtPassword.getText().toString().trim().length() < 4) {
            mInputPassword.setError(getString(R.string.password_small));
            mInputPassword.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mInputPassword.setErrorEnabled(false);
        }
    }

    /**
     * This method will check validation for Name.
     */
    private void checkValidationForConfirmPassword() {
        if (mEtConfirmPassword.getText().toString().trim().length() == 0) {
            mInputConfirmPassword.setError(getString(R.string.please_enter_confirm_password));
            mInputConfirmPassword.setErrorEnabled(true);
            mIsChecked = false;
        }
        if (!mEtConfirmPassword.getText().toString().equals(mEtPassword.getText().toString())) {
            mInputConfirmPassword.setError(getString(R.string.password_not_match));
            mInputConfirmPassword.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mInputConfirmPassword.setErrorEnabled(false);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (mEtPassword.hasFocus()) {
            checkValidationForPassword();
        }
        if (mEtConfirmPassword.hasFocus()) {
            checkValidationForConfirmPassword();
        }

    }

    private boolean isAllFieldsValid() {
        mIsChecked = true;
        checkValidationForPassword();
        checkValidationForConfirmPassword();

        return mIsChecked;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


}
