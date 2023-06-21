package xyz.vpscorelim.kisaan.dealer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.dealer.DealerAdaper.DealerProductListAdapter;
import xyz.vpscorelim.kisaan.dealer.DealerModel.DealerProductListModel;
import xyz.vpscorelim.kisaan.farmer.Adapter_Farmer_product_list;
import xyz.vpscorelim.kisaan.farmer.Farmer_Product_Data;


public class DealerMyProduct extends Fragment {


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
    DealerProductListAdapter postAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    List<DealerProductListModel> mData;
    String uid;
    TextView t2;


    public DealerMyProduct() {
        // Required empty public constructor
    }


    public static DealerMyProduct newInstance(String param1, String param2) {
        DealerMyProduct fragment = new DealerMyProduct();
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
        View dealerMyProduct = inflater.inflate(R.layout.farmer_my_product, container, false);


        //Database ID
        t2 = dealerMyProduct.findViewById(R.id.t2);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Dealer_Home.class);
                startActivity(intent);
            }
        });
        postRecyclerView   =dealerMyProduct.findViewById(R.id.product_list);
        mLayoutManager     = new LinearLayoutManager(getActivity());
        postRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(mLayoutManager);

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
                postAdapter = new DealerProductListAdapter(getActivity(), mData);


                postRecyclerView.setAdapter(postAdapter);
                ((LinearLayoutManager) postRecyclerView.getLayoutManager()).scrollToPositionWithOffset(lastFirstVisiblePosition,0);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




        return  dealerMyProduct;
    }
}