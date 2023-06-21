package xyz.vpscorelim.kisaan.farmer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xyz.vpscorelim.kisaan.R;

public class Farmer_My_Product extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;



    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    public static int index = -1;
    public static int top = -1;
    LinearLayoutManager mLayoutManager;
    RecyclerView postRecyclerView ;
    Adapter_Farmer_product_list postAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<Farmer_Product_Data> mData;
    String uid;


    public Farmer_My_Product() {
        // Required empty public constructor
    }


    public static Farmer_My_Product newInstance(String param1, String param2) {
        Farmer_My_Product fragment = new Farmer_My_Product();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View farmer_my_product = inflater.inflate(R.layout.farmer_my_product, container, false);

        //Database ID
        postRecyclerView   =farmer_my_product.findViewById(R.id.product_list);
        mLayoutManager     = new LinearLayoutManager(getActivity());
        postRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));
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
                postAdapter = new Adapter_Farmer_product_list(getActivity(), mData);


                postRecyclerView.setAdapter(postAdapter);
                ((LinearLayoutManager) postRecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastFirstVisiblePosition,0);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });





        return farmer_my_product;
    }
}