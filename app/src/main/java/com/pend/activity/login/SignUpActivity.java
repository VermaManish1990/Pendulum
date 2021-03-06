package com.pend.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.pend.models.SendOTPResponseModel;
import com.pend.models.SignupResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
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
    private Integer mUserID;
    private String mUserEmail;
    private String mFullName;
    private View mRootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mEtName = findViewById(R.id.et_name);
        mEtMobileNumber = findViewById(R.id.et_mobile_number);
   //     mEtEmail = findViewById(R.id.et_email);
        mEtPassword = findViewById(R.id.et_password);
        mEtConfirmPassword = findViewById(R.id.et_confirm_password);

        mInputLayoutName = findViewById(R.id.input_layout_name);
        mInputLayoutEmail = findViewById(R.id.input_layout_email);
        mInputLayoutMobileNumber = findViewById(R.id.input_layout_mobile_number);
        mInputLayoutPassword = findViewById(R.id.input_layout_password);
        mInputLayoutConfirmPassword = findViewById(R.id.input_layout_confirm_password);

        mEtName.addTextChangedListener(SignUpActivity.this);
       mEtMobileNumber.addTextChangedListener(SignUpActivity.this);
 //       mEtEmail.addTextChangedListener(SignUpActivity.this);
        mEtPassword.addTextChangedListener(SignUpActivity.this);
        mEtConfirmPassword.addTextChangedListener(SignUpActivity.this);


        findViewById(R.id.bt_sign_up).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        OtherUtil.setSpannableClick(SignUpActivity.this, (TextView) findViewById(R.id.tv_sign_in), 25, 32, 1.0f, R.color.colorBlue, new OtherUtil.IClickAbleSpanCallback() {
            @Override
            public void onSpanClick() {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_SIGN_UP_CODE:
                if (status) {
                    SignupResponseModel baseResponseModel = (SignupResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);


                        mPhoneNumber = baseResponseModel.Data.user.userPhone;
                        mUserID = baseResponseModel.Data.user.userID;
                      //  Intent intent = new Intent(SignUpActivity.this, VerifyCodeActivity.class);
                       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       // startActivity(intent);
                       // finish();
                        getData(IApiEvent.REQUEST_SEND_OTP);

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this,serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));


                }
                break;
            case IApiEvent.REQUEST_SEND_OTP:
                if (status) {
                    SendOTPResponseModel baseResponseModel = (SendOTPResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

                        if(baseResponseModel.Data!=null&&baseResponseModel.Data.Status.equalsIgnoreCase("success")) {
                            Intent intent = new Intent(SignUpActivity.this, VerifyCodeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("userID", mUserID);
                            intent.putExtra("sessionID", baseResponseModel.Data.Details);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this,serviceResponse);
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
        if (!NetworkUtil.isInternetConnected(this)) {
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }
        showProgressDialog();
        switch (actionID) {
            case IApiEvent.REQUEST_SIGN_UP_CODE:

                JsonObject requestObject = RequestPostDataUtil.signUpApiRegParam(mFullName, mPhoneNumber, mPassword);
                String request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<SignupResponseModel>(IWebServices.REQUEST_SIGN_UP_URL, NetworkUtil.getHeaders(this),
                        request, SignupResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(SignupResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_SEND_OTP:

                 requestObject = RequestPostDataUtil.sendOtpApiRegParam( mPhoneNumber, mUserID);
                 request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<SendOTPResponseModel>(IWebServices.REQUEST_SEND_OTP, NetworkUtil.getHeaders(this),
                        request, SendOTPResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(SendOTPResponseModel response) {
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
        } /*else if (mEtEmail.hasFocus()) {
            checkValidationForEmail();
        }*/ else if (mEtPassword.hasFocus()) {
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
//        checkValidationForEmail();
        checkValidationForPassword();
        checkValidationForConfirmPassword();
        return mIsChecked;
    }

    /**
     * This method will check validation for Name.
     */
    private void checkValidationForName() {
        if (mEtName.getText().toString().trim().length() == 0) {
            mInputLayoutName.setError(getString(R.string.please_enter_name));
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
        if (mEtMobileNumber.getText().toString().trim().length() == 0) {
           mInputLayoutMobileNumber.setError(getString(R.string.please_enter_phone_number));
           mInputLayoutMobileNumber.setErrorEnabled(true);
            mIsChecked = false;
       } else if (mEtMobileNumber.getText().toString().trim().length() < 10) {
            mInputLayoutMobileNumber.setError(getString(R.string.please_enter_valid_phone_number));
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
        if (mEtEmail.getText().toString().trim().length() == 0) {
            mInputLayoutEmail.setError(getString(R.string.please_enter_mail_id));
            mInputLayoutEmail.setErrorEnabled(true);
            mIsChecked = false;
        } else if (!mEtEmail.getText().toString().trim().matches(Constants.EMAIL_PATTERN)) {
            mInputLayoutEmail.setError(getString(R.string.please_enter_valid_mail_id));
            mInputLayoutEmail.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mUserEmail = mEtEmail.getText().toString().trim();
            mInputLayoutEmail.setErrorEnabled(false);
        }
    }


    /**
     * This method will check validation for Name.
     */
    private void checkValidationForPassword() {
        if (mEtPassword.getText().toString().trim().length() == 0) {
            mInputLayoutPassword.setError(getString(R.string.please_enter_password));
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
        if (mEtConfirmPassword.getText().toString().trim().length() == 0) {
            mInputLayoutConfirmPassword.setError(getString(R.string.please_enter_confirm_password));
            mInputLayoutConfirmPassword.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mInputLayoutConfirmPassword.setErrorEnabled(false);
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
