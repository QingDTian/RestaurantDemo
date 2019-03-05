package myapp.res.biz;

import com.zhy.http.okhttp.OkHttpUtils;

import myapp.res.CommonCallBack.CommonCallBack;
import myapp.res.config.Config;
import myapp.res.bean.User;

public class UserBiz {
    public void login_biz(String username, String password, CommonCallBack<User> commonCallBack){
        OkHttpUtils.post().url(Config.baseUrl+"user_login")
                .addParams("username",username)
                .addParams("password",password)
                .tag(this)
                .build()
                .execute(commonCallBack);
    }
    public void register_biz(String username, String password, CommonCallBack<User> commonCallBack){
        OkHttpUtils.post().url(Config.baseUrl+"user_register")
                .addParams("username",username)
                .addParams("password",password)
                .tag(this)
                .build()
                .execute(commonCallBack);
    }
        public void onDestroy(){
        OkHttpUtils.getInstance().cancelTag(this);
        }
}
