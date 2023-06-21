package xyz.vpscorelim.kisaan.farmer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.dealer.DealerAddProduct;
import xyz.vpscorelim.kisaan.dealer.Dealer_Home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Farmer_Add_product extends AppCompatActivity {

    ImageView img_view,close;
    Spinner spinner,addPro,unit;
    MaterialButton addProduct;
    TextView cancel;
    String productCategory,productName,productUnit,Image;
    EditText quantity,rate;

    private ArrayList<VegetableItems> vegetableItems;
    private VegetableAdapter vegetableAdapter;

    private ArrayList<FruitsItems> fruitsItems;
    private FruitsAdapter fruitsAdapter;

    private ArrayList<GrainsItems> grainsItems;
    private GrainsAdapter grainsAdapter;

    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressDialog pd;
    FirebaseUser currentUser;
    public static int index = -1;
    public static int top = -1;
    LinearLayoutManager mLayoutManager;
    RecyclerView postRecyclerView ;
    Adapter_Farmer_product_list postAdapter ;
    FirebaseDatabase firebaseDatabase,firData;
    DatabaseReference databaseReference,datRef;
    List<Farmer_Product_Data> mData;
    String uid;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.farmer_add_product);
        loadLocale();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        getSupportActionBar().hide();

        initList();
        initListFruits();
        initListGrains();



        getFarmerData();


        close = findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Farmer_Add_product.this, Farmer_Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        pd = new ProgressDialog(this);

        spinner =findViewById(R.id.spin1);
        addPro  =findViewById(R.id.spin2);
        unit    =findViewById(R.id.spin3);

        addProduct =  findViewById(R.id.add_product);
        quantity   = findViewById(R.id.quantity);
        rate       = findViewById(R.id.rate);
        mAuth =FirebaseAuth.getInstance();

        cancel = findViewById(R.id.cancel);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();




        //Database ID
        postRecyclerView   =findViewById(R.id.product_list);
        mLayoutManager     = new LinearLayoutManager(this);
        postRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(mLayoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Farmer_Products");
        mAuth =FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        final int lastFirstVisiblePosition = ((LinearLayoutManager) postRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Farmer_Products");
        Query query = ref.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mData = new ArrayList<>();
                for (DataSnapshot pvostsnap : dataSnapshot.getChildren()) {

                    Farmer_Product_Data post = pvostsnap.getValue(Farmer_Product_Data.class);
                    mData.add(post);
                    Collections.reverse(mData);
                }
                postAdapter = new Adapter_Farmer_product_list(Farmer_Add_product.this, mData);


                postRecyclerView.setAdapter(postAdapter);
                ((LinearLayoutManager) postRecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastFirstVisiblePosition,0);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Farmer_Add_product.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




        //Spinner Add Product Options
        final List<String> subOptions = new ArrayList<>();
        List<String> options = new ArrayList<>();
        options.add(0,"Select Product Type");
        options.add("Vegetable");
        options.add("Fruits");
        options.add("Grain");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Choose Options"))
                {
                    Toast.makeText(Farmer_Add_product.this, "Please Choose Something", Toast.LENGTH_SHORT).show();
                }
                else if(parent.getItemAtPosition(position).equals("Vegetable"))
                {
                    productCategory = "Vegetable";
                    subOptions.clear();
                    vegetableAdapter = new VegetableAdapter(Farmer_Add_product.this, vegetableItems);
                    addPro.setAdapter(vegetableAdapter);
                    addPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            VegetableItems clickedItem = (VegetableItems) adapterView.getItemAtPosition(i);
                            productName = clickedItem.getVegetableName();
                            Image = String.valueOf(clickedItem.getVegetableImage());

                            Toast.makeText(Farmer_Add_product.this, productName + " selected", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }
                else if(parent.getItemAtPosition(position).equals("Fruits"))
                {
                    productCategory = "Fruits";
                    subOptions.clear();
                    fruitsAdapter = new FruitsAdapter(Farmer_Add_product.this, fruitsItems);
                    addPro.setAdapter(fruitsAdapter);
                    addPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            FruitsItems clickedItem = (FruitsItems) adapterView.getItemAtPosition(i);
                            productName = clickedItem.getFruitsName();
                            Image = String.valueOf(clickedItem.getFruitsImage());
                            Toast.makeText(Farmer_Add_product.this, productName + " selected", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });



                }
                else if(parent.getItemAtPosition(position).equals("Grain"))
                {
                    productCategory = "Grain";
                    subOptions.clear();
                    grainsAdapter = new GrainsAdapter(Farmer_Add_product.this, grainsItems);
                    addPro.setAdapter(grainsAdapter);
                    addPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                            GrainsItems clickedItem = (GrainsItems) adapterView.getItemAtPosition(i);
                            productName = clickedItem.getGrainsName();
                            Image = String.valueOf(clickedItem.getGrainsImage());
                            Toast.makeText(Farmer_Add_product.this, productName + " selected", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                }

                else{
                    String item = parent.getItemAtPosition(position).toString();

                    Toast.makeText(Farmer_Add_product.this, "You Select "+item, Toast.LENGTH_SHORT).show();
                }
            }

            public void fillSpinner() {
                ArrayAdapter<String> dataAdapter1;
                dataAdapter1 = new ArrayAdapter<String> (getApplicationContext(),android.R.layout.simple_spinner_item,subOptions);
                dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addPro.setAdapter(dataAdapter1);

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
        units.add("Kg");
        units.add("Ton");
        units.add("Doz");
        ArrayAdapter<String> dataAdapter2;
        dataAdapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,units);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit.setAdapter(dataAdapter2);
        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Select Unit"))
                {

                }

                else{
                    productUnit = parent.getItemAtPosition(position).toString();


                    switch (productUnit){
                        case"Kg":
                            rate.setHint("Rate Per Kg");
                            break;

                        case"Doz":
                            rate.setHint("Rate Per Doz");
                            break;



                    }


                    Toast.makeText(Farmer_Add_product.this, "You Select "+productUnit, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });



        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                pd.setMessage("Product is Adding");
                final String productQuantity = quantity.getText().toString().trim();
                final String productRate     = rate.getText().toString().trim();

                if(productCategory ==null)
                {
                    pd.dismiss();
                    Toast.makeText(Farmer_Add_product.this, "Please select product category", Toast.LENGTH_SHORT).show();
                }
                else if(productName ==null)
                {
                    pd.dismiss();
                    Toast.makeText(Farmer_Add_product.this, "Please select product Name", Toast.LENGTH_SHORT).show();
                }
                else if(productUnit ==null)
                {
                    pd.dismiss();
                    Toast.makeText(Farmer_Add_product.this, "Please select product unit", Toast.LENGTH_SHORT).show();
                }
                else if(productQuantity.isEmpty())
                {
                    pd.dismiss();
                    Toast.makeText(Farmer_Add_product.this, "Product Added", Toast.LENGTH_SHORT).show();

                }
                else if(productRate.isEmpty())
                {
                    pd.dismiss();
                    Toast.makeText(Farmer_Add_product.this, "Product Added", Toast.LENGTH_SHORT).show();

                }else{
                    Uri imageUri = Uri.parse("android.resource://xyz.vpscorelim.kisaan/" + Image);
                    final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                    ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                                    String phone = user.getPhoneNumber();
                                    String timestamp = String.valueOf(System.currentTimeMillis());
                                    HashMap<Object ,String> hashMap =new HashMap<>();
                                    hashMap.put("uid",uid);
                                    hashMap.put("phoneNumber",phone);
                                    hashMap.put("FarmerName",FarmerCommon.farName);
                                    hashMap.put("FarmerState",FarmerCommon.farState);
                                    hashMap.put("FarmerCity",FarmerCommon.farCity);
                                    hashMap.put("FarmerSubArea",FarmerCommon.farSubArea);
                                    hashMap.put("Product_Category",productCategory);
                                    hashMap.put("Product_Name",productName);
                                    hashMap.put("Product_Quantity",productQuantity);
                                    hashMap.put("Product_Unit",productUnit);
                                    hashMap.put("Product_rate",productRate);
                                    hashMap.put("Product_image",downloadUri);
                                    hashMap.put("Product_Id",timestamp);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = database.getReference("Farmer_Products");
                                    reference.child(timestamp).setValue(hashMap);

                                    pd.dismiss();
                                    quantity.setText("");
                                    rate.setText("");
                                    Toast.makeText(Farmer_Add_product.this, "Your Product Added", Toast.LENGTH_SHORT).show();


                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Farmer_Add_product.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Farmer_Add_product.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });









                }


            }
        });


    }

    private void getFarmerData() {
        mAuth= FirebaseAuth.getInstance();
        currentUser =mAuth.getCurrentUser();
        firData =FirebaseDatabase.getInstance();
        datRef =firData.getReference("Farmer_Data");
        String far_uid = currentUser.getUid();


        //---------------------/Get Current User Data/----------------------------//
        Query query = datRef.orderByChild("uid").equalTo(far_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds :snapshot.getChildren()){
                    String farmer_name        =""+ds.child("Farmer_Name").getValue();
                    String farmer_sub_area    =""+ds.child("Tehsil_Name").getValue();
                    String farmer_phone       =""+ds.child("phoneNumber").getValue();
                    String farmer_city        =""+ds.child("District_Name").getValue();
                    String farmer_state       =""+ds.child("State_Name").getValue();


                    FarmerCommon.farName = farmer_name;
                    FarmerCommon.farCity = farmer_city;
                    FarmerCommon.farState= farmer_state;
                    FarmerCommon.farSubArea = farmer_sub_area;

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //---------------------//----------------------------//





    }


    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }

    private void initListFruits() {

        fruitsItems = new ArrayList<>();
        fruitsItems.add(new FruitsItems("Apple", R.drawable.apple));
        fruitsItems.add(new FruitsItems("Avocado", R.drawable.avocado));
        fruitsItems.add(new FruitsItems("केळी / केला / Banana", R.drawable.banana));
        fruitsItems.add(new FruitsItems("लीची / लीची / Lychee", R.drawable.lychee));
        fruitsItems.add(new FruitsItems("सीताफळ / शरीफा / Custard apple", R.drawable.custardapple));
        fruitsItems.add(new FruitsItems("Dry plums", R.drawable.dryplums));
        fruitsItems.add(new FruitsItems("अंजीर / अंजीर / Figs", R.drawable.figs));
        fruitsItems.add(new FruitsItems("फणस / पनस / Jack fruit", R.drawable.jack_fruit));
        fruitsItems.add(new FruitsItems("काळा मनुका / काले बेर / Black plum", R.drawable.blackplum));
        fruitsItems.add(new FruitsItems("Indian goose berry", R.drawable.indiangooseberry));
        fruitsItems.add(new FruitsItems("द्राक्षे / अंगूर / Grapes", R.drawable.grapes));
        fruitsItems.add(new FruitsItems("पेरू / अमरूद / Guava", R.drawable.guava));
        fruitsItems.add(new FruitsItems("Kiwi fruit", R.drawable.kiwifruit));
        fruitsItems.add(new FruitsItems("आंबा / आम / Mango (ripe)", R.drawable.mango_ripe));
        fruitsItems.add(new FruitsItems("कैरी / कैरी / Mango (unripe)", R.drawable.mango_unripe));
        fruitsItems.add(new FruitsItems("तुतीची / शहतूत /Mulberry", R.drawable.mulberry));
        fruitsItems.add(new FruitsItems("कस्तुरी खरबूज / खरबूजा / Musk melon", R.drawable.musk_melon));
        fruitsItems.add(new FruitsItems("संतरा / संतरा / Orange", R.drawable.orange));
        fruitsItems.add(new FruitsItems("पपई / पपीता / Papaya", R.drawable.papaya));
        fruitsItems.add(new FruitsItems("नासपती / नाशपाती / Pear", R.drawable.pear));
        fruitsItems.add(new FruitsItems("अननस / अनानास /Pineapple", R.drawable.pineapple));
        fruitsItems.add(new FruitsItems("डाळिंब / अनार / Pomegranate", R.drawable.pomegranate));
        fruitsItems.add(new FruitsItems("चिकू / चिकू / Sapota", R.drawable.sapota));
        fruitsItems.add(new FruitsItems("टरबूज / तरबूज / Watermelon", R.drawable.watermelon));


    }

    private void initListGrains() {

        grainsItems = new ArrayList<>();
        grainsItems.add(new GrainsItems("लाल मसूर / Red Lentil", R.drawable.red_lentile));
        grainsItems.add(new GrainsItems("मुंग दाल / Mung Beans Whole", R.drawable.mung_beans_whole));
        grainsItems.add(new GrainsItems("Mung Beans Skinned Split", R.drawable.mung_beans_skilled_split));
        grainsItems.add(new GrainsItems("राजमा(लाल) / राज़में(लाल) / Kidney Beans", R.drawable.kidney_beans));
        grainsItems.add(new GrainsItems("राजमा(भूरा) / राज़में(भूरा) / Pinto Beans", R.drawable.pinto_beans));
        grainsItems.add(new GrainsItems("काले चने / काला चणा / Black Chickpeas", R.drawable.black_chickpeas));
        grainsItems.add(new GrainsItems("हरभरा / चने / Chickpeas", R.drawable.chickpeas));
        grainsItems.add(new GrainsItems("कोरडे वाटाणे / सूखी मटर / Dry White Peas", R.drawable.dry_white_peas));
        grainsItems.add(new GrainsItems("काळा हरभरा मसूर / काले चने की दाल / Black Gram Lentils", R.drawable.black_gram_lentile));
        grainsItems.add(new GrainsItems("चन्याची डाळ / चने की दाल / Bengal Gram Lentils", R.drawable.bengal_gram_lentile));
        grainsItems.add(new GrainsItems("अरहर ची डाळ / अरहर की दाल / Pigeon Pea Split", R.drawable.peagon_pea_split));
        grainsItems.add(new GrainsItems("धान्याचा दाणा / मक्का / Corn ", R.drawable.corn));
        grainsItems.add(new GrainsItems("ज्वार / ज्वार / Sorghum Millet", R.drawable.sorghum_millet));
        grainsItems.add(new GrainsItems("बाजरी / बाजरा / Pearl Millet", R.drawable.pearn_millet));
        grainsItems.add(new GrainsItems("बरबटी / लोबिया / Cowpea", R.drawable.cowpea));
        grainsItems.add(new GrainsItems("नाचनी / रागी / Finger Millet", R.drawable.finger_millet));
        grainsItems.add(new GrainsItems("तीळ / तिल / Sesame", R.drawable.sesame));
        grainsItems.add(new GrainsItems("तांदूळ / चावल / Rice", R.drawable.rice));
        grainsItems.add(new GrainsItems("मुरमुरे / मुरमुरे /Puffed Rice", R.drawable.puffed_rice));
        grainsItems.add(new GrainsItems("पोहा / पोहा /Flattened Rice", R.drawable.flattend_rice));
        grainsItems.add(new GrainsItems("जव / जौ / Barley", R.drawable.barley));
        grainsItems.add(new GrainsItems("गहू / गेहूँ / Wheat", R.drawable.wheat));
        grainsItems.add(new GrainsItems("गव्हाचे पीठ / गेहूं का आटा / Wheat Flour", R.drawable.wheat_flor));
        grainsItems.add(new GrainsItems("मैदा / मैदा / Fine Flour", R.drawable.fine_floor));
        grainsItems.add(new GrainsItems("रवा / सूजी / Semolina", R.drawable.semolina));
        grainsItems.add(new GrainsItems("बेसन / बेसन / Gram Flour", R.drawable.gram_floor));
        grainsItems.add(new GrainsItems("अनाज का आटा / Buckwheat flour", R.drawable.buckwheat));
        grainsItems.add(new GrainsItems("साबूदाना / Sago ", R.drawable.sago));
        grainsItems.add(new GrainsItems("ओट्स / जई / Oats", R.drawable.oats));
        grainsItems.add(new GrainsItems("लप्सी / दलिया / Broken Wheat", R.drawable.broken_wheat));
        grainsItems.add(new GrainsItems("सोयाबीन की मंगोडी /Soya Chunks", R.drawable.soya_chunks));


    }

    private void initList() {

        vegetableItems = new ArrayList<>();
        vegetableItems.add(new VegetableItems("राजगिरा पाने / अमृत पान /Amaranthlea.", R.drawable.amaranth_leaves));
        vegetableItems.add(new VegetableItems("भोपळा / लौकी /pumpkin", R.drawable.ash_gaurd));
        vegetableItems.add(new VegetableItems("वांगे / बैंगन /brinjal", R.drawable.aubergine_img));
        vegetableItems.add(new VegetableItems("गोडमका / मकई /Babycorn", R.drawable.baby_corn));
        vegetableItems.add(new VegetableItems("बीट / चुकंदर /Beetroot", R.drawable.beet_root));
        vegetableItems.add(new VegetableItems("दुधीभोपळा / लौकी /oposquash", R.drawable.bottle_gourd));
        vegetableItems.add(new VegetableItems("कारले / करेला /Bitter gourd", R.drawable.bitter_gourd));
        vegetableItems.add(new VegetableItems("कोबी / पत्ता गोभी /Cabbage", R.drawable.cabbage_img));
        vegetableItems.add(new VegetableItems("गाजर / गाजर /Carrot", R.drawable.carrot_img));
        vegetableItems.add(new VegetableItems("शिमला मिर्ची / शिमला मिर्च /Capsicum", R.drawable.capsicum_img));
        vegetableItems.add(new VegetableItems("फुलकोबी / गोभी /Cauliflower", R.drawable.cauliflower_img));
        vegetableItems.add(new VegetableItems("क्लस्टर सोयाबीनचे / गँवार फली /Clusterbe.", R.drawable.cluster_beans));
        vegetableItems.add(new VegetableItems("नारळ / नारियल /Coconut", R.drawable.coconut_img));
        vegetableItems.add(new VegetableItems("कोथिंबीर / धनिया /Coriander", R.drawable.coriander_img));
        vegetableItems.add(new VegetableItems("मक्का / मक्का /Corn", R.drawable.corn_img));
        vegetableItems.add(new VegetableItems("काकडी / खीरा /Cucumber", R.drawable.cucumber_img));
        vegetableItems.add(new VegetableItems("कढीपत्ता / करी पत्ते /Curryleaves", R.drawable.curry_leaves));
        vegetableItems.add(new VegetableItems("बडीशेप /Dill", R.drawable.dill_img));
        vegetableItems.add(new VegetableItems("Drumsticks", R.drawable.drum_sticks));
        vegetableItems.add(new VegetableItems("मेथीची पाने / कसूरी मेथी /Fenugreekleaves", R.drawable.fenugreek_leaves));
        vegetableItems.add(new VegetableItems("चवळीच्या शेंगा /Frenchbeans", R.drawable.french_beans));
        vegetableItems.add(new VegetableItems("लसूण / लहसुन /Garlic", R.drawable.garl_ic));
        vegetableItems.add(new VegetableItems("ताजे आले / ताजा अदरक /Freshginger", R.drawable.fresh_ginger));
        vegetableItems.add(new VegetableItems("हिरवी मिरची / हरी मिर्च /Greenchilli", R.drawable.green_chilli));
        vegetableItems.add(new VegetableItems("फणस / कटहल /Jackfruit", R.drawable.jack_fruit));
        vegetableItems.add(new VegetableItems("लिंबू / नींबू /lemon", R.drawable.lemon_img));
        vegetableItems.add(new VegetableItems("मलबार पालक / मालाबार पालक /Malabarspinach", R.drawable.malabar_spinach));
        vegetableItems.add(new VegetableItems("मशरूम / मशरूम /Mushroom", R.drawable.mushroom_img));
        vegetableItems.add(new VegetableItems("मोहरीची पाने / सरसों के पत्ते /Mustardlea.", R.drawable.mustard_leaves));
        vegetableItems.add(new VegetableItems("कांदा / प्याज /Onion", R.drawable.onion_img));
        vegetableItems.add(new VegetableItems("भेंडी / भिन्डी /Okra", R.drawable.lady_finger));
        vegetableItems.add(new VegetableItems("Flat green beans", R.drawable.flat_green_beans));
        vegetableItems.add(new VegetableItems("वाटाणे / मटर /Peas", R.drawable.peas_img));
        vegetableItems.add(new VegetableItems("पुदीना पाने / पुदीने की पत्तियां /Mintleaves", R.drawable.mint_leaves));
        vegetableItems.add(new VegetableItems("भोपळा / कद्दू /Pumpkin", R.drawable.pumpkin_img));
        vegetableItems.add(new VegetableItems("बटाटा / आलू /Potato", R.drawable.potato_img));
        vegetableItems.add(new VegetableItems("मुळा / मूली /Radish", R.drawable.radish_img));
        vegetableItems.add(new VegetableItems("मुळा शेंगा / मूली की फली /Radishpods", R.drawable.radish_pods));
        vegetableItems.add(new VegetableItems("Raw plaintain", R.drawable.raw_plaintain));
        vegetableItems.add(new VegetableItems("लाल तिखट / लाल मिर्च /Redchili", R.drawable.red_chili));
        vegetableItems.add(new VegetableItems("पडवळ / चिचिण्डा /Snakegourd", R.drawable.snake_gourd));
        vegetableItems.add(new VegetableItems("पालक / पालक /Spinach", R.drawable.spinach_img));
        vegetableItems.add(new VegetableItems("रताळे / शकरकंद /Sweetpotato", R.drawable.sweet_potato));
        vegetableItems.add(new VegetableItems("टोमॅटो / टमाटर /Tomato", R.drawable.tomato_img));
        vegetableItems.add(new VegetableItems("Taro roots", R.drawable.taro_roots));
        vegetableItems.add(new VegetableItems("तोंडली / कुंदरु /Ivygourd", R.drawable.ivy_gourd));
        vegetableItems.add(new VegetableItems("सलगम / शलजम /Turnip", R.drawable.turnip_img));
        vegetableItems.add(new VegetableItems("सुरण / सुरण /Elephantyam", R.drawable.elephant_yam));
        vegetableItems.add(new VegetableItems("बटाटा / आलू /Potato", R.drawable.potato_img));
        vegetableItems.add(new VegetableItems("मेरुदंड /Spinegourd", R.drawable.spine_gourd));

    }
}