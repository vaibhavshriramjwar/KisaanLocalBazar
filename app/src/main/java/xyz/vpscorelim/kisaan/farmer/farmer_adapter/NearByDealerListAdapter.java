package xyz.vpscorelim.kisaan.farmer.farmer_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.CustomerRequest;
import xyz.vpscorelim.kisaan.customer.Vegetable_Details;
import xyz.vpscorelim.kisaan.farmer.FarmerDealerProfile;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderDetails;
import xyz.vpscorelim.kisaan.farmer.farmer_Model.GetDealerModel;

public class NearByDealerListAdapter extends RecyclerView.Adapter<NearByDealerListAdapter.MyViewHolder>{

    Context mContext;
    List<GetDealerModel> mData;


    public NearByDealerListAdapter(Context mContext, List<GetDealerModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public NearByDealerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.dealer_list, parent, false);
        return new NearByDealerListAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull NearByDealerListAdapter.MyViewHolder holder, final int position) {

        holder.store_name.setText(mData.get(position).getStoreName());
        holder.store_owner_name.setText(mData.get(position).getDealerName());
        holder.shop_sub_area.setText(mData.get(position).getSubArea());
        holder.shop_city.setText(mData.get(position).getDistrictName());
        holder.shop_state.setText(mData.get(position).getStateName());


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




    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView store_name,store_owner_name,shop_sub_area,shop_city,shop_state;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            store_name =itemView.findViewById(R.id.txt_store_name);
            store_owner_name = itemView.findViewById(R.id.txt_own_name);
            shop_sub_area = itemView.findViewById(R.id.txt_sub_area);
            shop_city = itemView.findViewById(R.id.txt_city_name);
            shop_state = itemView.findViewById(R.id.state_name);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postDetailActivity = new Intent(mContext, FarmerDealerProfile.class);
                    int position = getAdapterPosition();
                    postDetailActivity.putExtra("phoneNumber",mData.get(position).getPhoneNumber());
                    postDetailActivity.putExtra("dealerUid",mData.get(position).getUid());
                    mContext.startActivity(postDetailActivity);
                    Animatoo.animateZoom(mContext);
                }
            });





        }
    }
}