package xyz.vpscorelim.kisaan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Verify_OTP extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    MaterialButton verify_btn;
    ProgressDialog pd;
    private EditText editText;

    TextView time,resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.verify__o_t_p);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        time = findViewById(R.id.wait);
        resend = findViewById(R.id.resend);

        timer();






        verify_btn = findViewById(R.id.verify_btn);
        editText = findViewById(R.id.otp_text_view);
        pd = new ProgressDialog(this);
        mAuth =FirebaseAuth.getInstance();
        final String phoneNumber = getIntent().getStringExtra("phoneNumber");
        final String userRole = getIntent().getStringExtra("userRole");
        sendVerificationCode(phoneNumber);



        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(phoneNumber);
                timer();
            }
        });



        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                pd.setMessage("please wait...");
                String code = editText.getText().toString().trim();
                if(code.isEmpty() || code.length()<6){
                    editText.setError(getString(R.string.enter_code));
                    editText.requestFocus();
                    pd.dismiss();
                    return;
                }


                verifyCode(code);
            }
        });



    }

    private void timer() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                time.setText("Wait for: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                resend.setVisibility(View.VISIBLE);

            }

        }.start();
    }


    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredemtial(credential);
    }

    private void signInWithCredemtial(PhoneAuthCredential credential) {
        final String phoneNumber = getIntent().getStringExtra("phoneNumber");
        final String userRole = getIntent().getStringExtra("userRole");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User_Data");
                            userRef.orderByChild("phoneNumber").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue()!=null){


                                        Intent intent = new Intent(Verify_OTP.this,CheckUserProfile.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }else{
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid =user.getUid();
                                        HashMap<Object ,String> hashMap =new HashMap<>();
                                        //put hashmap in info
                                        hashMap.put("User_Role",userRole);
                                        hashMap.put("uid",uid);
                                        hashMap.put("phoneNumber",phoneNumber);
                                        //Firebase Database  instance
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("User_Data");

                                        reference.child(uid).setValue(hashMap);


                                        Toast.makeText(Verify_OTP.this, "Data Added", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Verify_OTP.this,CheckUserProfile.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Verify_OTP.this, "Data Not Added", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{
                            Toast.makeText(Verify_OTP.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    verificationId =s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    pd.show();
                    String code=phoneAuthCredential.getSmsCode();
                    if(code != null){
                        editText.setText(code);
                        verifyCode(code);
                    }else {
                        pd.show();
                    }

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    pd.dismiss();
                    Toast.makeText(Verify_OTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            };


    public  void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Setting", Activity.MODE_PRIVATE);
        String lamguage = preferences.getString("My_Language","");
        Locale locale =new Locale(lamguage);
        Locale.setDefault(locale);

    }
}