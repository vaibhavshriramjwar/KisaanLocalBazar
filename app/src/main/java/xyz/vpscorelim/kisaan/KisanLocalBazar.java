package xyz.vpscorelim.kisaan;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class KisanLocalBazar extends Application {

    private static KisanLocalBazar kisanLocalBazar;

    public KisanLocalBazar() {
        kisanLocalBazar = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        kisanLocalBazar =this;
        // OneSignal Initialization

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static synchronized KisanLocalBazar getzInstance(){
        return kisanLocalBazar;
    }

}
