package myapp.res.CommonCallBack;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import myapp.res.utils.GsonUtil;
import okhttp3.Call;

public abstract class CommonCallBack<T> extends StringCallback {
     Type mType;
    public CommonCallBack() {
        mType = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
    onError(e);
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            JSONObject json=new JSONObject(response);
            int resultCode=json.getInt("resultCode");
            if(resultCode==1){
                //Gson gson=new Gson();
                onResponse((T) GsonUtil.getGson().fromJson(json.getString("data"), mType));
            }else {
                onError(new RuntimeException(json.getString("resultMessage")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onError(e);
        }
    }
    public abstract void onError(Exception e);
    public abstract void onResponse(T response);
}
