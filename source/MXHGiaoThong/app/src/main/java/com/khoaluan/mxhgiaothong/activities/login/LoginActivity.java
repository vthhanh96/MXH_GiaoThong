package com.khoaluan.mxhgiaothong.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.khoaluan.mxhgiaothong.PreferManager;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.main.MainActivity;
import com.khoaluan.mxhgiaothong.activities.post.ListPostActivity;
import com.khoaluan.mxhgiaothong.customView.dialog.ErrorMessageDialogFragment;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.model.User;
import com.khoaluan.mxhgiaothong.restful.request.LoginUseRequest;
import com.khoaluan.mxhgiaothong.restful.response.GetUserInfoResponse;
import com.khoaluan.mxhgiaothong.restful.response.UserLoginResponse;
import com.khoaluan.mxhgiaothong.utils.FontHelper;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final int RC_SIGN_IN = 2018;
    private static final String TAG = "NETFIC_TAG";

    private CallbackManager mCallbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    @BindView(R.id.login_button_google_back)
    SignInButton signInButton;

    @BindView(R.id.login_button_google_front)
    Button loginGoogleFront;

    @BindView(R.id.login_button_front)
    Button loginFBFront;

    @BindView(R.id.login_button_back)
    LoginButton mLoginButton;

    @BindView(R.id.tv_forgot_pass)
    TextView tvForgotPass;

    @BindView(R.id.tv_register)
    TextView tvRegister;

    @BindView(R.id.edt_username)
    TextView edtUserName;

    @BindView(R.id.edt_password)
    TextView edtPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        tvForgotPass.setTypeface(FontHelper.getInstance().getTypeface(this, FontHelper.FONT_QUICKSAND_BOLD));
        tvRegister.setTypeface(FontHelper.getInstance().getTypeface(this, FontHelper.FONT_QUICKSAND_BOLD));
        edtUserName.setTypeface(FontHelper.getInstance().getTypeface(this, FontHelper.FONT_QUICKSAND_BOLD));
        edtPassWord.setTypeface(FontHelper.getInstance().getTypeface(this, FontHelper.FONT_QUICKSAND_BOLD));
        loginWithFacebook();

        loginWithGoogle();

    }

    @OnClick(R.id.login_button_front)
    public void ClickLoginFacebook() {
        mLoginButton.performClick();
    }

    @OnClick(R.id.login_button_google_front)
    public void ClickLoginGoogle() {
        signInButton.performClick();
    }

    @OnClick(R.id.tv_register)
    public void Register() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @OnClick(R.id.tv_forgot_pass)
    public void ForgotPassword() {
        startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
    }

    @OnClick(R.id.btn_login)
    public void Login() {
        ApiManager.getInstance().getUserService().login(new LoginUseRequest(edtUserName.getText().toString(), edtPassWord.getText().toString())).enqueue(new RestCallback<UserLoginResponse>() {
            @Override
            public void success(UserLoginResponse res) {
                PreferManager.getInstance(LoginActivity.this).saveToken(res.getToken());
                PreferManager.getInstance(LoginActivity.this).saveUserId(res.getId());
                PreferManager.getInstance(LoginActivity.this).clearValue();
                getUserInfo(res.getToken());
            }

            @Override
            public void failure(RestError error) {
                ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                errorDialog.setError(error.message);
                errorDialog.show(getSupportFragmentManager(), LoginActivity.class.getName());
            }
        });
    }

    private void getUserInfo(String token) {
        ApiManager.getInstance().getUserService().getUserInfo(token).enqueue(new RestCallback<GetUserInfoResponse>() {
            @Override
            public void success(GetUserInfoResponse res) {
                PreferManager.getInstance(LoginActivity.this).saveUser(res.getUser());
                MainActivity.start(LoginActivity.this);
            }

            @Override
            public void failure(RestError error) {
                ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                errorDialog.setError(error.message);
                errorDialog.show(getSupportFragmentManager(), LoginActivity.class.getName());
            }
        });
    }

    private void loginWithGoogle() {
        signInButton = findViewById(R.id.login_button_google_back);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        loginGoogleFront = findViewById(R.id.login_button_google_front);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void loginWithFacebook() {
        AccessToken.setCurrentAccessToken(null);

        mCallbackManager = CallbackManager.Factory.create();

        loginFBFront = findViewById(R.id.login_button_front);
        mLoginButton = findViewById(R.id.login_button_back);

        // Set the initial permissions to request from the user while logging in
        mLoginButton.setReadPermissions(Arrays.asList(EMAIL, USER_POSTS));
        // Register a callback to respond to the user
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    String gender = me.optString("gender");
                                    String fullname = me.optString("name");
                                    String birthday = me.optString("birthday");
                                    String id = me.optString("id");
                                    String email = id;
                                    String address = me.optString("address");
                                    String image_url = "http://graph.facebook.com/" + id + "/picture?type=large";

                                    User user = new User.Builder()
                                            .setAvatar(image_url)
                                            .setBirthday(birthday)
                                            .setFullName(fullname)
                                            .setGender(gender)
                                            .setAddress(address)
                                            .build();

                                    ApiManager.getInstance().getUserService().loginFacebookGoogle(user).enqueue(new RestCallback<UserLoginResponse>() {
                                        @Override
                                        public void success(UserLoginResponse res) {
                                            PreferManager.getInstance(LoginActivity.this).saveToken(res.getToken());
                                            PreferManager.getInstance(LoginActivity.this).saveUserId(res.getId());
                                            getUserInfo(res.getToken());
                                        }

                                        @Override
                                        public void failure(RestError error) {
                                            ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                                            errorDialog.setError(error.message);
                                            errorDialog.show(getSupportFragmentManager(), LoginActivity.class.getName());
                                        }
                                    });
                                }
                            }
                        }).executeAsync();
//                finish();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
                errorDialog.setError(e.getMessage());
                errorDialog.show(getSupportFragmentManager(), LoginActivity.class.getName());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

//            updateUI(account);
            Toast.makeText(this, "account : " + account.getEmail(), Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();
            errorDialog.setError(e.getMessage());
            errorDialog.show(getSupportFragmentManager(), LoginActivity.class.getName());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
//        Toast.makeText(this, "account : " +account.getDisplayName(), Toast.LENGTH_SHORT).show();
    }
}
