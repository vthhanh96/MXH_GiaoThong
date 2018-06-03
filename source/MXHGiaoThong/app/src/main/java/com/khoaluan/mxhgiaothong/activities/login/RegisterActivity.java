package com.khoaluan.mxhgiaothong.activities.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.UploadImageListener;
import com.khoaluan.mxhgiaothong.customView.dialog.ErrorMessageDialogFragment;
import com.khoaluan.mxhgiaothong.customView.dialog.SelectModeImageDialogFragment;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.request.LoginUseRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.utils.FileUtils;
import com.khoaluan.mxhgiaothong.utils.FontHelper;
import com.khoaluan.mxhgiaothong.utils.PermissionUtils;
import com.khoaluan.mxhgiaothong.utils.UploadImageUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HieuMinh on 3/30/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edt_password)
    EditText edtPassword;

    @BindView(R.id.edt_email)
    EditText edtEmail;

    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;

    @BindView(R.id.edt_full_name)
    EditText edtFullName;

    @BindView(R.id.profile_image)
    CircleImageView profileImage;

    @BindView(R.id.ic_camera)
    ImageView icCamera;

    private String mImageUrl;
    private Context mContext;
    private File mFile;
    private Uri mImageUri;
    private static final int REQUEST_CODE_TAKE_PICTURE = 11;
    private static final int REQUEST_CODE_CAMERA = 12;
    private static final int REQUEST_CODE_GET_IMAGE = 13;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        edtPassword.setTypeface(FontHelper.getInstance().getTypeface(this, FontHelper.FONT_QUICKSAND_BOLD));
        edtEmail.setTypeface(FontHelper.getInstance().getTypeface(this, FontHelper.FONT_QUICKSAND_BOLD));
        edtConfirmPassword.setTypeface(FontHelper.getInstance().getTypeface(this, FontHelper.FONT_QUICKSAND_BOLD));
        edtFullName.setTypeface(FontHelper.getInstance().getTypeface(this, FontHelper.FONT_QUICKSAND_BOLD));
        mContext = this;
    }

    private void uploadImage() {
        UploadImageUtils.uploadImage(mImageUri, new UploadImageListener() {
            @Override
            public void uploadSuccess(String url) {
                mImageUrl = url;
                ApiManager.getInstance().getUserService().register(new LoginUseRequest(edtEmail.getText().toString(),edtPassword.getText().toString(),edtFullName.getText().toString(),mImageUrl)).enqueue(new RestCallback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse res) {
                        Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void failure(RestError error) {
                        ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                        errorDialog.setError(error.message);
                        errorDialog.show(getSupportFragmentManager(), RegisterActivity.class.getName());
                    }
                });
            }

            @Override
            public void uploadFailure(String err) {
                Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.btn_register)
    public void Register(){
        if(!Objects.equals(edtPassword.getText().toString(), edtConfirmPassword.getText().toString())){
            Toast.makeText(this, "Nhập lại mật khẩu không khớp !", Toast.LENGTH_SHORT).show();
        } else {
            if(mImageUri != null){
                uploadImage();
            } else {
                ApiManager.getInstance().getUserService().register(new LoginUseRequest(edtEmail.getText().toString(),edtPassword.getText().toString(),edtFullName.getText().toString(),"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqZJOzhw6-xhnjU2qnCi_U0aEE2u7ngwrCzKJXHuQvMYzxcboY")).enqueue(new RestCallback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse res) {
                        Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void failure(RestError error) {
                        ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();

                        errorDialog.setError(error.message);
                        errorDialog.show(getSupportFragmentManager(), RegisterActivity.class.getName());
                    }
                });
            }
        }
    }

    @OnClick(R.id.profile_image)
    public void takePicture() {
        if (PermissionUtils.checkPermission(mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            openDialog();
        } else {
            PermissionUtils.requestPermission(RegisterActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMERA);
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
        selectModeImageDialogFragment.show(getSupportFragmentManager(),RegisterActivity.this.getClass().getName());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_CODE_TAKE_PICTURE) {
            if(mFile == null) return;
            Glide.with(mContext.getApplicationContext()).load(mFile).into(profileImage);
        } else if(requestCode == REQUEST_CODE_GET_IMAGE) {
            if (data == null || data.getData() == null)
                return;
            Glide.with(mContext.getApplicationContext()).load(data.getData()).into(profileImage);
            mFile = new File(FileUtils.getPath(mContext, data.getData()));
            mImageUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", mFile);
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
}
