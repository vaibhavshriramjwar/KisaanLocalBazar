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

import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.OrderViewHolder;
import xyz.vpscorelim.kisaan.customer.CustomerOrderDetails;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.farmer.FarmerOwnOrderDeatils;
import xyz.vpscorelim.kisaan.farmer.farmer_Model.FarmerRequest;

public class FarmerOwnOrderViewHolder extends RecyclerView.Adapter<FarmerOwnOrderViewHolder.MyViewHolder> {

    Context mContext;
    List<FarmerRequest> mData ;

    public FarmerOwnOrderViewHolder(Context mContext, List<FarmerRequest> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public FarmerOwnOrderViewHolder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.farmer_own_order_list_layout,parent,false);
        return new FarmerOwnOrderViewHolder.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerOwnOrderViewHolder.MyViewHolder holder, int position) {

        CustomerRequest request;

        holder.txtOrderName.setText(mData.get(position).getOrder_id());
        holder.txtOrderStatus.setText(mData.get(position).getStatus());
        holder.viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FarmerOwnOrderDeatils.class);
                intent.putExtra("OrderId",mData.get(position).getOrder_id());
                intent.putExtra("phone",mData.get(position).getPhone());
                intent.putExtra("dealer_id",mData.get(position).getDealer_id());
                mContext.startActivity(intent);
                Animatoo.animateZoom(mContext);
            }
        });
//        holder.txtPhone.setText(mData.get(position).getPhone());
//        holder.txtOrderAddress.setText(mData.get(position).getAddress());

    }

    private String covertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "On My way";
        else
            return "Shipped";
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
