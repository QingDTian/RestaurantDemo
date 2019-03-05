package myapp.res.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import myapp.res.CommonCallBack.CommonCallBack;
import myapp.res.bean.User;
import myapp.res.biz.UserBiz;

public class RegisterActivity extends BasicActivity {
    private EditText et_register_username;
    private EditText et_register_passwoed;
    private EditText et_register_repeat_word;
    private Button   btn_register;
     String username;
     String password;
     String repeatPassword;
     private UserBiz userBiz=new UserBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        requestInternet();
        initView();
        initEvent();
    }
    private void requestInternet(){
        if (PermissionsUtil.hasPermission(this, Manifest.permission.INTERNET)) {
            Toast.makeText(RegisterActivity.this,"网络权限已开启",Toast.LENGTH_SHORT).show();
        }else {
        PermissionsUtil.requestPermission(this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                Toast.makeText(RegisterActivity.this,"授权成功",Toast.LENGTH_SHORT).show();
                //initEvent();
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {
                Toast.makeText(RegisterActivity.this,"权限被拒",Toast.LENGTH_SHORT).show();
            }
        });
        }
    }

    private void initView() {
        et_register_username=findViewById(R.id.et_register_username);
        et_register_passwoed=findViewById(R.id.et_register_password);
        et_register_repeat_word=findViewById(R.id.et_repeat_password);
        btn_register=findViewById(R.id.btn_register);
    }
    private void initEvent() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=et_register_username.getText().toString();
                password= et_register_passwoed.getText().toString();
                repeatPassword=et_register_repeat_word.getText().toString();
                if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(repeatPassword)){
                    Toast.makeText(RegisterActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();

                }else if(!password.equals(repeatPassword)){
                Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();}
                else {
                    startDialog();
                    userBiz.register_biz(username, password, new CommonCallBack<User>() {
                        @Override
                        public void onError(Exception e) {
                            stopDialog();
                            Toast.makeText(RegisterActivity.this, e.getMessage()+"注册失败",
                                    Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(User response) {
                            stopDialog();
                            Toast.makeText(RegisterActivity.this,
                                    response.getUsername()+"注册成功",Toast.LENGTH_SHORT).show();
                            LoginActivity.launchLoginActivity(response,RegisterActivity.this);
                        }
                    });
                }

            }
        });
    }

}
