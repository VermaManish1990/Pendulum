package com.pend.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.LoginResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private final String TAG = LoginActivity.class.getSimpleName();
    private String mUserName ;
    private String mPassword ;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private boolean mIsChecked = true;
    private TextInputLayout mInputLayoutEmail;
    private TextInputLayout mInputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
    }

    @Override
    protected void initUI() {
        mEtEmail = findViewById(R.id.et_email);
        mEtPassword = findViewById(R.id.et_password);
        findViewById(R.id.bt_sign_in).setOnClickListener(this);
        findViewById(R.id.tv_forgot_password).setOnClickListener(this);
        findViewById(R.id.bt_sign_up).setOnClickListener(this);

        mInputLayoutEmail = findViewById(R.id.input_layout_email);
        mInputLayoutPassword = findViewById(R.id.input_layout_password);
    }

    @Override
    public void updateUi(final boolean status, final int actionID, final Object serviceResponse) {

        switch (actionID) {
            case IApiEvent.REQUEST_LOGIN_CODE:
                if (status) {
                    LoginResponseModel loginResponseModel = (LoginResponseModel) serviceResponse;
                    if (loginResponseModel != null && loginResponseModel.status ) {
                        LoggerUtil.d(TAG, loginResponseModel.statusCode);

                        SharedPrefUtils.setUserLoggedIn(LoginActivity.this,true);
                        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                        startActivity(intent);
                        finish();

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
    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(final int actionID) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
//            showSnake(getString(R.string.network_connection));
            return;
        }
//        showProgressDialog(getResources().getString(R.string.pleaseWait), false);

        switch (actionID) {
            case IApiEvent.REQUEST_LOGIN_CODE:

                JsonObject requestObject = RequestPostDataUtil.loginApiRegParam(mUserName, mPassword);
                String request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<LoginResponseModel>(IWebServices.REQUEST_LOGIN_URL, NetworkUtil.getHeaders(this),
                        request, LoginResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(LoginResponseModel response) {
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
            case R.id.bt_sign_in:

                if(isAllFieldsValid()){
                    getData(IApiEvent.REQUEST_LOGIN_CODE);
                }
                break;

            case R.id.bt_sign_up:

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.tv_forgot_password:
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (mEtEmail.hasFocus()) {
            checkValidationForEmail();
        } else if (mEtPassword.hasFocus()) {
            checkValidationForPassword();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * Method is used to check validation for all fields.
     *
     * @return true if all validation is correct , otherwise false.
     */
    private boolean isAllFieldsValid() {
        mIsChecked = true;
        checkValidationForEmail();
        checkValidationForPassword();
        return mIsChecked;
    }

    /**
     * This method will check validation for Email Id.
     */
    void checkValidationForEmail() {
        if (mEtEmail.getText().toString().trim().length() == 0) {
            mInputLayoutEmail.setError(Constants.EMAIL_ID_VALIDATION_ERROR);
            mInputLayoutEmail.setErrorEnabled(true);
            mIsChecked = false;
        }/* else if (!mEtEmail.getText().toString().trim().matches(Constants.EMAIL_PATTERN)) {
            mInputLayoutEmail.setError(Constants.NOT_VALID_EMAIL_ID_ERROR);
            mInputLayoutEmail.setErrorEnabled(true);
            mIsChecked = false;
        }*/ else {
            mUserName = mEtEmail.getText().toString();
            mInputLayoutEmail.setErrorEnabled(false);
        }
    }

    /**
     * This method will check validation for Name.
     */
    void checkValidationForPassword() {
        if (mEtPassword.getText().toString().trim().length() == 0) {
            mInputLayoutPassword.setError(Constants.PASSWORD_VALIDATION_ERROR);
            mInputLayoutPassword.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mPassword = mEtPassword.getText().toString();
            mInputLayoutPassword.setErrorEnabled(false);
        }
    }
}