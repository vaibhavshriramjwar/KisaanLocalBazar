package xyz.vpscorelim.kisaan.dealer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.dealer.DealerAdaper.DealerProductListAdapter;
import xyz.vpscorelim.kisaan.dealer.DealerAdaper.SeedItemAdapter;
import xyz.vpscorelim.kisaan.dealer.DealerModel.DealerProductListModel;
import xyz.vpscorelim.kisaan.dealer.DealerModel.SeedModel;
import xyz.vpscorelim.kisaan.farmer.Adapter_Farmer_product_list;
import xyz.vpscorelim.kisaan.farmer.Farmer_Add_product;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;
import xyz.vpscorelim.kisaan.farmer.VegetableAdapter;
import xyz.vpscorelim.kisaan.farmer.VegetableItems;

public class DealerAddProduct extends AppCompatActivity {


    Spinner pro_categories,pro_unit;
    EditText qty,rate_pro,pro_name,pro_brand;
    MaterialButton add_product;


    SeedItemAdapter seedAdapter;
     ArrayList<SeedModel> seedModel;


    //Permission Constant
    private static final int CAMERA_REQUEST_CODE =100;
    private static final int STORAGE_REQUEST_CODE =200;

    //Image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE =300;
    private static final int IMAGE_PICK_GALLERY_CODE =400;

    //permission array
    String cameraPermissions[];
    String storagePermissions[];


    private Uri pickedImgUri = null;

    String productCategory,productName,productUnit,Image;



    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressDialog pd;
    FirebaseUser currentUser;

    public static int index = -1;
    public static int top = -1;

    LinearLayoutManager mLayoutManager;
    RecyclerView postRecyclerView ;
    DealerProductListAdapter postAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<DealerProductListModel> mData;
    String uid;


