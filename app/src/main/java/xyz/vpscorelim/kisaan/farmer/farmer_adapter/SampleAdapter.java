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
import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderModel;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.MyViewHolder> {

    Context mContext;
    List<FarmerOrderModel> mData;
    String myUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    public SampleAdapter(Context mContext, List<FarmerOrderModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public SampleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.order_detail_layout, parent, false);
        return new SampleAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull SampleAdapter.MyViewHolder holder, int position) {

        holder.productName.setText(mData.get(position).getProductName());
        holder.product_quantity.setText(mData.get(position).getQuantity());
        holder.product_unit.setText(mData.get(position).getUnit());
        holder.prices.setText(mData.get(position).getPrice());
        holder.brand.setText(mData.get(position).getProductBrand());
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
            brand  = itemView.findViewById(R.id.product_brand);
            product_img = itemView.findViewById(R.id.product_img);


        }


    }
}