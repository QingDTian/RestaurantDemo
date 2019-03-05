package myapp.res.biz;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

import myapp.res.CommonCallBack.CommonCallBack;
import myapp.res.config.Config;
import myapp.res.bean.Order;
import myapp.res.bean.Product;

public class OrderBiz {
    public void loadOrder(int currentPage, CommonCallBack<List<Order>> commonCallBack) {
        OkHttpUtils.post()
                .url(Config.baseUrl + "order_find")
                .addParams("currentPage", currentPage + "")
                .tag(this)
                .build()
                .execute(commonCallBack);
    }

    public void addOrder(Order order, CommonCallBack commonCallBack) {
        StringBuilder sb;
        sb = new StringBuilder();
        Map<Product,Integer> map=order.productIntegerHashMap;
        for (Product product : order.productIntegerHashMap.keySet()) {
            sb.append(product.getId()+"_"+map.get(product));
            sb.append("|");
        }
        sb = sb.deleteCharAt(sb.length() - 1);
        OkHttpUtils.post()
                .url(Config.baseUrl + "order_add")
                .addParams("res_id", order.getRestaurant().getId() + "")
                .addParams("product_str", sb.toString())
                .addParams("count", order.getCount() + "")
                .addParams("price", order.getPrice() + "")
                .tag(this)
                .build()
                .execute(commonCallBack);

    }
}