    ImageView pick_img,close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dealer_add_product);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();
        pd = new ProgressDialog(this);
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Variable Init



        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DealerAddProduct.this, Dealer_Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        pro_brand= findViewById(R.id.brand_name);
        pro_name = findViewById(R.id.pro_name);
        pick_img = findViewById(R.id.add_img_icon);
        pick_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here when image clicked we need to open the gallery
                // before we open the gallery we need to check if our app have the access to user files
                // we did this before in register activity I'm just going to copy the code to save time ...

                pickFromGallery();
            }
        });




        pro_categories = findViewById(R.id.pro_categories);
        //pro_name       = findViewById(R.id.pro_name);
        pro_unit       = findViewById(R.id.pro_unit);
        qty            = findViewById(R.id.qty);
        rate_pro       = findViewById(R.id.rate_pro);
        add_product    = findViewById(R.id.add_product);



        postRecyclerView   =findViewById(R.id.dealer_product_list);
        mLayoutManager     = new LinearLayoutManager(this);
        postRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(mLayoutManager);



        mAuth =FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Dealer_Products");
        mAuth =FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        final int lastFirstVisiblePosition = ((LinearLayoutManager) postRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Dealer_Products");
        Query query = ref.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {

                    DealerProductListModel post = pvostsnap.getValue(DealerProductListModel.class);
                    mData.add(post);
                    Collections.reverse(mData);
                }
                postAdapter = new DealerProductListAdapter(DealerAddProduct.this, mData);


                postRecyclerView.setAdapter(postAdapter);
                ((LinearLayoutManager) postRecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastFirstVisiblePosition,0);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(DealerAddProduct.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });



        initSeed();



        //Spinner Add Product Options
        final List<String> subOptions = new ArrayList<>();
        List<String> options = new ArrayList<>();
        options.add(0,"Select Product Type");
        options.add("Seed");
        options.add("Pesticide");
        options.add("Fertilizer");
        options.add("Farm machinery");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pro_categories.setAdapter(dataAdapter);
        pro_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select Product Type"))
                {
                    Toast.makeText(DealerAddProduct.this, "Please Choose Something", Toast.LENGTH_SHORT).show();
                }
                else if(parent.getItemAtPosition(position).equals("Seed"))
                {
                    productCategory = "Seed";
//                    subOptions.clear();
//                    seedAdapter = new SeedItemAdapter(DealerAddProduct.this, seedModel);
//                    pro_name.setAdapter(seedAdapter);
//                    pro_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                            SeedModel clickedItem = (SeedModel) adapterView.getItemAtPosition(i);
//                            productName = clickedItem.getSeedName();
//                            Image = String.valueOf(clickedItem.getSeedImage());
//
//                            Toast.makeText(DealerAddProduct.this, productName + " selected", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//
//                        }
//                    });
                }
                else if(parent.getItemAtPosition(position).equals("Pesticide"))
                {
                    productCategory = "Pesticide";
                }
                else if(parent.getItemAtPosition(position).equals("Fertilizer"))
                {
                    productCategory = "Fertilizer";
                }
                else if(parent.getItemAtPosition(position).equals("Farm machinery"))
                {
                    productCategory = "machinery";
                }

                else{
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(DealerAddProduct.this, "You Select "+item, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //End Options


        //Spinner Units
        List<String> units = new ArrayList<>();
        units.add(0,"Select Unit");
        units.add("Gm");
        units.add("Gms");
        units.add("Kg");
        units.add("Ton");
        units.add("Doz");
        units.add("ml");
        units.add("ltr");
        units.add("sds");
        units.add("seeds");
        units.add("No Unit");
        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,units);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pro_unit.setAdapter(dataAdapter2);
        pro_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select Unit"))
                {

                }

                else{
                    productUnit = parent.getItemAtPosition(position).toString();

                    Toast.makeText(DealerAddProduct.this, "You Select "+productUnit, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                pd.show();
                pd.setMessage("Product is Adding");
                final String productQuantity = qty.getText().toString().trim();
                final String productName = pro_name.getText().toString().trim();
                final String productBrandName = pro_brand.getText().toString().trim();
                final String productRate     = rate_pro.getText().toString().trim();



                if(productCategory ==null)
                {
                    pd.dismiss();
                    Toast.makeText(DealerAddProduct.this, "Please select product category", Toast.LENGTH_SHORT).show();
                }
                else if(productName.isEmpty())
                {
                    pd.dismiss();
                    Toast.makeText(DealerAddProduct.this, "Please select product Name", Toast.LENGTH_SHORT).show();
                }
                else if(productBrandName.isEmpty())
                {
                    pd.dismiss();
                    Toast.makeText(DealerAddProduct.this, "Please select product Name", Toast.LENGTH_SHORT).show();
                }
                else if(productUnit ==null)
                {
                    pd.dismiss();
                    Toast.makeText(DealerAddProduct.this, "Please select product unit", Toast.LENGTH_SHORT).show();
                }
                else if(productQuantity.isEmpty())
                {
                    pd.dismiss();
                    Toast.makeText(DealerAddProduct.this, "Product Added", Toast.LENGTH_SHORT).show();

                }
                else if(productRate.isEmpty())
                {
                    pd.dismiss();
                    Toast.makeText(DealerAddProduct.this, "Product Added", Toast.LENGTH_SHORT).show();

                }
                else if(pickedImgUri == null)
                {
                    pd.dismiss();
                    Toast.makeText(DealerAddProduct.this, "Please add Image", Toast.LENGTH_SHORT).show();

                }else{

                    Bitmap bmp = ((BitmapDrawable)pick_img.getDrawable()).getBitmap();
                    try {

                        bmp = MediaStore.Images.Media.getBitmap(getContentResolver(),pickedImgUri);


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream baos =new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] fileInBytes =baos.toByteArray();


                    final StorageReference ref = storageReference.child("dealer_image/" + UUID.randomUUID().toString());
                    ref.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while(!uriTask.isSuccessful());
                                    String downloadUri = uriTask.getResult().toString();

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String uid =user.getUid();
                                    String timestamp = String.valueOf(System.currentTimeMillis());
                                    HashMap<Object ,String> hashMap =new HashMap<>();
                                    hashMap.put("uid",uid);
                                    hashMap.put("Product_Category",productCategory);
                                    hashMap.put("Product_Name",productName);
                                    hashMap.put("Product_Brand",productBrandName);
                                    hashMap.put("Product_Quantity",productQuantity);
                                    hashMap.put("Product_Unit",productUnit);
                                    hashMap.put("Product_rate",productRate);
                                    hashMap.put("Product_image",downloadUri);
                                    hashMap.put("Product_Id",timestamp);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Dealer_Products");
                                    reference.child(timestamp).setValue(hashMap);

                                    pd.dismiss();
                                    qty.setText("");
                                    rate_pro.setText("");
                                    Toast.makeText(DealerAddProduct.this, "Your Product Added", Toast.LENGTH_SHORT).show();


                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DealerAddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(DealerAddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){

        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
    }


    private boolean checkCameraPermission(){

        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)
                ==(PackageManager.PERMISSION_GRANTED);


        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){

        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case STORAGE_REQUEST_CODE:{

                if(grantResults.length >0){

                    boolean writeStorageAccepted = grantResults[1]== PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Please enable storage permission ", Toast.LENGTH_SHORT).show();
                    }
                }




            }
            break;
        }







    }





    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK){

            if(requestCode == IMAGE_PICK_GALLERY_CODE) {
                pickedImgUri = data.getData();

                pick_img.setImageURI(pickedImgUri);

            }


        }

        super.onActivityResult(requestCode, resultCode, data);

    }





    private void initSeed()
    {
        seedModel = new ArrayList<>();
        seedModel.add(new SeedModel("Pumpkin", R.drawable.wheat));

    }
}