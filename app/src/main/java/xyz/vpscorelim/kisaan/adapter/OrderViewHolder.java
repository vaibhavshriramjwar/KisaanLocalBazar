package xyz.vpscorelim.kisaan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import xyz.vpscorelim.kisaan.CheckUserProfile;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.CustomerOrderDetails;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.farmer.Farmer_Home;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class OrderViewHolder extends RecyclerView.Adapter<OrderViewHolder.MyViewHolder> {

    Context mContext;
    List<CustomerRequest> mData ;


    public OrderViewHolder(Context mContext, List<CustomerRequest> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public OrderViewHolder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.order_layout,parent,false);
        return new OrderViewHolder.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder.MyViewHolder holder, int position) {

        CustomerRequest request;


        holder.txtOrderName.setText(mData.get(position).getOrder_id());
        holder.txtOrderStatus.setText(mData.get(position).getStatus());
        holder.viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CustomerOrderDetails.class);
                intent.putExtra("OrderId",mData.get(position).getOrder_id());
                intent.putExtra("phone",mData.get(position).getPhone());
                intent.putExtra("farmer_id",mData.get(position).getFarmer_id());
                mContext.startActivity(intent);
                Animatoo.animateZoom(mContext);
            }
        });
//        holder.txtPhone.setText(mData.get(position).getPhone());
//        holder.txtOrderAddress.setText(mData.get(position).getAddress());

    }



    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderName,txtOrderStatus,txtOrderAddress,txtPhone;


        ImageView viewOrder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderName        = itemView.findViewById(R.id.order_name);
            txtOrderStatus      = itemView.findViewById(R.id.order_status);
            viewOrder           = itemView.findViewById(R.id.view);

//            txtOrderAddress     = itemView.findViewById(R.id.order_address);
//            txtPhone            = itemView.findViewById(R.id.order_phone);




        }
    }
}
