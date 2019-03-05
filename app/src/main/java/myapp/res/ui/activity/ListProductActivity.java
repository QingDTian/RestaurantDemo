package myapp.res.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import myapp.res.CommonCallBack.CommonCallBack;
import myapp.res.adapt.ProductAdapt;
import myapp.res.bean.Order;
import myapp.res.bean.Product;
import myapp.res.bean.ProductItem;
import myapp.res.biz.OrderBiz;
import myapp.res.biz.ProductBiz;
import myapp.res.ui.view.refresh.SwipeRefresh;
import myapp.res.ui.view.refresh.SwipeRefreshLayout;
import myapp.res.utils.T;


public class ListProductActivity extends BasicActivity {
    private RecyclerView recyclerView;
    private Button btn_pay;
    private TextView tv_total_count;
    private ProductBiz productBiz = new ProductBiz();
    private OrderBiz orderBiz = new OrderBiz();
    private Order mOrder;// = new Order();//
    public ProductAdapt productAdapt;
    private List<ProductItem> mDates = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPage = 0;
    private int totalCount = 0;
    private float totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        setTitle("点餐");
        loadDate();
        initView();
        initEvent();

    }




    private void loadDate() {
        startDialog();
        productBiz.loadProduct(currentPage, new CommonCallBack<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopDialog();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                T.showToast("加载失败" + e.getMessage());

            }

            @Override
            public void onResponse(List<Product> response) {
                stopDialog();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                T.showToast("加载成功");
                mDates.clear();
                for (Product product : response) {
                    mDates.add(new ProductItem(product));
                }
                productAdapt.notifyDataSetChanged();


            }
        });
    }


    private void initView() {
        btn_pay = findViewById(R.id.btn_pay);
        tv_total_count = findViewById(R.id.tv_count);
        recyclerView = findViewById(R.id.id_recycle_view);
        mOrder=new Order();

        productAdapt = new ProductAdapt(ListProductActivity.this, mDates);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListProductActivity.this));
        recyclerView.setAdapter(productAdapt);
        productAdapt.setProductAdaptInterface(new ProductAdapt.ProductAdaptInterface() {
            @Override
            public void add(ProductItem productItem) {
                ++totalCount;
                totalPrice=totalPrice+productItem.getPrice();
                mOrder.addProduct(productItem);
                tv_total_count.setText("数量："+totalCount);
                btn_pay.setText("总价："+totalPrice+" pay");

            }

            @Override
            public void sub(ProductItem productItem) {
                --totalCount;
                totalPrice=totalPrice-productItem.getPrice();
                mOrder.removeProduct(productItem);
                tv_total_count.setText("数量："+totalCount);
                btn_pay.setText("总价："+totalPrice+" pay");


            }
        });


        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.BLUE, Color.GRAY);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDate();
            }
        });
        swipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMoreDate();
            }
        });


    }

    private void loadMoreDate() {
        productBiz.loadProduct(currentPage++, new CommonCallBack<List<Product>>() {
            @Override
            public void onError(Exception e) {
                swipeRefreshLayout.setPullUpRefreshing(false);
                currentPage--;
                T.showToast("加载失败～");
            }

            @Override
            public void onResponse(List<Product> response) {
                swipeRefreshLayout.setPullUpRefreshing(false);
                int size = response.size();
                if (size == 0) {
                    T.showToast("木有菜了～");
                }
                if (size > 0) {
                    T.showToast("又来" + size + "道菜");
                }
                for (Product product : response) {
                    mDates.add(new ProductItem(product));
                }
                productAdapt.notifyDataSetChanged();
            }
        });
    }

    private void initEvent() {
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder.setCount(totalCount);
                mOrder.setPrice(totalPrice);
                mOrder.setRestaurant(mDates.get(0).getRestaurant());
                if(totalCount==0){
                    T.showToast("您还未选择商品～");
                }else {
                    startDialog();
                    orderBiz.addOrder(mOrder, new CommonCallBack<String>() {
                        @Override
                        public void onError(Exception e) {
                            stopDialog();
                            T.showToast("生成订单失败"+e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            stopDialog();
                            T.showToast("生成订单成功～");
                            startActivity(new Intent(ListProductActivity.this,OrderListActivity.class));
                            finish();
                        }
                    });
                }
            }
        });

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        //TO DO
//        //productBiz.
//    }
}

