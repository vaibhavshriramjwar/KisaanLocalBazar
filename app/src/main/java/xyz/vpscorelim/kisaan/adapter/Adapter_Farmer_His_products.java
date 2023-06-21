package xyz.vpscorelim.kisaan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.His_Farmer_Profile;
import xyz.vpscorelim.kisaan.customer.Vegetable_Details;
import xyz.vpscorelim.kisaan.dealer.DFarmerDealerProfile;
import xyz.vpscorelim.kisaan.dealer.DFarmerProductDetails;
import xyz.vpscorelim.kisaan.farmer.Adapter_Farmer_product_list;
import xyz.vpscorelim.kisaan.farmer.Farmer_Home;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;


public class Adapter_Farmer_His_products extends RecyclerView.Adapter<Adapter_Farmer_His_products.MyViewHolder> {

    Context mContext;
    List<Farmer_Product_Data> mData ;
    String myUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    public Adapter_Farmer_His_products(Context mContext, List<Farmer_Product_Data> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.his_farmer_products_row,parent,false);
        return new Adapter_Farmer_His_products.MyViewHolder(row);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.fa_product_name.setText(mData.get(position).getProduct_Name());
        holder.fa_product_price.setText(mData.get(position).getProduct_rate());
        holder.fa_product_unit.setText(mData.get(position).getProduct_Unit());
        Glide.with(mContext).load(mData.get(position).getProduct_image()).into(holder.fa_product_image);

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
        ImageView fa_more;
        ImageView add,sub;

        public MyViewHolder(View itemView) {
            super(itemView);

            //add              = itemView.findViewById(R.id.add);
            //sub              = itemView.findViewById(R.id.sub);
            //price            = itemView.findViewById(R.id.add_price);
            fa_product_image = itemView.findViewById(R.id.product_img);
            fa_product_name  = itemView.findViewById(R.id.product_name);
            fa_product_unit  = itemView.findViewById(R.id.product_price_unit);
            fa_product_price = itemView.findViewById(R.id.price);
            //fa_more          = itemView.findViewById(R.id.more);


            //Firebase Init
            mAuth             = FirebaseAuth.getInstance();
            currentUser       = mAuth.getCurrentUser();
            firebaseDatabase  = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("User_Data");


            DatabaseReference datref1 = firebaseDatabase.getReference("User_Data");
            Query query = datref1.orderByChild("uid").equalTo(currentUser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot ds :snapshot.getChildren()){

                        //get Value
                        final String user_role                =""+ds.child("User_Role").getValue();


                        if(user_role.equals("dealer"))
                        {


                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent postDetailActivity = new Intent(mContext, DFarmerProductDetails.class);
                                    int position = getAdapterPosition();

                                    postDetailActivity.putExtra("vegetable_name",mData.get(position).getProduct_Name());
                                    postDetailActivity.putExtra("vegetable_img",mData.get(position).getProduct_image());
                                    postDetailActivity.putExtra("vegetable_price",mData.get(position).getProduct_rate());
                                    postDetailActivity.putExtra("vegetable_unit",mData.get(position).getProduct_Unit());
                                    postDetailActivity.putExtra("vegetable_id",mData.get(position).getProduct_Id());
                                    postDetailActivity.putExtra("far_id",mData.get(position).getUid());
                                    postDetailActivity.putExtra("vegetable_category",mData.get(position).getProduct_Category());
                                    // will fix this later i forgot to add user name to post object
                                    //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
                                    //Long timestamp  = (Long) mData.get(position).getTimeStamp();
                                    //postDetailActivity.putExtra("postDate",timestamp) ;
                                    mContext.startActivity(postDetailActivity);
                                    Animatoo.animateZoom(mContext);




                                }
                            });


                        }

                        else if(user_role.equals("customer")){


                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent postDetailActivity = new Intent(mContext, Vegetable_Details.class);
                                    int position = getAdapterPosition();

                                    postDetailActivity.putExtra("vegetable_name",mData.get(position).getProduct_Name());
                                    postDetailActivity.putExtra("vegetable_img",mData.get(position).getProduct_image());
                                    postDetailActivity.putExtra("vegetable_price",mData.get(position).getProduct_rate());
                                    postDetailActivity.putExtra("vegetable_unit",mData.get(position).getProduct_Unit());
                                    postDetailActivity.putExtra("vegetable_id",mData.get(position).getProduct_Id());
                                    postDetailActivity.putExtra("far_id",mData.get(position).getUid());
                                    postDetailActivity.putExtra("vegetable_category",mData.get(position).getProduct_Category());
                                    // will fix this later i forgot to add user name to post object
                                    //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
                                    //Long timestamp  = (Long) mData.get(position).getTimeStamp();
                                    //postDetailActivity.putExtra("postDate",timestamp) ;
                                    mContext.startActivity(postDetailActivity);
                                    Animatoo.animateZoom(mContext);



                                }
                            });

                        }






                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(mContext, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });





        }


    }
}
