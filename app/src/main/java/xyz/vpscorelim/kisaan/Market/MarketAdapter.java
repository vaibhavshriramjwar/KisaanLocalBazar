package xyz.vpscorelim.kisaan.Market;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.Adapter_Farmer_His_products;
import xyz.vpscorelim.kisaan.customer.Vegetable_Details;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.MyViewHolder> {

    Context mContext;
    List<Item> mData ;
    String myUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;


    public MarketAdapter(Context mContext, List<Item> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }



    @NonNull
    @Override
    public MarketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.market_list_layout,parent,false);
        return new MarketAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketAdapter.MyViewHolder holder, int position) {

        holder.district.setText(mData.get(position).getDistrict());
        holder.state.setText(mData.get(position).getState());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView district;
        TextView state;



        public MyViewHolder(View itemView) {
            super(itemView);

            //add              = itemView.findViewById(R.id.add);
            //sub              = itemView.findViewById(R.id.sub);
            //price            = itemView.findViewById(R.id.add_price);
            //district = itemView.findViewById(R.id.district);
            //state = itemView.findViewById(R.id.mandi);

            //fa_more          = itemView.findViewById(R.id.more);


        }
    }


    }