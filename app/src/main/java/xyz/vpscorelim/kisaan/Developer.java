package xyz.vpscorelim.kisaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Objects;

import xyz.vpscorelim.kisaan.customer.Customer_Home;
import xyz.vpscorelim.kisaan.customer.Customer_Info_Form;
import xyz.vpscorelim.kisaan.farmer.Farmer_Home;

public class Developer extends AppCompatActivity {




    EditText feedbackMessage;
    MaterialButton sendFeedBack;
    ImageView back;



    KisanLocalBazar kisanLocalBazar;

    //Firebase ID
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference ;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        kisanLocalBazar = KisanLocalBazar.getzInstance();
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setIcon(R.drawable.icon1);

        feedbackMessage = findViewById(R.id.editText1);
        sendFeedBack = findViewById(R.id.update_info);

        //Firebase Initialize
        mAuth       = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference("Users_Feedback");


        back= findViewById(R.id.close);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbackMessageText =  feedbackMessage.getText().toString();
                if(feedbackMessageText.isEmpty()){
                    feedbackMessage.setError("Please Describe your feedback!");
                    feedbackMessage.requestFocus();
                }else{
                    sendMessage(feedbackMessageText);
                }

            }
        });

    }

    private void sendMessage(String feedbackMessageText) {


        HashMap<String,Object> result =new HashMap<>();
        result.put("FeedBack",feedbackMessageText);
        result.put("User_Id",currentUser.getUid());


        databaseReference.child(currentUser.getUid()).updateChildren(result)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(kisanLocalBazar, "Your FeedBack is Submitted!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(kisanLocalBazar, "Thank You For Your Valuable FeedBack!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(kisanLocalBazar, "Something Goes Wrong!", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}