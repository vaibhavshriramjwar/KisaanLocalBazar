package xyz.vpscorelim.kisaan.farmer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import xyz.vpscorelim.kisaan.R;

public class Adapter_Farmer_product_list extends RecyclerView.Adapter<Adapter_Farmer_product_list.MyViewHolder> {

    Context mContext;
    List<Farmer_Product_Data> mData ;
    String myUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;




    public Adapter_Farmer_product_list(Context mContext, List<Farmer_Product_Data> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_farmer_product,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        FirebaseUser currentUser;
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Farmer_Products");
        holder.fa_product_name.setText(mData.get(position).getProduct_Name());
        holder.fa_product_price.setText(mData.get(position).getProduct_rate());
        holder.fa_product_unit.setText(mData.get(position).getProduct_Unit());
        Glide.with(mContext).load(mData.get(position).getProduct_image()).into(holder.fa_product_image);
        final String hisUid    = mData.get(position).getUid();
        final String uid       = mData.get(position).getUid();
        final String pId       = mData.get(position).getProduct_Id();
        final String image       = mData.get(position).getProduct_image();
        //String userimg = mData.get(position).getUserPhoto();


        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        myUid=currentUser.getUid();




        holder.fa_more.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                showMoreOptions(holder.fa_more,uid,myUid,pId,image);
            }
        });


//        holder.imgPostProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                builder.setItems(new String[]{"Profile", "Chat"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if(which==0){
//                            //Profile Clicked
//                            Intent intent1 = new Intent(mContext, ThereProfile.class);
//                            intent1.putExtra("uid",hisUid);
//                            mContext.startActivity(intent1);
//
//
//
//
//                        }
//                        if(which==1){
//                            //Chat
//                            Intent intent = new Intent(mContext, ChatActivity.class);
//                            intent.putExtra("hisUid",hisUid);
//                            mContext.startActivity(intent);
//
//
//                        }
//
//                    }
//                });
//                builder.create().show();
//            }
//
//
//        });
//
//
//
//
   }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showMoreOptions(ImageView moreBtn, String uid, String myUid, final String pId, final String image) {


        PopupMenu popupMenu = new PopupMenu(mContext,moreBtn, Gravity.END);
        if(uid.equals(myUid)){
            popupMenu.getMenu().add(Menu.NONE,0,0,"Delete");
            popupMenu.getMenu().add(Menu.NONE,1,0,"Edit");

        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == 0){

                    beginDelete(pId,image);
                }else if(id == 1){
                    //Edit is Clicked
                    Intent intent = new Intent(mContext, EditFarmerProduct.class);
                    intent.putExtra("key","editpost");
                    intent.putExtra("editPostId",pId);
                    mContext.startActivity(intent);
                }



                return false;
            }
        });
        popupMenu.show();


    }

    private void beginDelete(final String pId, String image) {

        ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage("Deleting....");
        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(image);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Query fQuery = FirebaseDatabase.getInstance().getReference("Farmer_Products").orderByChild("Product_Id").equalTo(pId);
                        fQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for(DataSnapshot ds:dataSnapshot.getChildren()){
                                    ds.getRef().removeValue();
                                }

                                Toast.makeText(mContext, "Deleted Succesfully...", Toast.LENGTH_SHORT).show();

                                //pd.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //pd.dismiss();
                        Toast.makeText(mContext, "Something went wrong"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView fa_product_image;
        TextView fa_product_name;
        TextView  fa_product_price;
        TextView  fa_product_unit;
        ImageView fa_more;

        public MyViewHolder(View itemView) {
            super(itemView);

            fa_product_image = itemView.findViewById(R.id.product_image);
            fa_product_name  = itemView.findViewById(R.id.product_name);
            fa_product_unit  = itemView.findViewById(R.id.product_unit);
            fa_product_price = itemView.findViewById(R.id.product_price);
            fa_more          = itemView.findViewById(R.id.more);




//            itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
//                            int position = getAdapterPosition();
//
//                            postDetailActivity.putExtra("title",mData.get(position).getTitle());
//                            postDetailActivity.putExtra("postImage",mData.get(position).getPicture());
//                            postDetailActivity.putExtra("description",mData.get(position).getDescription());
//                            postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
//                            postDetailActivity.putExtra("userPhoto",mData.get(position).getUserPhoto());
//                            // will fix this later i forgot to add user name to post object
//                            //postDetailActivity.putExtra("userName",mData.get(position).getUsername);
//                            Long timestamp  = (Long) mData.get(position).getTimeStamp();
//                            postDetailActivity.putExtra("postDate",timestamp) ;
//                            mContext.startActivity(postDetailActivity);




//                }
//            });


        }


    }
}