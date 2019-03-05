package myapp.res;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import myapp.res.utils.SPUtils;
import myapp.res.utils.T;
import okhttp3.OkHttpClient;

public class ResApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        T.init(this);
        SPUtils.init(this,"sp_res_pref");
        CookieJarImpl cookieJar=new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient= new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10, TimeUnit.SECONDS)
                .cookieJar(cookieJar).readTimeout(10,TimeUnit.SECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }
}
