package xyz.vpscorelim.kisaan.adapter;

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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.His_Farmer_Profile;
import xyz.vpscorelim.kisaan.customer.Vegetable_Details;
import xyz.vpscorelim.kisaan.farmer.Farmer_Home;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class HomePageFarmerList extends RecyclerView.Adapter<HomePageFarmerList.MyViewHolder>  {



    Context mContext;
    List<ModelFarmerData> mData ;
    String myUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;


    public HomePageFarmerList(Context mContext, List<ModelFarmerData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public HomePageFarmerList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.area_farmer,parent,false);
        return new HomePageFarmerList.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageFarmerList.MyViewHolder holder, int position) {

        holder.fa_product_name.setText(mData.get(position).getFarmer_Name());
        holder.fa_product_unit.setText(mData.get(position).getPhoneNumber());
        holder.viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postDetailActivity = new Intent(mContext, His_Farmer_Profile.class);
                postDetailActivity.putExtra("phoneNumber",mData.get(position).getPhoneNumber());
                mContext.startActivity(postDetailActivity);
                Animatoo.animateZoom(mContext);
            }
        });
        //Glide.with(mContext).load(mData.get(position).getProduct_image()).into(holder.fa_product_image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView fa_product_image;
        TextView fa_product_name;
        TextView  fa_product_price,price;
        TextView  fa_product_unit;
        MaterialButton viewProducts;
        ImageView fa_more;
        ImageView add,sub;

        public MyViewHolder(View itemView) {
            super(itemView);


            //fa_product_image = itemView.findViewById(R.id.pro_img);
            fa_product_name  = itemView.findViewById(R.id.farmer_name);
            fa_product_unit  = itemView.findViewById(R.id.customer_phone);
            viewProducts     = itemView.findViewById(R.id.view_products);









        }


    }





}
