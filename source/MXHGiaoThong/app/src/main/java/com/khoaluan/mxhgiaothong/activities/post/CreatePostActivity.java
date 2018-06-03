package com.khoaluan.mxhgiaothong.activities.post;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.Application;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.UploadImageListener;
import com.khoaluan.mxhgiaothong.activities.post.dialog.SelectCommentOptionsDialogFragment;
import com.khoaluan.mxhgiaothong.activities.post.dialog.SelectLocationOptionsDialog;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.CustomProgressDialog;
import com.khoaluan.mxhgiaothong.customView.dialog.ErrorMessageDialogFragment;
import com.khoaluan.mxhgiaothong.customView.dialog.SelectModeImageDialogFragment;
import com.khoaluan.mxhgiaothong.eventbus.EventUpdateListPost;
import com.khoaluan.mxhgiaothong.eventbus.EventUpdatePost;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.CreatePostRequest;
import com.khoaluan.mxhgiaothong.restful.request.UpdatePostRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.restful.response.CreatePostResponse;
import com.khoaluan.mxhgiaothong.utils.FileUtils;
import com.khoaluan.mxhgiaothong.utils.GeoHelper;
import com.khoaluan.mxhgiaothong.utils.PermissionUtils;
import com.khoaluan.mxhgiaothong.utils.UploadImageUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hong Hanh on 4/4/2018.
 */

