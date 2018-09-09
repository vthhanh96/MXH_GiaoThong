package com.khoaluan.mxhgiaothong.activities.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.SelectModeImageDialogFragment;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.Category;
import com.khoaluan.mxhgiaothong.restful.model.Post;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.UpdatePostRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.utils.FileUtils;
import com.khoaluan.mxhgiaothong.utils.PermissionUtils;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPostActivity extends AppCompatActivity {

    private static final String ARG_KEY_SELECTED_CATEGORIES = "ARG_KEY_SELECTED_CATEGORIES";
    private static final int REQUEST_CODE_SELECT_CATEGORY = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;
    private static final int REQUEST_CODE_TAKE_PICTURE = 3;
    private static final int REQUEST_CODE_GET_IMAGE = 4;
    private static final int REQUEST_CODE_CAMERA = 5;
    private static final String ARG_KEY_POST = "ARG_KEY_POST";

    @BindView(R.id.topBar) TopBarView mTopBarView;
    @BindView(R.id.imgAvatar) ImageView mImgAvatar;
    @BindView(R.id.tvName) TextView mTvName;
    @BindView(R.id.rcvCategories) RecyclerView mCategoriesRecyclerView;
    @BindView(R.id.loPlace) LinearLayout mPlaceLayout;
    @BindView(R.id.tvPlace) TextView mTvPlace;
    @BindView(R.id.rcvImages) RecyclerView mImagesRecyclerView;
    @BindView(R.id.loAmount) LinearLayout mAmountLayout;
    @BindView(R.id.tvAmount) TextView mTvAmount;
    @BindView(R.id.loTime) LinearLayout mTimeLayout;
    @BindView(R.id.tvTime) TextView mTvTime;
    @BindView(R.id.edtContent) EditText mEdtContent;

    private String mToken;
    private CategoryAdapter mCategoryAdapter;
    private File mFile;
    private Uri mImageUri;
    private ImageAdapter mImageAdapter;
    private MaterialNumberPicker mNumberPicker;
    private Post mPost;
    private Location mLocation;

    public static void start(Context context, Post post) {
        Intent starter = new Intent(context, EditPostActivity.class);
        starter.putExtra(ARG_KEY_POST, post);
        context.startActivity(starter);
    }

    @OnClick(R.id.btnCamera)
    public void cameraButtonClicked() {
        if (PermissionUtils.checkPermission(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            openDialog();
        } else {
            PermissionUtils.requestPermission(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
        }
    }

    @OnClick(R.id.btnCategory)
    public void chooseCategory() {
        CategoryActivity.startForResult(this, mPost.getCategories(), REQUEST_CODE_SELECT_CATEGORY);
    }

    @OnClick(R.id.btnAmount)
    public void chooseAmount() {
        showNumberPickerDialog();
    }

    @OnClick(R.id.btnLocation)
    public void chooseLocation() {
        openSearchActivity();
    }

    @OnClick(R.id.btnTime)
    public void chooseTime() {
        showDateTimePickerDialog();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getExtras();
        initTopBar();
        getUserInfo();
        initNumberPicker();
        initCategoryRecyclerView();
        initImageRecyclerView();
        initData();
    }

    private void getExtras() {
        mPost = (Post) getIntent().getSerializableExtra(ARG_KEY_POST);
    }

    private void initTopBar() {
        mTopBarView.setImageViewLeft(AppConstants.LEFT_BACK);
        mTopBarView.setTextViewRight(getString(R.string.save));
        mTopBarView.setTextTitle(getString(R.string.update_post));
        mTopBarView.setOnClickListener(new TopBarView.OnItemClickListener() {
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
                updatePost();
            }
        });
    }

    private void getUserInfo() {
        mToken = PreferManager.getInstance(this).getToken();
    }

    private void initData() {
        if(mPost == null) return;
        if(mPost.getCreator() != null) {
            Glide.with(getApplicationContext()).load(mPost.getCreator().getAvatar()).apply(RequestOptions.circleCropTransform()).into(mImgAvatar);
            mTvName.setText(mPost.getCreator().getFullName());
        }

        mTvAmount.setText(String.valueOf(mPost.getAmount()));
        mTvPlace.setText(mPost.getPlace());
        setTime(mPost.getTime());
        mEdtContent.setText(mPost.getContent());
        mCategoryAdapter.setNewData(mPost.getCategories());
        mLocation = new Location("");
        mLocation.setLongitude(mPost.getLocation().getCoordinates().get(0));
        mLocation.setLatitude(mPost.getLocation().getCoordinates().get(1));
    }

    private void initNumberPicker() {
        mNumberPicker = new MaterialNumberPicker.Builder(this)
                .minValue(1)
                .maxValue(100)
                .defaultValue(2)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();
    }

    private void initCategoryRecyclerView() {
        mCategoryAdapter = new CategoryAdapter();

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);

        mCategoriesRecyclerView.setAdapter(mCategoryAdapter);
        mCategoriesRecyclerView.setLayoutManager(flowLayoutManager);

    }

    private void initImageRecyclerView() {
        mImageAdapter = new ImageAdapter();
        mImageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId() == R.id.imgDelete) {
                    mImageAdapter.remove(position);
                    updateImages();
                }
            }
        });

        mImagesRecyclerView.setAdapter(mImageAdapter);
        mImagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void updateCategory() {
        if(mPost.getCategories() == null || mPost.getCategories().isEmpty()) {
            mCategoryAdapter.setNewData(new ArrayList<Category>());
            mCategoriesRecyclerView.setVisibility(View.GONE);
            return;
        }

        mCategoriesRecyclerView.setVisibility(View.VISIBLE);
        mCategoryAdapter.setNewData(mPost.getCategories());
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
        selectModeImageDialogFragment.show(getSupportFragmentManager(), this.getClass().getName());
    }

    private void openCamera() {
        try {
            mFile = FileUtils.createImageFile();
            mImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", mFile);

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

    private void updateImages() {
        if(mImageAdapter.getData().isEmpty()) {
            mImagesRecyclerView.setVisibility(View.GONE);
        } else {
            mImagesRecyclerView.setVisibility(View.VISIBLE);
        }
        mImageAdapter.notifyDataSetChanged();
    }

    private void showNumberPickerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Chọn số lượng người tham gia")
                .setView(mNumberPicker)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPost.setAmount(mNumberPicker.getValue());
                        mAmountLayout.setVisibility(View.VISIBLE);
                        mTvAmount.setText(String.valueOf(mNumberPicker.getValue()));
                    }
                })
                .show();
    }

    private void showDateTimePickerDialog() {
        if(mPost.getTime() == null) {
            mPost.setTime(Calendar.getInstance().getTime());
        }
        SwitchDateTimeDialogFragment dialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Chọn thời gian",
                "OK",
                "Hủy"
        );

        dialogFragment.startAtTimeView();
        dialogFragment.setHighlightAMPMSelection(true);
        dialogFragment.setDefaultDateTime(mPost.getTime());
        dialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                setTime(date);
            }

            @Override
            public void onNegativeButtonClick(Date date) {

            }
        });

        dialogFragment.show(getSupportFragmentManager(), "dateTimePicker");
    }

    private void setTime(Date date) {
        if(date == null) return;
        mPost.setTime(date);
        mTvTime.setText(DateFormat.format("dd/MM/yyyy hh:mm", date));
        mTimeLayout.setVisibility(View.VISIBLE);
    }

    private void updatePost() {
        UpdatePostRequest request = new UpdatePostRequest(
                mEdtContent.getText().toString(),
                mLocation.getLatitude(),
                mLocation.getLongitude(),
                mTvPlace.getText().toString(),
                mPost.getCategories(),
                mPost.getAmount(),
                mPost.getTime()
        );
        ApiManager.getInstance().getPostService().updatePost(mToken, mPost.getId(), request).enqueue(new RestCallback<BaseResponse>() {
            @Override
            public void success(BaseResponse res) {
                finish();
            }

            @Override
            public void failure(RestError error) {
                Toast.makeText(EditPostActivity.this, error.message, Toast.LENGTH_SHORT).show();
            }
        });
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
        if(resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case REQUEST_CODE_SELECT_CATEGORY:
                if(data == null) return;
                try {
                    String selectedCategories = data.getStringExtra(ARG_KEY_SELECTED_CATEGORIES);
                    List<Category> selected = Arrays.asList(new Gson().fromJson(selectedCategories, Category[].class));
                    if(mPost.getCategories() == null) {
                        mPost.setCategories(new ArrayList<Category>());
                    }
                    mPost.getCategories().clear();
                    mPost.getCategories().addAll(selected);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                updateCategory();
                break;
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                final Place place = PlacePicker.getPlace(data, this);

                if (place != null) {
                    mPlaceLayout.setVisibility(View.VISIBLE);
                    mTvPlace.setText(place.getName().toString());
                    mLocation.setLatitude(place.getLatLng().latitude);
                    mLocation.setLongitude(place.getLatLng().longitude);
                }
                break;
            case REQUEST_CODE_TAKE_PICTURE:
                if(mFile == null) return;
                mImageAdapter.addData(mImageUri);
                updateImages();
                break;
            case REQUEST_CODE_GET_IMAGE:
                if (data == null || data.getData() == null)
                    return;
                mFile = new File(FileUtils.getPath(this, data.getData()));
                mImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", mFile);
                mImageAdapter.addData(mImageUri);
                updateImages();
                break;
        }
    }

    private class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

        public CategoryAdapter() {
            super(R.layout.layout_category_item, new ArrayList<Category>());
        }

        @Override
        protected void convert(BaseViewHolder helper, Category item) {
            helper.setText(R.id.txtCategoryName, item.getName());
        }
    }

    private class ImageAdapter extends BaseQuickAdapter<Uri, BaseViewHolder> {

        public ImageAdapter() {
            super(R.layout.layout_image_item, new ArrayList<Uri>());
        }

        @Override
        protected void convert(BaseViewHolder helper, Uri item) {
            ImageView imageView = helper.getView(R.id.imgPost);
            Glide.with(getApplicationContext()).load(item).into(imageView);

            helper.addOnClickListener(R.id.imgDelete);
        }
    }
}
