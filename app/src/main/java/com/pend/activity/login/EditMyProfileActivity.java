package com.pend.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.AddUserImageResponseModel;
import com.pend.models.DeleteUserImageResponseModel;
import com.pend.models.SetUserImageResponseModel;
import com.pend.models.UpdateUserProfileResponseModel;
import com.pend.models.UserProfileResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;
import com.squareup.picasso.Picasso;

public class EditMyProfileActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private static final String TAG = EditMyProfileActivity.class.getSimpleName();
    private View mRootView;
    private LinearLayout mLlUploadPhotos;
    private EditText mEtName;
    private EditText mEtAge;
    private EditText mEtGender;
    private EditText mEtLocation;
    private ImageView mIvUploadPhoto;
    private TextInputLayout mInputLayoutName;
    private TextInputLayout mInputLayoutAge;
    private TextInputLayout mInputLayoutGender;
    private TextInputLayout mInputLayoutLocation;
    private boolean mIsChecked = true;
    private String mFullName;
    private int mAge;
    private String mGender;
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mLlUploadPhotos = findViewById(R.id.ll_upload_photos);
        mEtName = findViewById(R.id.et_name);
        mEtAge = findViewById(R.id.et_age);
        mEtGender = findViewById(R.id.et_gender);
        mEtLocation = findViewById(R.id.et_location);
        mIvUploadPhoto = findViewById(R.id.iv_upload_photo);

        mInputLayoutName = findViewById(R.id.input_layout_name);
        mInputLayoutAge = findViewById(R.id.input_layout_age);
        mInputLayoutGender = findViewById(R.id.input_layout_gender);
        mInputLayoutLocation = findViewById(R.id.input_layout_location);

        findViewById(R.id.bt_save).setOnClickListener(this);
        mIvUploadPhoto.setOnClickListener(this);

    }

    @Override
    protected void setInitialData() {

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.USER_DATA_MODEL_KEY)) {
                UserProfileResponseModel.UserProfileData userProfileData = (UserProfileResponseModel.UserProfileData) localBundle.getSerializable(
                        Constants.USER_DATA_MODEL_KEY);

                if (userProfileData != null && userProfileData.userData != null) {
                    mEtName.setText(userProfileData.userData.userName != null ? userProfileData.userData.userName : "");
                    mEtAge.setText(String.valueOf(userProfileData.userData.userAge));
                    mEtGender.setText(userProfileData.userData.userGender != null ? userProfileData.userData.userGender : "");
                    mEtLocation.setText(userProfileData.userData.cityName != null ? userProfileData.userData.cityName : "");
                }

                if (userProfileData != null && userProfileData.imageData != null) {
                    for (UserProfileResponseModel.ImageDetails imageDetails : userProfileData.imageData) {
                        ImageView imageView = new ImageView(this);
                        Picasso.with(this)
                                .load(imageDetails.imageURL)
                                .resize(150, 150)
                                .into(imageView);
                        mLlUploadPhotos.addView(imageView);
                    }
                }
            }
        }
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_UPDATE_USER_PROFILE_CODE:
                if (status) {
                    UpdateUserProfileResponseModel updateUserProfileResponseModel = (UpdateUserProfileResponseModel) serviceResponse;
                    if (updateUserProfileResponseModel != null && updateUserProfileResponseModel.status) {
                        LoggerUtil.d(TAG, updateUserProfileResponseModel.statusCode);

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_ADD_USER_IMAGE_CODE:
                if (status) {
                    AddUserImageResponseModel addUserImageResponseModel = (AddUserImageResponseModel) serviceResponse;
                    if (addUserImageResponseModel != null && addUserImageResponseModel.status) {
                        LoggerUtil.d(TAG, addUserImageResponseModel.statusCode);

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_DELETE_USER_IMAGE_CODE:
                if (status) {
                    DeleteUserImageResponseModel deleteUserImageResponseModel = (DeleteUserImageResponseModel) serviceResponse;
                    if (deleteUserImageResponseModel != null && deleteUserImageResponseModel.status) {
                        LoggerUtil.d(TAG, deleteUserImageResponseModel.statusCode);

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_SET_USER_IMAGE_CODE:
                if (status) {
                    SetUserImageResponseModel setUserImageResponseModel = (SetUserImageResponseModel) serviceResponse;
                    if (setUserImageResponseModel != null && setUserImageResponseModel.status) {
                        LoggerUtil.d(TAG, setUserImageResponseModel.statusCode);

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
        if (!NetworkUtil.isInternetConnected(this)) {
            Snackbar.make(mRootView, getString(R.string.network_connection), Snackbar.LENGTH_LONG);
            return;
        }
        showProgressDialog();
        JsonObject requestObject;
        String request;

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(EditMyProfileActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_UPDATE_USER_PROFILE_CODE:

                //TODO CityId
                requestObject = RequestPostDataUtil.updateUserProfileApiRegParam(userId, "", mAge, mGender, 1, mLocation);
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<UpdateUserProfileResponseModel>(IWebServices.REQUEST_UPDATE_USER_PROFILE_URL, NetworkUtil.getHeaders(this),
                        request, UpdateUserProfileResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(UpdateUserProfileResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_ADD_USER_IMAGE_CODE:

                //TODO imageUrl
                requestObject = RequestPostDataUtil.addUserImageApiRegParam(userId, true, "");
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<AddUserImageResponseModel>(IWebServices.REQUEST_ADD_USER_IMAGE_URL, NetworkUtil.getHeaders(this),
                        request, AddUserImageResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(AddUserImageResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_DELETE_USER_IMAGE_CODE:

                //TODO imageId
                requestObject = RequestPostDataUtil.deleteUserImageApiRegParam(userId, 1);
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<DeleteUserImageResponseModel>(IWebServices.REQUEST_DELETE_USER_IMAGE_URL, NetworkUtil.getHeaders(this),
                        request, DeleteUserImageResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(DeleteUserImageResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_SET_USER_IMAGE_CODE:

                //TODO imageId
                requestObject = RequestPostDataUtil.setUserImageApiRegParam(userId, 1, true);
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<SetUserImageResponseModel>(IWebServices.REQUEST_SET_USER_IMAGE_URL, NetworkUtil.getHeaders(this),
                        request, SetUserImageResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(SetUserImageResponseModel response) {
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
            case R.id.bt_save:

                if (isAllFieldsValid()) {
                    getData(IApiEvent.REQUEST_UPDATE_USER_PROFILE_CODE);
                }
                break;

            case R.id.iv_upload_photo:
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
        } else if (mEtAge.hasFocus()) {
            checkValidationForAge();
        } else if (mEtGender.hasFocus()) {
            checkValidationGender();
        } else if (mEtLocation.hasFocus()) {
            checkValidationForLocation();
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
        checkValidationForAge();
        checkValidationGender();
        checkValidationForLocation();
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
     * This method will check validation for Age.
     */
    private void checkValidationForAge() {
        if (mEtAge.getText().toString().trim().length() == 0) {
            mInputLayoutAge.setError(getString(R.string.please_enter_age));
            mInputLayoutAge.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mAge = Integer.parseInt(mEtAge.getText().toString().trim());
            mInputLayoutAge.setErrorEnabled(false);
        }
    }

    /**
     * This method will check validation for Gender.
     */
    private void checkValidationGender() {
        if (mEtGender.getText().toString().trim().length() == 0) {
            mInputLayoutGender.setError(getString(R.string.please_enter_gender));
            mInputLayoutGender.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mGender = mEtGender.getText().toString().trim();
            mInputLayoutGender.setErrorEnabled(false);
        }
    }

    /**
     * This method will check validation for Location.
     */
    private void checkValidationForLocation() {
        if (mEtLocation.getText().toString().trim().length() == 0) {
            mInputLayoutLocation.setError(getString(R.string.please_enter_location));
            mInputLayoutLocation.setErrorEnabled(true);
            mIsChecked = false;
        } else {
            mLocation = mEtLocation.getText().toString().trim();
            mInputLayoutLocation.setErrorEnabled(false);
        }
    }

}
