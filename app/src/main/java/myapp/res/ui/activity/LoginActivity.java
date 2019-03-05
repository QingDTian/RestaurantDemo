package myapp.res.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import myapp.res.CommonCallBack.CommonCallBack;
import myapp.res.UserInfoHolder;
import myapp.res.bean.User;
import myapp.res.biz.UserBiz;

public class LoginActivity extends BasicActivity {
    public static final String KEY_USER = "key_user";
    public static final String TAG = "merroe";
    public Button button;
    public EditText et_username;
    public EditText et_password;
    public TextView tx_register;
    public String userName;
    public String password;
    private UserBiz userBiz = new UserBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initIntent();
        initEvent();

    }


     @Override
    protected void onResume() {
        super.onResume();
        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    private void startActivity() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = et_username.getText().toString();
                password = et_password.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    startDialog();
                    userBiz.login_biz(userName, password, new CommonCallBack<User>() {

                        @Override
                        public void onError(Exception e) {
                            stopDialog();
                            Log.i(TAG,e.getMessage());
                            System.out.print(e.getStackTrace());
                            System.out.print(e.getMessage());
                            Toast.makeText(LoginActivity.this, e.getMessage() + "登陆失败", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onResponse(User response) {
                            stopDialog();
                            UserInfoHolder.getInstance().setmUser(response);
                            Toast.makeText(LoginActivity.this,
                                    response.getUsername() + response.getUsername()+"登陆成功～", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(LoginActivity.this,ListProductActivity.class));
                            finish();

                        }
                    });
                }
            }
        });

        tx_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });

    }

    private void initView() {
        button = findViewById(R.id.btn_login);
        et_username = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        tx_register = findViewById(R.id.tv_register);
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            User u = (User) intent.getSerializableExtra(KEY_USER);
            if (u != null) {
                et_password.setText(u.getPassword());
                et_username.setText(u.getUsername());
            }
        }

    }

    public static void launchLoginActivity(User user, Context context) {
        Intent intent = new Intent(context,LoginActivity.class);
        intent.putExtra(KEY_USER, user);
        context.startActivity(intent);
    }
}
