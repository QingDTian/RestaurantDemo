package myapp.res.biz;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.Serializable;
import java.util.List;

import myapp.res.CommonCallBack.CommonCallBack;
import myapp.res.config.Config;
import myapp.res.bean.Product;

public class ProductBiz implements Serializable {
    public void loadProduct(int currentPage, CommonCallBack<List<Product>> commonCallBack){
        OkHttpUtils.post()
                .url(Config.baseUrl+"product_find")
                .addParams("currentPage",currentPage+"")
                .tag(this)
                .build()
                .execute(commonCallBack);
    }

}
