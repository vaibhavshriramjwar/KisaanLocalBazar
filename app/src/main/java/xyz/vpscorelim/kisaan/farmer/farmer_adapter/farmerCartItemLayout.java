package xyz.vpscorelim.kisaan.farmer.farmer_adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xyz.vpscorelim.kisaan.Database.Database;
import xyz.vpscorelim.kisaan.Database.FarmerDatabase;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.customer.ViewMyCart;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderModel;
import xyz.vpscorelim.kisaan.farmer.FarmerViewMyCart;

class farmerCartViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_item_name,txt_item_price,txt_item_brand,txt_item_unit;
    public ImageView cart_item_img,del;
    public ElegantNumberButton numberBtn;
    Context context;



    public void setTxt_item_name(TextView txt_item_name) {
        this.txt_item_name = txt_item_name;
    }

    public farmerCartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_item_name = itemView.findViewById(R.id.cart_itm_name);
        txt_item_brand = itemView.findViewById(R.id.cart_itm_brand);
        txt_item_price= itemView.findViewById(R.id.cart_itm_price);
        del           = itemView.findViewById(R.id.product_img);
        numberBtn     = itemView.findViewById(R.id.count);
        txt_item_unit = itemView.findViewById(R.id.product_price_unit);






    }

    @Override
    public void onClick(View v) {



    }
}


public class farmerCartItemLayout extends RecyclerView.Adapter<farmerCartViewHolder> {

    private List<FarmerOrderModel> listData = new ArrayList<>();
    private FarmerViewMyCart viewMyCart;

    public farmerCartItemLayout(List<FarmerOrderModel> listData, FarmerViewMyCart viewMyCart) {
        this.listData = listData;
        this.viewMyCart = viewMyCart;
    }

    @NonNull
    @Override
    public farmerCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewMyCart);
        View itemView = layoutInflater.inflate(R.layout.farmer_cart_layout, parent, false);
        return new farmerCartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull farmerCartViewHolder holder, final int position) {



        FirebaseAuth mAuth;
        FirebaseUser currentUser;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String phone = currentUser.getPhoneNumber();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewMyCart);
                builder.setItems(new String[]{"Delete Cart"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            FarmerOrderModel orderModel = listData.get(position);
                            new FarmerDatabase(viewMyCart).deleteItem(orderModel);
                        }
                    }
                });
                builder.create().show();
            }
        });
        holder.numberBtn.setNumber(listData.get(position).getQuantity());
        holder.numberBtn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                FarmerOrderModel orderModel = listData.get(position);
                orderModel.setQuantity(String.valueOf(newValue));
                new FarmerDatabase(viewMyCart).updateCartItem(orderModel);


                int total = 0;
                List<FarmerOrderModel> cart1 = new FarmerDatabase(viewMyCart).getCarts(phone);
                for (FarmerOrderModel item : cart1)

                    total += (Integer.parseInt(orderModel.getPrice())) * (Integer.parseInt(item.getQuantity()));
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));


                viewMyCart.txt_total.setText(formatter.format(total));
            }
        });
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        int price = (Integer.parseInt(listData.get(position).getPrice())) * (Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_item_price.setText(formatter.format(price));
        holder.txt_item_unit.setText(listData.get(position).getUnit());
        holder.txt_item_name.setText(listData.get(position).getProductName());
        holder.txt_item_brand.setText(listData.get(position).getProductBrand());
        Glide.with(viewMyCart).load(listData.get(position).getProductImage()).into(holder.del);


    }


    @Override
    public int getItemCount() {
        return listData.size();

    }
}