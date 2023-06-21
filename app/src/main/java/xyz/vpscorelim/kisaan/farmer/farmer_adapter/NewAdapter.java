package xyz.vpscorelim.kisaan.farmer.farmer_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.dealer.DealerModel.DealerOrderListModel;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.MyViewHolder> {

    Context mContext;
    List<DealerOrderListModel> mData;
    String myUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public NewAdapter(Context mContext, List<DealerOrderListModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.order_detail_layout, parent, false);
        return new NewAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.productName.setText(mData.get(position).getProductName());
        holder.product_quantity.setText(mData.get(position).getQuantity());
        holder.product_unit.setText(mData.get(position).getUnit());
        holder.prices.setText(mData.get(position).getPrice());
        holder.brand.setVisibility(View.GONE);
        Glide.with(mContext).load(mData.get(position).getProductImage()).into(holder.product_img);

        Toast.makeText(mContext, "" + mData.get(position).getPrice(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productName, product_quantity, product_unit, prices,brand;
        public ImageView product_img;

        public MyViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            product_quantity = itemView.findViewById(R.id.product_quantity);
            product_unit = itemView.findViewById(R.id.product_unit);
            prices = itemView.findViewById(R.id.price);
            product_img = itemView.findViewById(R.id.product_img);
            brand = itemView.findViewById(R.id.product_brand);


            //fa_more          = itemView.findViewById(R.id.more);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent postDetailActivity = new Intent(mContext, Vegetable_Details.class);
//                    int position = getAdapterPosition();
//
//                    postDetailActivity.putExtra("vegetable_name",mData.get(position).getProduct_Name());
//                    postDetailActivity.putExtra("vegetable_img",mData.get(position).getProduct_image());
//                    postDetailActivity.putExtra("vegetable_price",mData.get(position).getProduct_rate());
//                    postDetailActivity.putExtra("vegetable_unit",mData.get(position).getProduct_Unit());
//                    postDetailActivity.putExtra("vegetable_id",mData.get(position).getProduct_Id());
//                    postDetailActivity.putExtra("far_id",mData.get(position).getUid());
//                    postDetailActivity.putExtra("vegetable_category",mData.get(position).getProduct_Category());
//                    // will fix this later i forgot to add user name to post object
//                    //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
//                    //Long timestamp  = (Long) mData.get(position).getTimeStamp();
//                    //postDetailActivity.putExtra("postDate",timestamp) ;
//                    mContext.startActivity(postDetailActivity);


        }


    }

}

