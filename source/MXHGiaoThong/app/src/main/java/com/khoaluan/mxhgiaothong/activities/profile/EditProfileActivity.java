package com.khoaluan.mxhgiaothong.activities.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.customView.dialog.ErrorMessageDialogFragment;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.khoaluan.mxhgiaothong.AppConstants.LEFT_BACK;
import static com.khoaluan.mxhgiaothong.activities.post.ListPostActivity.token;

/**
 * Created by HieuMinh on 4/13/2018.
 */

public class EditProfileActivity extends AppCompatActivity{

    @BindView(R.id.topBar)
    TopBarView topBar;
    @BindView(R.id.imvAvatar)
    CircleImageView imvAvatar;
    @BindView(R.id.imvAvatarBlur)
    ImageView imvAvatarBlur;
    @BindView(R.id.edtFullName_edit)
    EditText edtFullName;
    @BindView(R.id.tvDate_edit)
    TextView tvDate;
    @BindView(R.id.tvGender_edit)
    TextView tvGender;
    @BindView(R.id.edtAddress_edit)
    EditText edtAddress;
    @BindView(R.id.edtPassword_edit)
    EditText edtPassword;
    @BindView(R.id.btnEditPassWord)
    ImageView btnEditPass;
    @BindView(R.id.edtNewPass_edit)
    EditText edtNewPass;
    @BindView(R.id.edtConfirmNewPass_edit)
    EditText edtConfirmNewPass;
    @BindView(R.id.lnNewPass)
    LinearLayout lnNewPass;
    @BindView(R.id.tvEmail_edit)
    TextView tvEmail;
    @BindView(R.id.btn_choise_date)
    ImageView btnChoiseDate;
    @BindView(R.id.spiner_gender)
    Spinner spinerGender;

    private User userLogin;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        userLogin = (User) getIntent().getSerializableExtra("userLogin");

        initTopBar();
        initView();
    }

    private void initView() {
        Glide.with(this)
                .load(userLogin.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(imvAvatar);

        Glide.with(this)
                .load(userLogin.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform().transforms(new CenterCrop(),new BlurTransformation(50)))
                .into(imvAvatarBlur);

        edtFullName.setText(userLogin.getFullName());
        tvDate.setText(userLogin.getBirthDate());
        tvGender.setText(userLogin.getGender());
        edtPassword.setText(userLogin.getPassword());
        edtAddress.setText(userLogin.getAddress());
        tvEmail.setText(userLogin.getEmail());

        List<String> listGender = new ArrayList<>();
        listGender.add("Nam");
        listGender.add("Nữ");
        listGender.add("Khác");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listGender);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinerGender.setAdapter(adapter);
        spinerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvGender.setText(spinerGender.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initTopBar() {
        topBar.setTextTitle("Chỉnh sửa thông tin");
        topBar.setImageViewLeft(LEFT_BACK);
        topBar.setTranpatentTopBar();
        topBar.setOnClickListener(new TopBarView.OnItemClickListener() {
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

            }
        });
    }

    @OnClick(R.id.btnEditPassWord)
    public void editPassWord(){
        if(lnNewPass.getVisibility() == View.GONE){
            lnNewPass.setVisibility(View.VISIBLE);
        }else {
            lnNewPass.setVisibility(View.GONE);
            edtPassword.setText("");
            edtNewPass.setText("");
            edtConfirmNewPass.setText("");
        }
    }

    @OnClick(R.id.btn_choise_date)
    public void choiseDate(){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tvDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @OnClick(R.id.btnEditUser)
    public void editUserClick(){
        userLogin.setFullName(edtFullName.getText().toString());
        userLogin.setAddress(edtAddress.getText().toString());
        userLogin.setBirthDate(tvDate.getText().toString());
        userLogin.setGender(tvGender.getText().toString());
        boolean isWrongPass = false;
        if(lnNewPass.getVisibility() == View.VISIBLE){
            if(!Objects.equals(edtPassword.getText().toString(), userLogin.getPassword())){
                Toast.makeText(this, "Mât khẩu hiện tại không đúng!", Toast.LENGTH_SHORT).show();
                isWrongPass = true;
            } else if(!Objects.equals(edtNewPass.getText().toString(), edtConfirmNewPass.getText().toString())){
                Toast.makeText(this, "Xác nhận mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                isWrongPass = true;
            }else {
                userLogin.setPassword(edtNewPass.getText().toString());
                isWrongPass = false;
            }
        }
        if(!isWrongPass){
            ApiManager.getInstance().getUserService().editUser(token, userLogin).enqueue(new RestCallback<BaseResponse>() {
                @Override
                public void success(BaseResponse res) {
                    Toast.makeText(EditProfileActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    edtPassword.setText(edtNewPass.getText());
                    lnNewPass.setVisibility(View.GONE);
//                finish();
                }

                @Override
                public void failure(RestError error) {
                    ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                    errorDialog.setError(error.message);
                    errorDialog.show(getSupportFragmentManager(), EditProfileActivity.class.getName());
                }
            });
        }
    }
}
