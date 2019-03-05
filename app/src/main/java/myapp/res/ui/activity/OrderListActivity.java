package myapp.res.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import myapp.res.CommonCallBack.CommonCallBack;
import myapp.res.UserInfoHolder;
import myapp.res.adapt.OrdertAdapt;
import myapp.res.bean.Order;
import myapp.res.bean.User;
import myapp.res.biz.OrderBiz;
import myapp.res.ui.view.refresh.SwipeRefresh;
import myapp.res.ui.view.refresh.SwipeRefreshLayout;
import myapp.res.utils.T;

public class OrderListActivity extends BasicActivity {
    private RecyclerView orderRecyclerView;
    private Button mButton;
    private TextView mtvUserName;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OrdertAdapt ordertAdapt;
    private OrderBiz orderBiz = new OrderBiz();
    private List<Order> mDates = new ArrayList<>();
    private int currentPage = 0;
    private User user = UserInfoHolder.getInstance().getmUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);
        loadDate();
        initView();
        initEvent();

    }

    private void loadDate() {
        if (user != null) {
            T.showToast("已登陆");
            orderBiz.loadOrder(currentPage, new CommonCallBack<List<Order>>() {
                @Override
                public void onError(Exception e) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    T.showToast("加载失败～" + e.getMessage());
                }

                @Override
                public void onResponse(List<Order> response) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    for (Order order : response) {
                        mDates.add(order);
                    }
                    T.showToast("加载成功");
                    ordertAdapt = new OrdertAdapt(mDates, OrderListActivity.this);
                    orderRecyclerView.setLayoutManager(new LinearLayoutManager(OrderListActivity.this));
                    orderRecyclerView.setAdapter(ordertAdapt);
                }
            });
        } else {
            T.showToast("用户未登陆");
            startActivity(new Intent(OrderListActivity.this, LoginActivity.class));
        }
    }

    private void initView() {
        orderRecyclerView = findViewById(R.id.order_recycle_view);
        swipeRefreshLayout = findViewById(R.id.order_refresh);
        mButton = findViewById(R.id.order_btn_diancan);
        mtvUserName = findViewById(R.id.order_tv_username);
        String username = user.getUsername();
        if(username !=null){
            mtvUserName.setText(username);
        }
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

    private void initEvent() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderListActivity.this,ListProductActivity.class));
            }
        });

    }

    private void loadMoreDate() {
        orderBiz.loadOrder(++currentPage, new CommonCallBack<List<Order>>() {
            @Override
            public void onError(Exception e) {
                swipeRefreshLayout.setPullUpRefreshing(false);
                T.showToast("刷新失败" + e.getMessage());
            }

            @Override
            public void onResponse(List<Order> response) {
                swipeRefreshLayout.setPullUpRefreshing(false);
                for (Order order : response) {
                    mDates.add(order);
                }
                ordertAdapt.notifyDataSetChanged();
                if(response.size()>0){
                    T.showToast("刷新成功");
                }
                else {
                    T.showToast("没有订单哩！");
                }
            }
        });
    }
}
