package com.khoaluan.mxhgiaothong.activities.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.ListPostActivity;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.request.LoginUseRequest;
import com.khoaluan.mxhgiaothong.restful.response.UserLoginResponse;

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

        loginWithFacebook();

        loginWithGoogle();

//        ApiManager.getInstance().getPostService().getAllPost().enqueue(new RestCallback<GetAllPostResponse>() {
//            @Override
//            public void success(GetAllPostResponse res) {
//                Log.d("TestApi", res.getPosts().get(0).mPostContent);
//            }
//
//            @Override
//            public void failure(RestError error) {
//                Log.d("TestApi", error.message);
//            }
//        });

    }

    @OnClick(R.id.login_button_front)
    public void ClickLoginFacebook(){
        mLoginButton.performClick();
    }

    @OnClick(R.id.login_button_google_front)
    public void ClickLoginGoogle(){
        signInButton.performClick();
    }

    @OnClick(R.id.tv_register)
    public void Register(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @OnClick(R.id.tv_forgot_pass)
    public void ForgotPassword(){
        startActivity(new Intent(LoginActivity.this,ForgotPassActivity.class));
    }

    @OnClick(R.id.btn_login)
    public void Login(){
        ApiManager.getInstance().getUserService().login(new LoginUseRequest(edtUserName.getText().toString(),edtPassWord.getText().toString())).enqueue(new RestCallback<UserLoginResponse>() {
            @Override
            public void success(UserLoginResponse res) {
                Intent intent = new Intent(LoginActivity.this, ListPostActivity.class);

                SharedPreferences sharedPreferences = getSharedPreferences("data_token", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token",res.getToken());
                editor.apply();

                startActivity(intent);
            }

            @Override
            public void failure(RestError error) {
                Toast.makeText(LoginActivity.this, ""+error.message, Toast.LENGTH_SHORT).show();
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
        if (AccessToken.getCurrentAccessToken() != null) {
            Toast.makeText(this, "đã từng đăng nhập", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Token bằng null, chưa đăng nhập lần nào", Toast.LENGTH_SHORT).show();
        }

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
                finish();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                // Handle exception
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
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
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
