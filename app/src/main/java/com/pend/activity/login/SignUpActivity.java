package com.pend.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ToastUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class SignUpActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private TextInputLayout mInputLayoutName;
    private TextInputLayout mInputLayoutEmail;
    private TextInputLayout mInputLayoutMobileNumber;
    private TextInputLayout mInputLayoutPassword;
    private TextInputLayout mInputLayoutConfirmPassword;
    private EditText mEtName;
    private EditText mEtMobileNumber;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtConfirmPassword;
    private boolean mIsChecked = true;
    private String mPassword;
    private String mPhoneNumber;
    private String mUserEmail;
    private String mFullName;
    private TextView mTvSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();
    }

    @Override
    protected void initUI() {

        mEtName = findViewById(R.id.et_name);
        mEtMobileNumber = findViewById(R.id.et_mobile_number);
        mEtEmail = findViewById(R.id.et_email);
        mEtPassword = findViewById(R.id.et_password);
        mEtConfirmPassword = findViewById(R.id.et_confirm_password);

        mTvSignIn = findViewById(R.id.tv_sign_in);
        mTvSignIn.setOnClickListener(this);
        findViewById(R.id.bt_sign_up).setOnClickListener(this);

        mInputLayoutName = findViewById(R.id.input_layout_name);
        mInputLayoutEmail = findViewById(R.id.input_layout_email);
        mInputLayoutMobileNumber = findViewById(R.id.input_layout_mobile_number);
        mInputLayoutPassword = findViewById(R.id.input_layout_password);
        mInputLayoutConfirmPassword = findViewById(R.id.input_layout_confirm_password);
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_SIGN_UP_CODE:
                if (status) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
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
        if (!NetworkUtil.isInternetConnected(this)) {
//            showSnake(getString(R.string.network_connection));
            return;
        }
//        showProgressDialog(getResources().getString(R.string.pleaseWait), false);

        switch (actionID) {
            case IApiEvent.REQUEST_SIGN_UP_CODE:

                JsonObject requestObject = RequestPostDataUtil.signUpApiRegParam(mFullName, mUserEmail, mPhoneNumber, mPassword);
                String request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<BaseResponseModel>(IWebServices.REQUEST_SIGN_UP_URL, NetworkUtil.getHeaders(this),
                        request, BaseResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(BaseResponseModel response) {
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
            case R.id.bt_sign_up:

                if (isAllFieldsValid()) {

                    if (isPasswordMatch()) {
                        getData(IApiEvent.REQUEST_SIGN_UP_CODE);
                    } else {
                        ToastUtils.showToast(SignUpActivity.this, getString(R.string.password_not_match));
                    }
                }
                break;

            case R.id.tv_sign_in:

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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

        if (mEtName.hasFocus()) {
            checkValidationForName();
        } else if (mEtMobileNumber.hasFocus()) {
            checkValidationForMobileNumber();
        } else if (mEtEmail.hasFocus()) {
            checkValidationForEmail();
        } else if (mEtPassword.hasFocus()) {
            checkValidationForPassword();
        } else if (mEtConfirmPassword.hasFocus()) {
            checkValidationForConfirmPassword();
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
        checkValidationForName();
        checkValidationForMobileNumber();
        checkValidationForEmail();
        checkValidationForPassword();
        checkValidationForConfirmPassword();
        return mIsChecked;
    }

    /**
     * This method will check validation for Name.
     */
    private void checkValidationForName() {
        if (mEtName.getText().toString().trim().length() == 0) {
            mInputLayoutName.setError(Constants.NAME_VALIDATION_ERROR);
            mInputLayoutName.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mFullName = mEtName.getText().toString().trim();
            mInputLayoutName.setErrorEnabled(false);
        }
    }

    /**
     * This method will check validation for Mobile Number.
     */
    private void checkValidationForMobileNumber() {
        checkValidationForName();
        if (mEtMobileNumber.getText().toString().trim().length() == 0) {
            mInputLayoutMobileNumber.setError(Constants.MOBILE_NUMBER_VALIDATION_ERROR);
            mInputLayoutMobileNumber.setErrorEnabled(true);
            mIsChecked = false;
        } else if (mEtMobileNumber.getText().toString().trim().length() < 10) {
            mInputLayoutMobileNumber.setError(Constants.NOT_VALID_MOBILE_NUMBER_ERROR);
            mInputLayoutMobileNumber.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mPhoneNumber = mEtMobileNumber.getText().toString().trim();
            mInputLayoutMobileNumber.setErrorEnabled(false);
        }
    }

    /**
     * This method will check validation for Email Id.
     */
    private void checkValidationForEmail() {
        checkValidationForMobileNumber();
        if (mEtEmail.getText().toString().trim().length() == 0) {
            mInputLayoutEmail.setError(Constants.EMAIL_ID_VALIDATION_ERROR);
            mInputLayoutEmail.setErrorEnabled(true);
            mIsChecked = false;
        }/* else if (!mEtEmail.getText().toString().trim().matches(Constants.EMAIL_PATTERN)) {
            mInputLayoutEmail.setError(Constants.NOT_VALID_EMAIL_ID_ERROR);
            mInputLayoutEmail.setErrorEnabled(true);
            mIsChecked = false;
        }*/ else {
            mUserEmail = mEtEmail.getText().toString().trim();
            mInputLayoutEmail.setErrorEnabled(false);
        }
    }

    /**
     * This method will check validation for Name.
     */
    private void checkValidationForPassword() {
        if (mEtPassword.getText().toString().trim().length() == 0) {
            mInputLayoutPassword.setError(Constants.PASSWORD_VALIDATION_ERROR);
            mInputLayoutPassword.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mPassword = mEtPassword.getText().toString().trim();
            mInputLayoutPassword.setErrorEnabled(false);
        }
    }

    /**
     * This method will check validation for Name.
     */
    private void checkValidationForConfirmPassword() {
        if (mEtPassword.getText().toString().trim().length() == 0) {
            mInputLayoutPassword.setError(Constants.PASSWORD_VALIDATION_ERROR);
            mInputLayoutPassword.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mInputLayoutPassword.setErrorEnabled(false);
        }
    }

    /**
     * Method is used to know both password is match or not.
     *
     * @return true if password and ConfirmPassword match , otherwise false.
     */
    private boolean isPasswordMatch() {
        return mEtPassword.getText().toString().trim().equals(mEtConfirmPassword.getText().toString().trim());
    }

}
