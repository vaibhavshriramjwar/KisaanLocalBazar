package xyz.vpscorelim.kisaan.customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import xyz.vpscorelim.kisaan.Log_In;
import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.Verify_OTP;

public class custome_info_win implements GoogleMap.InfoWindowAdapter {

    private final View mwindow;
    private Context mCOntext;

    GetFarmer getFarmer;

    public custome_info_win(Context mCOntext) {
        this.mCOntext = mCOntext;

        mwindow = LayoutInflater.from(mCOntext).inflate(R.layout.custome_info_window,null);

    }


    private void renderWindowText(Marker marker,View view){


        FirebaseAuth mAuth;
        FirebaseUser currentUser;

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        String phone = currentUser.getPhoneNumber();




        final String title =marker.getTitle();
        final String mobNo =marker.getSnippet();

        TextView full_name = (TextView) view.findViewById(R.id.far_name);
        TextView mob_no = (TextView) view.findViewById(R.id.far_phone);
        MaterialButton buy_now =(MaterialButton) view.findViewById(R.id.generate_btn);


        if(!title.equals("You")){
            full_name.setText(title);
        }
        if(!mobNo.equals("This You Standing")){
            mob_no.setText(mobNo);
        }


        full_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCOntext, Customer_Profile.class);
                intent.putExtra("phoneNumber",mobNo);
                intent.putExtra("farmer_name",title);
                mCOntext.startActivity(intent);
            }
        });

        //MaterialButton buy_now = (MaterialButton) view.findViewById(R.id.buy_now);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mCOntext, Customer_Profile.class);
                intent.putExtra("phoneNumber",mobNo);
                intent.putExtra("farmer_name",title);
                mCOntext.startActivity(intent);

            }
        });

        mwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCOntext, Customer_Profile.class);
                intent.putExtra("phoneNumber",mobNo);
                intent.putExtra("farmer_name",title);
                mCOntext.startActivity(intent);
            }
        });










    }

    @Override
    public View getInfoWindow(Marker marker) {



        renderWindowText(marker,mwindow);



        return mwindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker,mwindow);

        return mwindow;
    }
}
