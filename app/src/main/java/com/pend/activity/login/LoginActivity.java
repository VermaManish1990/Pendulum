package com.pend.activity.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.BaseResponseModel;
import com.pend.R;
import com.pend.activity.home.HomeActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.GetNewsFeedDataModel;
import com.pend.models.LoginResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.utils.ConnectivityUtils;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private final String TAG = LoginActivity.class.getSimpleName();
    private String mUserName;
    private String mPassword;
    private String age;
    private String gender;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private boolean mIsChecked = true;
    private TextInputLayout mInputLayoutEmail;
    private TextInputLayout mInputLayoutPassword;
    private View mRootView;
    LoginButton loginButton;
    CallbackManager callbackManager;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        initFB();
    }

    @Override
    protected void initUI() {
        mRootView = findViewById(R.id.root_view);
        mEtEmail = findViewById(R.id.et_email);
        mEtPassword = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.login_button);
        findViewById(R.id.bt_sign_in).setOnClickListener(this);
        findViewById(R.id.tv_forgot_password).setOnClickListener(this);
        findViewById(R.id.bt_sign_up).setOnClickListener(this);
        findViewById(R.id.bt_guest_user).setOnClickListener(this);

        mInputLayoutEmail = findViewById(R.id.input_layout_email);
        mInputLayoutPassword = findViewById(R.id.input_layout_password);

        mEtEmail.addTextChangedListener(LoginActivity.this);
        mEtPassword.addTextChangedListener(LoginActivity.this);

    }

    @Override
    public void updateUi(final boolean status, final int actionID, final Object serviceResponse) {

        switch (actionID) {
            case IApiEvent.REQUEST_LOGIN_CODE:
                if (status) {
                    LoginResponseModel loginResponseModel = (LoginResponseModel) serviceResponse;
                    if (loginResponseModel != null && loginResponseModel.status) {
                        LoggerUtil.d(TAG, loginResponseModel.statusCode);

                        if (loginResponseModel.Data != null) {

                            if (loginResponseModel.Data.imageData != null && loginResponseModel.Data.imageData.size() > 0) {

                                SharedPrefUtils.setProfileImageUrl(LoginActivity.this, loginResponseModel.Data.imageData.get(0).imageURL);
                            }
                            if (loginResponseModel.Data.userData != null) {

                                SharedPrefUtils.setUserLoggedIn(LoginActivity.this, true);
                                userId=loginResponseModel.Data.userData.userID;
                                SharedPrefUtils.setUserId(LoginActivity.this, String.valueOf(loginResponseModel.Data.userData.userID));
                                getData(IApiEvent.REQUEST_GET_NEWS_FEED_DATA);

                            }
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        OtherUtil.showAlertDialog(getString(R.string.invalid_credential), this, new DialogInterface.OnClickListener() {
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

            case IApiEvent.REQUEST_FORGOT_PASSWORD_CODE:
               if (status) {
                    BaseResponseModel baseResponseModel = (BaseResponseModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }

                break;
            case IApiEvent.REQUEST_LOGIN_WITH_FB:
                if (status) {
                    LoginResponseModel loginResponseModel = (LoginResponseModel) serviceResponse;
                    if (loginResponseModel != null && loginResponseModel.status) {
                        LoggerUtil.d(TAG, loginResponseModel.statusCode);

                        if (loginResponseModel.Data != null) {

                            if (loginResponseModel.Data.imageData != null && loginResponseModel.Data.imageData.size() > 0) {

                                SharedPrefUtils.setProfileImageUrl(LoginActivity.this, loginResponseModel.Data.imageData.get(0).imageURL);
                            }
                            if (loginResponseModel.Data.userData != null) {

                                SharedPrefUtils.setUserLoggedIn(LoginActivity.this, true);
                                userId=loginResponseModel.Data.userData.userID;
                                SharedPrefUtils.setUserId(LoginActivity.this, String.valueOf(loginResponseModel.Data.userData.userID));
                              //  Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                               // startActivity(intent);
                                //finish();
                                getData(IApiEvent.REQUEST_GET_NEWS_FEED_DATA);
                            }
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        OtherUtil.showAlertDialog(getString(R.string.invalid_credential), this, new DialogInterface.OnClickListener() {
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
            case IApiEvent.REQUEST_GET_NEWS_FEED_DATA:
                if (status) {
                    GetNewsFeedDataModel baseResponseModel = (GetNewsFeedDataModel) serviceResponse;
                    if (baseResponseModel != null && baseResponseModel.status) {
                        LoggerUtil.d(TAG, baseResponseModel.statusCode);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                        Intent intent = new Intent(LoginActivity.this, MirrorActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                   // OtherUtil.showErrorMessage(this, serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                    Intent intent = new Intent(LoginActivity.this, MirrorActivity.class);
                    startActivity(intent);
                    finish();
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
            case IApiEvent.REQUEST_LOGIN_CODE:

                requestObject = RequestPostDataUtil.loginApiRegParam(mUserName, mPassword);
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<LoginResponseModel>(IWebServices.REQUEST_LOGIN_URL, NetworkUtil.getHeaders(this),
                        request, LoginResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(LoginResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_FORGOT_PASSWORD_CODE:

                //TODO Change emailId
                requestObject = RequestPostDataUtil.forgotPasswordApiRegParam("rahul9927chauhan@gmail.com");
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<BaseResponseModel>(IWebServices.REQUEST_FORGOT_PASSWORD_URL, NetworkUtil.getHeaders(this),
                        request, BaseResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(BaseResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_LOGIN_WITH_FB:

                //TODO Change emailId
                requestObject = RequestPostDataUtil.loginWithFB(mUserName,"24","Female");
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<LoginResponseModel>(IWebServices.REQUEST_LOGIN_WITH_FB, NetworkUtil.getHeaders(this),
                        request, LoginResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {


                    @Override
                    protected void deliverResponse(LoginResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_GET_NEWS_FEED_DATA:

                String getPostsUrl = IWebServices.REQUEST_GET_NEWS_FEED_DATA + Constants.PARAM_USER_ID + "=" + userId
                        + "&" + Constants.PARAM_PAGE_NUMBER + "= 1" ;
                Log.e("userId",userId+"");
                RequestManager.addRequest(new GsonObjectRequest<GetNewsFeedDataModel>(getPostsUrl, NetworkUtil.getHeaders(this),
                        null, GetNewsFeedDataModel.class, new VolleyErrorListener(this, actionID)) {


                    @Override
                    protected void deliverResponse(GetNewsFeedDataModel response) {
                        updateUi(true, actionID, response);


                    }
                });

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

                if (isAllFieldsValid()) {
                    getData(IApiEvent.REQUEST_LOGIN_CODE);
                }
                break;

            case R.id.bt_guest_user:

                Intent intentGuest = new Intent(LoginActivity.this, MirrorActivity.class);
                startActivity(intentGuest);
                finish();
                break;

            case R.id.bt_sign_up:

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();

                break;

            case R.id.tv_forgot_password:

              //  Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();
                Intent intentpassword = new Intent(LoginActivity.this,SendOTPForgotPasswordActivity.class);
                startActivity(intentpassword);
//                getData(IApiEvent.REQUEST_LOGIN_CODE);

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
            mInputLayoutEmail.setError(getString(R.string.please_enter_phone_number));
            mInputLayoutEmail.setErrorEnabled(true);
            mIsChecked = false;
        } /*else if (!mEtEmail.getText().toString().trim().matches(Constants.EMAIL_PATTERN)) {
            mInputLayoutEmail.setError(getString(R.string.please_enter_valid_mail_id));
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
            mInputLayoutPassword.setError(getString(R.string.please_enter_password));
            mInputLayoutPassword.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mPassword = mEtPassword.getText().toString();
            mInputLayoutPassword.setErrorEnabled(false);
        }
    }


    // Facebook Login
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                             mUserName=email;
                            getData(IApiEvent.REQUEST_LOGIN_WITH_FB);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    void initFB()
    {
        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

        if (!loggedOut) {
         /*   Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);*/
          /*  Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());*/

            //Using Graph API
            getUserProfile(AccessToken.getCurrentAccessToken());
        }


        AccessTokenTracker fbTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                 /*   txtUsername.setText(null);
                    txtEmail.setText(null);
                    imageView.setImageResource(0);
                    Toast.make Text(getApplicationContext(),"User Logged Out.",Toast.LENGTH_LONG).show();
              */  }
            }
        };
        fbTracker.startTracking();

        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //loginResult.getAccessToken();
                //loginResult.getRecentlyDeniedPermissions()
                //loginResult.getRecentlyGrantedPermissions()
                boolean loggedOut = AccessToken.getCurrentAccessToken() == null;

               /* if (!loggedOut) {
                    //Using Graph API
                    getUserProfile(AccessToken.getCurrentAccessToken());
                }
*/           if (!loggedOut) {
                    //Using Graph API
                    getUserProfile(AccessToken.getCurrentAccessToken());
                }

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }
}
