package myapp.res;

import android.text.TextUtils;

import myapp.res.bean.User;
import myapp.res.utils.SPUtils;

public class UserInfoHolder {
    private static UserInfoHolder mInstance=new UserInfoHolder();
    private User mUser;
    private static final String KEY_USERNAME="key_user_name";
    public static UserInfoHolder getInstance(){
        return mInstance;
    }

    public User getmUser() {
        User u=mUser;
        if(u==null){
           String username= (String) SPUtils.getInstance().get(KEY_USERNAME,"");
            if(!TextUtils.isEmpty(username)){
                u=new User();
                u.setUsername(username);
            }
        }
        mUser=u;
        return mUser;

    }

    public  void setmUser(User mUser) {
        this.mUser = mUser;
        if(mUser!=null){
            SPUtils.getInstance().put(KEY_USERNAME,mUser.getUsername());
        }
    }
}
