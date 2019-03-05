package myapp.res.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import myapp.res.bean.Order;
import myapp.res.bean.Product;
import myapp.res.config.Config;
import myapp.res.utils.T;

public class OrderDetailActivity extends BasicActivity {
    public static final String KEY = "key_order";
    private ImageView mImage;
    private TextView tvPrice;
    private TextView tvDescrib;
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
         initIntent();
        initView();

    }

    private void initView() {
        mImage=findViewById(R.id.order_detail_rest_view);
        tvPrice=findViewById(R.id.tv_order_detail_price);
        tvDescrib=findViewById(R.id.tv_order_detail_description);
        tvPrice.setText("共消费："+ mOrder.getPrice()+"元");
        Picasso.with(this)
                .load(Config.baseUrl+ mOrder.getRestaurant().getIcon())
                .placeholder(R.mipmap.splash)
                .into(mImage);
//        HashMap<Product,Integer> map= mOrder.productIntegerHashMap;
//        StringBuffer sb=new StringBuffer();
//        for (Product product:map.keySet()) {
//            sb.append(product.getName()).append("*"+map.get(product)).append("\n");
//        }
        List<Order.PS> psList=mOrder.getPs();
        StringBuffer sb=new StringBuffer();
        for (Order.PS ps:psList) {
            sb.append(ps.product.getName()).append("*"+ps.count).append("\n");
        }
        tvDescrib.setText(sb.toString());


    }
    private void initIntent(){
        Intent intent=getIntent();
        if(intent!=null){
            Log.e("xxx","inteng不为空");
            mOrder= (Order) intent.getSerializableExtra(KEY);
            if( mOrder==null){
                T.showToast("订单信息加载失败");
                Log.e("xxx","order不为空");
                return;
            }
            Log.e("xxx","order不为空");
        }
    }

    public static void startOrderDetailActivity(Context context,Order order){
        Intent intent=new Intent(context,OrderDetailActivity.class);
        intent.putExtra(KEY,order);
        context.startActivity(intent);
    }
}
