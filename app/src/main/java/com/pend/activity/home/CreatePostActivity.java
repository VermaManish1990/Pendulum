package com.pend.activity.home;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
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
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pend.BaseActivity;
import com.pend.R;
import com.pend.activity.contest.ContestActivity;
import com.pend.activity.login.ProfileActivity;
import com.pend.activity.mirror.MirrorActivity;
import com.pend.arena.view.ArenaActivity;
import com.pend.interfaces.Constants;
import com.pend.interfaces.IApiEvent;
import com.pend.interfaces.IWebServices;
import com.pend.models.AddAndUpdatePostResponseModel;
import com.pend.models.ErrorResponseModel;
import com.pend.models.GetPostsResponseModel;
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
import com.squareup.picasso.Picasso;

import java.io.File;

public class CreatePostActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = CreatePostActivity.class.getSimpleName();
    private ImageView mIvPost;
    private TextView mEtPostInfo;

    private File mPhotoPath;
    private String mEncodedImage;
    private View mRootView;
    private int mMirrorId;
    private boolean mIsUpdatePost;
    private GetPostsResponseModel.GetPostsDetails mPostDetails;
    private Button mBtCreatePost;
    private View mRlQuarterView;
    private View mFlQuarterBlackView;
    private View mFlMenuView;
    private ImageView mIvProfile;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Bundle localBundle = getIntent().getExtras();
        if (localBundle != null) {

            if (localBundle.containsKey(Constants.MIRROR_ID_KEY)) {
                mMirrorId = localBundle.getInt(Constants.MIRROR_ID_KEY, 0);
            }

            if (localBundle.containsKey(Constants.POST_DETAILS_KEY)) {
                mPostDetails = (GetPostsResponseModel.GetPostsDetails) localBundle.getSerializable(Constants.POST_DETAILS_KEY);
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
        mBtCreatePost = findViewById(R.id.bt_create_post);

        View quarterView = findViewById(R.id.quarter_view);
        mRlQuarterView = quarterView.findViewById(R.id.rl_quarter_view);
        mFlQuarterBlackView = quarterView.findViewById(R.id.fl_quarter_black_view);
        mFlMenuView = quarterView.findViewById(R.id.fl_menu_view);

        quarterView.findViewById(R.id.fl_mirror).setOnClickListener(this);
        quarterView.findViewById(R.id.fl_contest).setOnClickListener(this);
        mIvProfile = quarterView.findViewById(R.id.iv_profile);
        quarterView.findViewById(R.id.fl_area).setOnClickListener(this);
        mFlMenuView.setOnClickListener(this);
        mIvProfile.setOnClickListener(this);

        mIvPost.setOnClickListener(this);
        mBtCreatePost.setOnClickListener(this);
    }

    @Override
    protected void setInitialData() {

        String imageUrl = SharedPrefUtils.getProfileImageUrl(this);

        if (imageUrl != null && !imageUrl.equals("")) {
            Picasso.with(this)
                    .load(imageUrl)
                    .into(mIvProfile);
        }

        mIsUpdatePost = false;
        if (mPostDetails != null) {

            mIsUpdatePost = true;
            mBtCreatePost.setText(getString(R.string.update_post));

            if (mPostDetails.imageURL != null) {

                Picasso.with(this)
                        .load(mPostDetails.imageURL)
                        .into(mIvPost, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap bitmap = ((BitmapDrawable) mIvPost.getDrawable()).getBitmap();
                                mEncodedImage = OtherUtil.getBase64FormatFromBitmap(bitmap);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }

            mEtPostInfo.setText(mPostDetails.postInfo != null ? mPostDetails.postInfo : "");
        }

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        switch (actionID) {
            case IApiEvent.REQUEST_ADD_POST_CODE:
                if (status) {
                    AddAndUpdatePostResponseModel addAndUpdatePostResponseModel = (AddAndUpdatePostResponseModel) serviceResponse;
                    if (addAndUpdatePostResponseModel != null && addAndUpdatePostResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdatePostResponseModel.statusCode);

                        if (addAndUpdatePostResponseModel.Data != null && addAndUpdatePostResponseModel.Data.postData != null) {
                            finish();
                        }
                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
                    LoggerUtil.d(TAG, getString(R.string.status_is_false));
                }
                break;

            case IApiEvent.REQUEST_UPDATE_POST_CODE:
                if (status) {
                    AddAndUpdatePostResponseModel addAndUpdatePostResponseModel = (AddAndUpdatePostResponseModel) serviceResponse;
                    if (addAndUpdatePostResponseModel != null && addAndUpdatePostResponseModel.status) {
                        LoggerUtil.d(TAG, addAndUpdatePostResponseModel.statusCode);

                        if (addAndUpdatePostResponseModel.Data != null && addAndUpdatePostResponseModel.Data.postData != null) {
                            finish();
                        }

                    } else {
                        LoggerUtil.d(TAG, getString(R.string.server_error_from_api));
                    }
                } else {
                    OtherUtil.showErrorMessage(this, serviceResponse);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject jsonObject;
        String request;

        switch (actionID) {
            case IApiEvent.REQUEST_ADD_POST_CODE:

                jsonObject = RequestPostDataUtil.addPostApiRegParam(userId, mMirrorId, mEtPostInfo.getText().toString(), mEncodedImage != null ? mEncodedImage : "");
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<AddAndUpdatePostResponseModel>(IWebServices.REQUEST_ADD_POST_URL, NetworkUtil.getHeaders(this),
                        request, AddAndUpdatePostResponseModel.class, new VolleyErrorListener(this, actionID)) {

                    @Override
                    protected void deliverResponse(AddAndUpdatePostResponseModel response) {
                        updateUi(true, actionID, response);
                    }
                });
                break;

            case IApiEvent.REQUEST_UPDATE_POST_CODE:

                jsonObject = RequestPostDataUtil.updatePostApiRegParam(userId, mPostDetails.postID, mMirrorId, mEtPostInfo.getText().toString(), mEncodedImage, false, true);
                request = jsonObject.toString();
                RequestManager.addRequest(new GsonObjectRequest<AddAndUpdatePostResponseModel>(IWebServices.REQUEST_UPDATE_POST_URL, NetworkUtil.getHeaders(this),
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create_post:

                if (mEncodedImage != null || mEtPostInfo.getText().toString().length() > 0) {

                    if (mIsUpdatePost) {
                        getData(IApiEvent.REQUEST_UPDATE_POST_CODE);
                    } else {
                        getData(IApiEvent.REQUEST_ADD_POST_CODE);
                    }
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

            case R.id.iv_profile:
                hideReveal();
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.fl_mirror:
                hideReveal();
                Intent intentMirror = new Intent(this, MirrorActivity.class);
                startActivity(intentMirror);
                break;

            case R.id.fl_contest:
                hideReveal();
                Intent intentContest = new Intent(this, ContestActivity.class);
                startActivity(intentContest);
//                Snackbar.make(mRootView, getString(R.string.under_development), Snackbar.LENGTH_LONG).show();

                break;

            case R.id.fl_area:
                hideReveal();
                Intent intentArena = new Intent(this, ArenaActivity.class);
                startActivity(intentArena);
                break;

            case R.id.fl_menu_view:
                mFlMenuView.setVisibility(View.GONE);
                showReveal();
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
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void showReveal() {
        int cx = 0;
        int cy = mRlQuarterView.getHeight();
        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mRlQuarterView, cx, cy, 0, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRlQuarterView.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void hideReveal() {

        int cx = 0;
        int cy = mRlQuarterView.getHeight();
        float initialRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(mRlQuarterView, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRlQuarterView.setVisibility(View.GONE);
                mFlMenuView.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (mRlQuarterView.getVisibility() == View.VISIBLE) {
            hideReveal();
        } else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Rect outRect = new Rect();
        mFlQuarterBlackView.getGlobalVisibleRect(outRect);

        if (mRlQuarterView.getVisibility() == View.VISIBLE && !outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
            hideReveal();
        }
        return super.dispatchTouchEvent(event);
    }


}
