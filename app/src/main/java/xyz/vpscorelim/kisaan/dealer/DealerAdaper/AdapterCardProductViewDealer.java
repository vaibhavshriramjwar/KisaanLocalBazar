package xyz.vpscorelim.kisaan.dealer.DealerAdaper;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.AdapterCardProductView;
import xyz.vpscorelim.kisaan.customer.His_Farmer_Profile;
import xyz.vpscorelim.kisaan.dealer.DFarmerDealerProfile;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class AdapterCardProductViewDealer extends RecyclerView.Adapter<AdapterCardProductViewDealer.MyViewHolder> {

    Context mContext;
    List<Farmer_Product_Data> mData;
    String myUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public AdapterCardProductViewDealer(Context mContext, List<Farmer_Product_Data> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdapterCardProductViewDealer.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.product_view_layout, parent, false);
        return new AdapterCardProductViewDealer.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCardProductViewDealer.MyViewHolder holder, int position) {

        holder.fa_product_name.setText(mData.get(position).getProduct_Name());
        holder.fa_product_price.setText(mData.get(position).getProduct_rate());
        holder.fa_product_unit.setText(mData.get(position).getProduct_Unit());
        holder.fa_product_owner.setText(mData.get(position).getFarmerName());
        holder.fa_product_category.setText(mData.get(position).getProduct_Category());
        Glide.with(mContext).load(mData.get(position).getProduct_image()).into(holder.fa_product_image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView fa_product_image;
        TextView fa_product_name;
        TextView fa_product_price;
        TextView fa_product_unit;
        TextView fa_product_category;
        TextView fa_product_owner;


        public MyViewHolder(View itemView) {
            super(itemView);


            fa_product_image = itemView.findViewById(R.id.product_img);
            fa_product_name = itemView.findViewById(R.id.proName);
            fa_product_unit = itemView.findViewById(R.id.product_price_unit);
            fa_product_price = itemView.findViewById(R.id.price);
            fa_product_category = itemView.findViewById(R.id.cat);
            fa_product_owner = itemView.findViewById(R.id.owner_name);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postDetailActivity = new Intent(mContext, DFarmerDealerProfile.class);
                    int position = getAdapterPosition();
                    postDetailActivity.putExtra("phoneNumber",mData.get(position).getPhoneNumber());
                    mContext.startActivity(postDetailActivity);
                    Animatoo.animateZoom(mContext);
                }
            });



        }
    }


}