public class CreatePostActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE_CATEGORY = 10;
    private static final String ARG_KEY_POST = "ARG_KEY_POST";
    private static final int REQUEST_CODE_TAKE_PICTURE = 11;
    private static final int REQUEST_CODE_CAMERA = 12;
    private static final int REQUEST_CODE_GET_IMAGE = 13;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 14;

    @BindView(R.id.topBar)
    TopBarView mTopBar;
    @BindView(R.id.imgAvatar)
    ImageView mImgAvatar;
    @BindView(R.id.edt_content)
    EditText mEdtContent;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvPlace)
    TextView mTvPlace;
    @BindView(R.id.tvCategory)
    TextView mTvCategory;
    @BindView(R.id.imgPost)
    ImageView mImgPost;
    @BindView(R.id.tvPostAction)
    TextView mTvPostAction;

    private Context mContext;
    private String token;
    private Location mLocation;
    private String mImageUrl;
    private Category mCategory;
    private int mLevel;
    private Post mPost;
    private File mFile;
    private Uri mImageUri;
    private CustomProgressDialog mProgressDialog;

    public static void start(Context context, Post post) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        intent.putExtra(ARG_KEY_POST, post);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);
        mContext = this;
        init();
    }

    private void init() {
        getExtras();
        initProgressDialog();
        getToken();
        initTopBar();
    }

    private void initProgressDialog() {
        mProgressDialog = new CustomProgressDialog(mContext, "Loading...");
    }

    private void showLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    private void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }

    private void getExtras() {
        mPost = (Post) getIntent().getSerializableExtra(ARG_KEY_POST);
        if(mPost != null) {
            fillData();
        } else {
            getCurrentLocation();
            getUserInfo();
        }
    }

    private void fillData() {
        mTvPostAction.setVisibility(View.GONE);
        mEdtContent.setText(mPost.getContent());
        Glide.with(mContext).load(mPost.getCreator().getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(mImgAvatar);
        mTvName.setText(mPost.getCreator().getFullName());
        if(!TextUtils.isEmpty(mPost.getImageUrl())) {
            mImgPost.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mPost.getImageUrl()).into(mImgPost);
        }
        mTvPlace.setText(mPost.getPlace());
        mCategory = mPost.getCategory();
        mLevel = mPost.getLevel();
        updateUICategory();
        mLocation = new Location("");
        mLocation.setLatitude(mPost.getLocation().getCoordinates().get(1));
        mLocation.setLongitude(mPost.getLocation().getCoordinates().get(0));
    }

    private void getToken() {
        token = PreferManager.getInstance(mContext).getToken();
    }

    private void initTopBar() {
        mTopBar.setImageViewLeft(AppConstants.LEFT_BACK);
        mTopBar.setTextTitle(mPost == null ? "Đăng bài viết" : "Chỉnh sửa bài viết");
        if(mPost != null) {
            mTopBar.setTextViewRight("Lưu");
        }
        mTopBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                finish();
            }

            @Override
            public void onImvRightClicked() {

            }

            @Override
            public void onTvLeftClicked() {

            }

            @Override
            public void onTvRightClicked() {
                showLoading();
                if(mImageUri == null) {
                    updatePost();
                } else {
                    uploadImage();
                }
            }
        });
    }

    private void getUserInfo() {
        User user = PreferManager.getInstance(mContext).getUser();
        if (user == null) return;
        Glide.with(mContext).load(user.getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(mImgAvatar);
        mTvName.setText(user.getFullName());
    }

    private void getCurrentLocation() {
        mLocation = Application.getBackgroundService().getCurrentLocation();
        if (mLocation != null) {
            mTvPlace.setText(GeoHelper.getAddress(mLocation));
        } else {
            mTvPlace.setText("Chưa xác định");
            Toast.makeText(mContext, "Không thể xác định được vị trí hiện tại của bạn.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tvPostAction)
    public void createPost() {
        showLoading();
        if(mImageUri == null) {
            saveNewPost();
        } else {
            uploadImage();
        }
    }

    private void uploadImage() {
        UploadImageUtils.uploadImage(mImageUri, new UploadImageListener() {
            @Override
            public void uploadSuccess(String url) {
                mImageUrl = url;
                saveNewPost();
            }

            @Override
            public void uploadFailure(String err) {
                hideLoading();
                Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNewPost() {
        if(mCategory == null || mLocation == null) return;
        CreatePostRequest request = new CreatePostRequest(
                mEdtContent.getText().toString(),
                mLocation.getLatitude(),
                mLocation.getLongitude(),
                mTvPlace.getText().toString(),
                mCategory.getId(),
                mLevel,
                true,
                mImageUrl);

        ApiManager.getInstance().getPostService().createPost(token, request).enqueue(new RestCallback<CreatePostResponse>() {
            @Override
            public void success(CreatePostResponse res) {
                hideLoading();
                EventBus.getDefault().post(new EventUpdateListPost());
                Toast.makeText(CreatePostActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
                ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                errorDialog.setError(error.message);
                errorDialog.show(getSupportFragmentManager(), CreatePostActivity.class.getName());
            }
        });
    }

    private void updatePost() {
        if(mPost == null) finish();
        UpdatePostRequest request = new UpdatePostRequest(
                mEdtContent.getText().toString(),
                mLocation.getLatitude(),
                mLocation.getLongitude(),
                mTvPlace.getText().toString(),
                mCategory,
                mLevel,
                TextUtils.isEmpty(mImageUrl) ? mPost.getImageUrl() : mImageUrl);
        ApiManager.getInstance().getPostService().updatePost(token, mPost.getId(), request).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                hideLoading();
                EventBus.getDefault().post(new EventUpdatePost());
                EventBus.getDefault().post(new EventUpdateListPost());
                Toast.makeText(mContext, "Chỉnh sửa bài viết thành công", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void failure(RestError error) {
                hideLoading();
                ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                errorDialog.setError(error.message);
                errorDialog.show(getSupportFragmentManager(), CreatePostActivity.class.getName());
            }
        });
    }

    private void updateUICategory() {
        mTvCategory.setVisibility(View.VISIBLE);
        mTvCategory.setText(mCategory.getName());
        switch (mLevel) {
            case 0:
                mTvCategory.setBackgroundResource(R.drawable.bg_corner_solid_green);
                break;
            case 1:
                mTvCategory.setBackgroundResource(R.drawable.bg_corner_solid_yellow);
                break;
            case 2:
                mTvCategory.setBackgroundResource(R.drawable.bg_corner_solid_orange);
                break;
            case 3:
                mTvCategory.setBackgroundResource(R.drawable.bg_corner_solid_red);
                break;
        }
    }

    @OnClick(R.id.imgCategory)
    public void chooseCategory() {
        ChooseCategoryActivity.start(this, REQUEST_CODE_CHOOSE_CATEGORY, mCategory, mLevel);
    }

    @OnClick(R.id.imgCamera)
    public void takePicture() {
        if (PermissionUtils.checkPermission(mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            openDialog();
        } else {
            PermissionUtils.requestPermission(CreatePostActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
        }
    }

    @OnClick(R.id.imgMarker)
    public void searchLocation() {
        openLocationOptionsDialog();
    }

    private void openDialog() {
        SelectModeImageDialogFragment selectModeImageDialogFragment = new SelectModeImageDialogFragment();
        selectModeImageDialogFragment.setPostArticleEditListener(new SelectModeImageDialogFragment.SelectModeImageListener() {
            @Override
            public void callCamera() {
                openCamera();
            }

            @Override
            public void callGallery() {
                openLibrary();
            }
        });
        selectModeImageDialogFragment.show(getSupportFragmentManager(),CreatePostActivity.this.getClass().getName());
    }

    private void openCamera() {
        try {
            mFile = FileUtils.createImageFile();
            mImageUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", mFile);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void openLibrary() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_GET_IMAGE);
    }

    private void openLocationOptionsDialog() {
        SelectLocationOptionsDialog dialogFragment = new SelectLocationOptionsDialog();
        dialogFragment.setLocationOptionsListener(new SelectLocationOptionsDialog.SelectLocationOptionsListener() {
            @Override
            public void setCurrentLocation() {
                getCurrentLocation();
            }

            @Override
            public void openSearchLocation() {
                openSearchActivity();
            }
        });
        dialogFragment.show(getSupportFragmentManager(), PostDetailActivity.class.getName());
    }

    private void openSearchActivity() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setBoundsBias(new LatLngBounds(
                                    new LatLng(8.407168163601074, 104.1448974609375),
                                    new LatLng(10.7723923007117563, 106.6981029510498)))
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED))
            return;
        if (requestCode == REQUEST_CODE_CAMERA) {
            openDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_CODE_CHOOSE_CATEGORY) {
            mCategory = (Category) data.getSerializableExtra(AppConstants.ARG_KEY_CATEGORY_ID);
            mLevel = data.getIntExtra(AppConstants.ARG_KEY_LEVEL, 0);
            updateUICategory();
        } else if(requestCode == REQUEST_CODE_TAKE_PICTURE) {
            if(mFile == null) return;
            mImgPost.setVisibility(View.VISIBLE);
            Glide.with(mContext.getApplicationContext()).load(mFile).into(mImgPost);
        } else if(requestCode == REQUEST_CODE_GET_IMAGE) {
            if (data == null || data.getData() == null)
                return;
            mImgPost.setVisibility(View.VISIBLE);
            Glide.with(mContext.getApplicationContext()).load(data.getData()).into(mImgPost);
            mFile = new File(FileUtils.getPath(mContext, data.getData()));
            mImageUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", mFile);
        } else if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            final Place place = PlacePicker.getPlace(data, this);

            if (place != null) {
                mTvPlace.setText(place.getName().toString());
                String[] parts = place.getLatLng().toString().split("\\(");
                String[] parts1 = parts[1].split("\\)");
//                latLngStartPlace = parts1[0];
                mLocation = new Location("");
                mLocation.setLatitude(place.getLatLng().latitude);
                mLocation.setLongitude(place.getLatLng().longitude);
            }
        }
    }
}
