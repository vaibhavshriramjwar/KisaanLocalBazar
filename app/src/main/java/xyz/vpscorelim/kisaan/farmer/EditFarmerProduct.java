package xyz.vpscorelim.kisaan.farmer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;

import xyz.vpscorelim.kisaan.R;

public class EditFarmerProduct extends AppCompatActivity {

    EditText product_Name,product_Categories,product_Quantity,product_Unit,product_Rate;
    MaterialButton update_product;
    ImageView product_Image;
    public FirebaseAuth firebaseAuth;
    public FirebaseUser currentUser;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_farmer_product);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();

        String pId = getIntent().getStringExtra("editPostId");
        Toast.makeText(this, ""+pId, Toast.LENGTH_SHORT).show();

        product_Name = findViewById(R.id.productName);
        product_Categories = findViewById(R.id.productCategories);
        product_Unit = findViewById(R.id.productUnit);
        product_Quantity = findViewById(R.id.productQuantity);
        product_Rate = findViewById(R.id.productRate);

        update_product = findViewById(R.id.update_product);
        update_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateproduct(pId);
            }
        });
        product_Image  = findViewById(R.id.productImage);

        //Firebase
        firebaseAuth= FirebaseAuth.getInstance();
        currentUser =firebaseAuth.getCurrentUser();
        firebaseDatabase =FirebaseDatabase.getInstance();
        reference =firebaseDatabase.getReference("Farmer_Products");
        String farmer_uid =  currentUser.getUid();


        getProductData(pId);






    }

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }


    private void getProductData(String pId) {
        //---------------------/Get Current User Data/----------------------------//
        Query query = reference.orderByChild("Product_Id").equalTo(pId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String product_categories        =""+ds.child("Product_Category").getValue();
                    String product_name              =""+ds.child("Product_Name").getValue();
                    String product_quantity          =""+ds.child("Product_Quantity").getValue();
                    String product_unit              =""+ds.child("Product_Unit").getValue();
                    String product_image             =""+ds.child("Product_image").getValue();
                    String product_rate              =""+ds.child("Product_rate").getValue();


                    product_Categories.setText(product_categories);
                    product_Name.setText(product_name);
                    product_Quantity.setText(product_quantity);
                    product_Unit.setText(product_unit);
                    product_Rate.setText(product_rate);
                    Glide.with(EditFarmerProduct.this).load(product_image).into(product_Image);





                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //---------------------//----------------------------//
    }
    private void updateproduct(String pId) {

        String quantity = product_Quantity.getText().toString().trim();
        String rate     = product_Rate.getText().toString().trim();

        if(quantity.isEmpty()){
            product_Quantity.setError("Add new quantity");
            product_Quantity.requestFocus();

        }
        else if(rate.isEmpty()){

            product_Rate.setError("Add new quantity");
            product_Rate.requestFocus();
        }
        else {


            HashMap<String,Object> result =new HashMap<>();
            result.put("Product_Quantity",quantity);
            result.put("Product_rate",rate);

            reference.child(pId).updateChildren(result)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(EditFarmerProduct.this, "Product Updated", Toast.LENGTH_SHORT).show();
                            //pd.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditFarmerProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }



    }


}