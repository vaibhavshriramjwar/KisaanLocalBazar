package xyz.vpscorelim.kisaan.customer;

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

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.adapter.Adapter_Farmer_His_products;
import xyz.vpscorelim.kisaan.dealer.DFarmerProductDetails;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;

public class AdapterCardProductView extends RecyclerView.Adapter<AdapterCardProductView.MyViewHolder> {

    Context mContext;
    List<Farmer_Product_Data> mData;
    String myUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    public AdapterCardProductView(Context mContext, List<Farmer_Product_Data> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AdapterCardProductView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.product_view_layout, parent, false);
        return new AdapterCardProductView.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCardProductView.MyViewHolder holder, int position) {

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
                    Intent postDetailActivity = new Intent(mContext, His_Farmer_Profile.class);
                    int position = getAdapterPosition();
                    postDetailActivity.putExtra("phoneNumber",mData.get(position).getPhoneNumber());
                    mContext.startActivity(postDetailActivity);
                    Animatoo.animateZoom(mContext);
                }
            });



        }
    }
}









