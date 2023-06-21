package xyz.vpscorelim.kisaan.farmer.farmer_adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderDetails;

class MyViewHolder extends RecyclerView.ViewHolder{


    public TextView productName,product_quantity,product_unit,prices;
    public ImageView product_img;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        productName         = itemView.findViewById(R.id.product_name);
        product_quantity    = itemView.findViewById(R.id.product_quantity);
        product_unit        = itemView.findViewById(R.id.product_unit);
        prices              = itemView.findViewById(R.id.price);
        product_img         = itemView.findViewById(R.id.product_img);


    }
}


public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

    List<CustomerOrderModel> orderModels = new ArrayList<>();
    Context mContext;
    List<CustomerOrderModel> customerRequest;



    public OrderDetailAdapter(FarmerOrderDetails farmerOrderDetails, List<CustomerOrderModel> customerRequest) {
        this.customerRequest=customerRequest;
    }


//    public OrderDetailAdapter(List<CustomerOrderModel> orderModels) {
//        this.orderModels = orderModels;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_layout,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CustomerOrderModel order = orderModels.get(position);
        holder.productName.setText(order.getProductName());
        holder.product_quantity.setText(order.getQuantity());
        holder.product_unit.setText(order.getUnit());
        holder.prices.setText(order.getPrice());
        //Glide.with(mContext).load(order.getProductImage()).into(holder.product_img);


    }

    @Override
    public int getItemCount() {
        return orderModels.size();
    }
}
