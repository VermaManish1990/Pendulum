package com.pend.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.AddUserImageResponseModel;
import com.pend.models.DeleteUserImageResponseModel;
import com.pend.models.SetUserImageResponseModel;
import com.pend.models.UpdateUserProfileResponseModel;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.VolleyErrorListener;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

public class EditMyProfileActivity extends BaseActivity implements TextWatcher, View.OnClickListener {

    private static final String TAG = EditMyProfileActivity.class.getSimpleName();
    private View mRootView;
    private View mLlUploadPhotos;
    private EditText mEtName;
    private EditText mEtAge;
    private EditText mEtGender;
    private EditText mEtLocation;
    private ImageView mIvUploadPhoto;
    private TextInputLayout mInputLayoutName;
    private TextInputLayout mInputLayoutAge;
    private TextInputLayout mInputLayoutGender;
    private TextInputLayout mInputLayoutLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        initUI();
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

        JsonObject requestObject;
        String request;

        switch (actionID) {
            case IApiEvent.REQUEST_UPDATE_USER_PROFILE_CODE:

                requestObject = RequestPostDataUtil.updateUserProfileApiRegParam(12345, "", 22, "Male", 1, "");
                request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<UpdateUserProfileResponseModel>(IWebServices.REQUEST_UPDATE_USER_PROFILE_URL, NetworkUtil.getHeaders(this),
                        request, UpdateUserProfileResponseModel.class, new
                        VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(UpdateUserProfileResponseModel response) {
                        updateUi(true, actionID, response);

                    }
                });
                break;

            case IApiEvent.REQUEST_ADD_USER_IMAGE_CODE:

                requestObject = RequestPostDataUtil.addUserImageApiRegParam(12345, true, "");
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

                requestObject = RequestPostDataUtil.deleteUserImageApiRegParam(12345, 1);
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

                requestObject = RequestPostDataUtil.setUserImageApiRegParam(12345, 1, true);
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

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
