package com.pend.activity.home;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.mirror.MirrorDetailsActivity;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.AddAndUpdatePostResponseModel;
import com.pend.util.AndroidPermissionUtils;
import com.pend.util.ImageFilePathUtil;
import com.pend.util.LoggerUtil;
import com.pend.util.NetworkUtil;
import com.pend.util.OtherUtil;
import com.pend.util.RequestPostDataUtil;
import com.pend.util.SharedPrefUtils;
import com.pend.util.VolleyErrorListener;
import com.pendulum.volley.ext.GsonObjectRequest;
import com.pendulum.volley.ext.RequestManager;

import java.io.File;

public class CreatePostActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = CreatePostActivity.class.getSimpleName();
    private ImageView mIvPost;
    private TextView mEtPostInfo;

    private File mPhotoPath;
    private String mEncodedImage;
    private View mRootView;
    private int mMirrorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {
            if (localBundle.containsKey(Constants.MIRROR_ID_KEY)) {
                mMirrorId = localBundle.getInt(Constants.MIRROR_ID_KEY, 0);
            }
        }

        initUI();
        setInitialData();
    }

    @Override
    protected void initUI() {

        mRootView = findViewById(R.id.root_view);
        mEtPostInfo = findViewById(R.id.et_post_info);
        mIvPost = findViewById(R.id.iv_post);
        mIvPost.setOnClickListener(this);
        findViewById(R.id.bt_create_post).setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {


    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_ADD_POST_CODE:
                if (status) {
                    AddAndUpdatePostResponseModel addAndUpdatePostResponseModel = (AddAndUpdatePostResponseModel) serviceResponse;
                    if (addAndUpdatePostResponseModel != null && addAndUpdatePostResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdatePostResponseModel.statusCode);

                        if(addAndUpdatePostResponseModel.Data!=null&&addAndUpdatePostResponseModel.Data.postData!=null){
                            finish();
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
        if (!NetworkUtil.isInternetConnected(this)) {
            Snackbar.make(mRootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG).show();
            return;
        }
        showProgressDialog();

        int userId = -1;
        try {
            userId = Integer.parseInt(SharedPrefUtils.getUserId(CreatePostActivity.this));
        }catch (Exception e){
            e.printStackTrace();
        }

        switch (actionID) {
            case IApiEvent.REQUEST_ADD_POST_CODE:

                JsonObject requestObject = RequestPostDataUtil.addPostApiRegParam(userId,mMirrorId,mEtPostInfo.getText().toString(),mEncodedImage);
                String request = requestObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<AddAndUpdatePostResponseModel>(IWebServices.REQUEST_ADD_POST_URL, NetworkUtil.getHeaders(this),
                        request, AddAndUpdatePostResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(AddAndUpdatePostResponseModel response) {
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
            case R.id.bt_create_post:
                if (mEncodedImage != null) {
                    getData(IApiEvent.REQUEST_ADD_POST_CODE);
                } else {
                    Snackbar.make(mRootView, getString(R.string.can_t_add_a_post), Snackbar.LENGTH_LONG).show();
                }
                break;

            case R.id.iv_post:
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), getString(R.string.my_images));
                imagesFolder.mkdir();
                mPhotoPath = new File(imagesFolder, "myProfile.jpg");
                selectImageDialog();
                break;

            default:
                LoggerUtil.d(TAG, getString(R.string.wrong_case_selection));
                break;
        }
    }

    /**
     * Method is used to show an Alert Dialog for select Profile image
     */
    private void selectImageDialog() {
        final CharSequence[] items = new String[]{getString(R.string.take_photo), getString(R.string.choose_from_library), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_photo));

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (items[which].toString()) {
                    case "Take Photo":

                        if (AndroidPermissionUtils.checkPermission(CreatePostActivity.this, Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            takePhotoFromCamera();
                        } else {
                            AndroidPermissionUtils.requestForPermission(CreatePostActivity.this, Constants.REQUEST_TAKE_PHOTO, Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        break;

                    case "Choose from Library":

                        if (AndroidPermissionUtils.checkPermission(CreatePostActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            selectImageFromAlbum();
                        } else {
                            AndroidPermissionUtils.requestForPermission(CreatePostActivity.this, Constants.REQUEST_SELECT_IMAGE_FROM_ALBUM,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                        break;

                    case "Cancel":
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Method is used to select image from Album or Gallery
     */
    private void selectImageFromAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.REQUEST_SELECT_IMAGE_FROM_ALBUM);
    }

    /**
     * Method is used to take photo from Camera
     */
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".my.package.name.provider", mPhotoPath));
        startActivityForResult(intent, Constants.REQUEST_TAKE_PHOTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String imagePath = null;
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = null;
            switch (requestCode) {
                case Constants.REQUEST_SELECT_IMAGE_FROM_ALBUM:

                    selectedImage = data.getData();
                    imagePath = ImageFilePathUtil.getPath(CreatePostActivity.this, selectedImage);
                    if (imagePath != null)
                        selectedImage = Uri.parse(imagePath);
                    break;

                case Constants.REQUEST_TAKE_PHOTO:

                    if (mPhotoPath != null) {
                        imagePath = mPhotoPath.getPath();
                    }
                    selectedImage = Uri.parse(imagePath);
                    break;
            }

            if (selectedImage != null) {
                mIvPost.setImageURI(selectedImage);
                mEncodedImage = OtherUtil.getBase64Format(selectedImage.getPath());
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.REQUEST_TAKE_PHOTO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoFromCamera();
                } else {
                    AndroidPermissionUtils.requestForPermission(CreatePostActivity.this, Constants.REQUEST_TAKE_PHOTO, Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                break;
            }

            case Constants.REQUEST_SELECT_IMAGE_FROM_ALBUM: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImageFromAlbum();
                } else {
                    AndroidPermissionUtils.requestForPermission(CreatePostActivity.this, Constants.REQUEST_SELECT_IMAGE_FROM_ALBUM,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        }
    }
}
