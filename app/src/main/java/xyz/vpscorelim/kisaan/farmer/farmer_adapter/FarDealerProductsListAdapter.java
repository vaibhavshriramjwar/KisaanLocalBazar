package xyz.vpscorelim.kisaan.farmer.farmer_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.Adapter_Farmer_His_products;
import xyz.vpscorelim.kisaan.customer.Vegetable_Details;
import xyz.vpscorelim.kisaan.dealer.DealerModel.DealerProductListModel;
import xyz.vpscorelim.kisaan.dealer.DealerMyProduct;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;
import xyz.vpscorelim.kisaan.farmer.ProductDetailsPage;

public class FarDealerProductsListAdapter extends RecyclerView.Adapter<FarDealerProductsListAdapter.MyViewHolder> {

    Context mContext;
    List<DealerProductListModel> mData ;

    public FarDealerProductsListAdapter(Context mContext, List<DealerProductListModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public FarDealerProductsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.farmer_dealer_product_list_layout,parent,false);
        return new FarDealerProductsListAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull FarDealerProductsListAdapter.MyViewHolder holder, int position) {

        holder.fa_product_name.setText(mData.get(position).getProduct_Name());
        holder.fa_product_brand.setText(mData.get(position).getProduct_Brand());
        holder.fa_product_price.setText(mData.get(position).getProduct_rate());
        holder.fa_product_unit.setText(mData.get(position).getProduct_Unit());
        Glide.with(mContext).load(mData.get(position).getProduct_image()).into(holder.fa_product_image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView fa_product_image;
        TextView fa_product_name,fa_product_brand;
        TextView  fa_product_price;
        TextView  fa_product_unit;

        public MyViewHolder(View itemView) {
            super(itemView);


            fa_product_image = itemView.findViewById(R.id.product_img);
            fa_product_name  = itemView.findViewById(R.id.product_name);
            fa_product_brand  = itemView.findViewById(R.id.product_brand);
            fa_product_unit  = itemView.findViewById(R.id.product_price_unit);
            fa_product_price = itemView.findViewById(R.id.price);





            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext, ProductDetailsPage.class);
                    int position = getAdapterPosition();
                    postDetailActivity.putExtra("product_brand",mData.get(position).getProduct_Brand());

                    postDetailActivity.putExtra("product_name",mData.get(position).getProduct_Name());
                    postDetailActivity.putExtra("product_img",mData.get(position).getProduct_image());
                    postDetailActivity.putExtra("product_price",mData.get(position).getProduct_rate());
                    postDetailActivity.putExtra("product_unit",mData.get(position).getProduct_Unit());
                    postDetailActivity.putExtra("product_id",mData.get(position).getProduct_Id());
                    postDetailActivity.putExtra("dealer_id",mData.get(position).getUid());
                    postDetailActivity.putExtra("product_category",mData.get(position).getProduct_Category());
                    mContext.startActivity(postDetailActivity);
                    Animatoo.animateZoom(mContext);



                }
            });


        }


    }




}
