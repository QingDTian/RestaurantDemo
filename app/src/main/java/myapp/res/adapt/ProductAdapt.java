package myapp.res.adapt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import myapp.res.bean.Product;
import myapp.res.bean.ProductItem;
import myapp.res.config.Config;
import myapp.res.utils.T;

public class ProductAdapt extends RecyclerView.Adapter<ProductAdapt.ItemViewHolder> {
    private Context mContext;
    private List<ProductItem> mProductItems;
    private LayoutInflater mLayoutInflater;
    private ProductItem productItem;

    public ProductAdapt(Context context, List<ProductItem> productItems) {
        mContext = context;
        mProductItems = productItems;
        mLayoutInflater = LayoutInflater.from(context);

    }
    public interface ProductAdaptInterface{
        void add(ProductItem productItem);
        void sub(ProductItem productItem);
    }
    private ProductAdaptInterface mProductAdaptInterface;
    public void setProductAdaptInterface(ProductAdaptInterface productAdaptInterface){
        mProductAdaptInterface=productAdaptInterface;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.item_product,parent,false);//还有第三个布尔参数
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ProductItem productItem=mProductItems.get(position);
        Picasso.with(mContext)
                .load(Config.baseUrl+productItem.getIcon())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.mIvImage);
        holder.mTvCount.setText(""+productItem.getCount());//
        holder.mTvLabel.setText(productItem.getDescription());
        holder.mTvName.setText(productItem.getName());
        holder.mTvPrice.setText(productItem.getPrice()+"元/份");

    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;
        public ImageView mIvAdd;
        public ImageView mIvSub;
        public TextView mTvCount;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mIvImage = itemView.findViewById(R.id.item_image);
            mTvName = itemView.findViewById(R.id.item_product_name);
            mTvLabel = itemView.findViewById(R.id.item_product_description);
            mTvPrice = itemView.findViewById(R.id.item_product_price);
            mIvAdd = itemView.findViewById(R.id.item_add);
            mIvSub = itemView.findViewById(R.id.item_sub);
            mTvCount = itemView.findViewById(R.id.item_tv_product_count);
            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position= getLayoutPosition();
                      productItem=mProductItems.get(position);
                    int count = productItem.getCount();
                    productItem.setCount(++count);//不能用count++
                   mTvCount.setText(""+productItem.getCount());//不通过适配器 直接更改UI
                    // 但如果不在适配器再写遍该方法，那么重写走生命周期渲染布局时UI控件值不会改变，还是原来的
                    if(mProductAdaptInterface!=null){
                        mProductAdaptInterface.add(productItem);
                    }
                }
            });
            mIvSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position= getLayoutPosition();
                     productItem=mProductItems.get(position);
                    int count = productItem.getCount();
                    if( count>0){
                        if(mProductAdaptInterface!=null){
                            mProductAdaptInterface.sub(productItem);
                        }
                        productItem.setCount(count-1);
                        mTvCount.setText(productItem.getCount()+"");

                    }
                    if(count==0){
                        T.showToast("已经是0啦！");
                    }

                }
            });
        }

    }
}
