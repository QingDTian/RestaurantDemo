package myapp.res.adapt;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import myapp.res.bean.Order;
import myapp.res.config.Config;
import myapp.res.ui.activity.OrderDetailActivity;

public class OrdertAdapt extends RecyclerView.Adapter<OrdertAdapt.OrderItemViewHolder>{
    private List<Order> mOrders;
    private Context mContext;
    private LayoutInflater layoutInflater;
    public OrdertAdapt(List<Order> orders,Context context) {
        layoutInflater=LayoutInflater.from(context);
       mOrders=orders;
        //mOrders.addAll(orders);//see
        mContext=context;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view=layoutInflater.inflate(R.layout.item_order,parent,false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Order order = mOrders.get(position);
        Picasso.with(mContext)
                .load(Config.baseUrl + order.getRestaurant().getIcon())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mIvImage);
        List<Order.PS> ps = order.getPs();
        if(ps.size()>0) {
            holder.mTvLabel.setText(ps.get(0).product.getName()+"等"+order.getCount()+"件商品");
        }
        holder.mTvPrice.setText("共消费："+order.getPrice()+"元");
        //holder.mTvName.setText(order.getRestaurant().getName()+"");
        holder.mTvName.setText("学院餐厅");
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }


    class OrderItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;

         public OrderItemViewHolder(View itemView) {
             super(itemView);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     Order order = mOrders.get(getLayoutPosition());
                     OrderDetailActivity.startOrderDetailActivity(mContext, order);
                 }
             });
             mIvImage=itemView.findViewById(R.id.item_order_img);
             mTvName=itemView.findViewById(R.id.item_order_name);
             mTvLabel=itemView.findViewById(R.id.item_order_lab);
             mTvPrice=itemView.findViewById(R.id.item_order_price);

         }

     }
}
