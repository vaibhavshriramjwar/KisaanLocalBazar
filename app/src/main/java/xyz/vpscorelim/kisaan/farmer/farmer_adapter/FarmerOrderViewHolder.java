package xyz.vpscorelim.kisaan.farmer.farmer_adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderDetails;
import xyz.vpscorelim.kisaan.farmer.Farmer_Order_Status;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class FarmerOrderViewHolder extends RecyclerView.Adapter<FarmerOrderViewHolder.MyViewHolder> implements View.OnLongClickListener {

    Context mContext;
    List<CustomerRequest> mData ;


    public FarmerOrderViewHolder (Context mContext, List<CustomerRequest> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public FarmerOrderViewHolder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.farmer_orders_list,parent,false);
        return new FarmerOrderViewHolder.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerOrderViewHolder.MyViewHolder holder, final int position) {

        CustomerRequest request;


        holder.txtOrderName.setText(mData.get(position).getOrder_id());
        holder.txtOrderStatus.setText(mData.get(position).getStatus());
        holder.txtPhone.setText(mData.get(position).getPhone());
        holder.txtOrderAddress.setText(mData.get(position).getAddress());


        holder.take_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetail = new Intent(mContext, FarmerOrderDetails.class);
                            orderDetail.putExtra("OrderId",mData.get(position).getOrder_id());
                            orderDetail.putExtra("phone",mData.get(position).getPhone());
                            mContext.startActivity(orderDetail);
                Animatoo.animateZoom(mContext);
            }
        });




//        holder.take_action.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setItems(new String[]{"View Order","Update Order Status", "Delete Order"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if(which==0){
//                            //View Order Clicked
//                            Intent orderDetail = new Intent(mContext, FarmerOrderDetails.class);
//                            orderDetail.putExtra("OrderId",mData.get(position).getOrder_id());
//                            orderDetail.putExtra("phone",mData.get(position).getPhone());
//                            mContext.startActivity(orderDetail);
//
//                        }
//                        if(which==1){
//                            //Update Order Status
//
//
//                        }
//                        if(which==2){
//                            //Delete Order
//
//                        }
//
//
//                    }
//                });
//                builder.create().show();
//            }
//        });









    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(mContext, "Long Click", Toast.LENGTH_SHORT).show();
        return false;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtOrderName,txtOrderStatus,txtOrderAddress,txtPhone;
        MaterialButton take_action;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderName        = itemView.findViewById(R.id.order_name);
            txtOrderStatus      = itemView.findViewById(R.id.order_status);
            txtOrderAddress     = itemView.findViewById(R.id.order_address);
            txtPhone            = itemView.findViewById(R.id.order_phone);
            take_action         = itemView.findViewById(R.id.take_action);


        }
    }
}
