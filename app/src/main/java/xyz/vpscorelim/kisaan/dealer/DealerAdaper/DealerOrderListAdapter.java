package xyz.vpscorelim.kisaan.dealer.DealerAdaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.dealer.DealerOrderDetails;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderDetails;
import xyz.vpscorelim.kisaan.farmer.farmer_Model.FarmerRequest;
import xyz.vpscorelim.kisaan.farmer.farmer_adapter.FarmerOrderViewHolder;

public class DealerOrderListAdapter extends RecyclerView.Adapter<DealerOrderListAdapter.MyViewHolder> implements View.OnLongClickListener {

    Context mContext;
    List<FarmerRequest> mData;


    public DealerOrderListAdapter(Context mContext, List<FarmerRequest> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public DealerOrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.farmer_orders_list, parent, false);
        return new DealerOrderListAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull DealerOrderListAdapter.MyViewHolder holder, final int position) {

        CustomerRequest request;


        holder.txtOrderName.setText(mData.get(position).getOrder_id());
        holder.txtOrderStatus.setText(mData.get(position).getStatus());
        holder.txtPhone.setText(mData.get(position).getPhone());
        holder.txtOrderAddress.setText(mData.get(position).getAddress());


        holder.take_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetail = new Intent(mContext, DealerOrderDetails.class);
                orderDetail.putExtra("OrderId", mData.get(position).getOrder_id());
                orderDetail.putExtra("phone", mData.get(position).getPhone());
                mContext.startActivity(orderDetail);
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

        TextView txtOrderName, txtOrderStatus, txtOrderAddress, txtPhone;
        MaterialButton take_action;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtOrderName = itemView.findViewById(R.id.order_name);
            txtOrderStatus = itemView.findViewById(R.id.order_status);
            txtOrderAddress = itemView.findViewById(R.id.order_address);
            txtPhone = itemView.findViewById(R.id.order_phone);
            take_action = itemView.findViewById(R.id.take_action);


        }
    }
}
