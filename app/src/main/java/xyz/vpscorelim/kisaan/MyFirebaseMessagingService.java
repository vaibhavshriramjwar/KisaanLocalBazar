package xyz.vpscorelim.kisaan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import xyz.vpscorelim.kisaan.R;
import xyz.vpscorelim.kisaan.customer.CustomerMyOrder;
import xyz.vpscorelim.kisaan.customer.CustomerOrderDetails;
import xyz.vpscorelim.kisaan.dealer.DealerOrderList;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderDetails;
import xyz.vpscorelim.kisaan.farmer.Farmer_Order_Status;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String NOTIFICATION_CHANNEL_ID = "MY_NOTIFICATION_CHANNEL_ID";

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        String notificationType = remoteMessage.getData().get("notificationType");

        assert notificationType != null;
        if (notificationType.equals("NewOrder")){
            String customerUid = remoteMessage.getData().get("customerUid");
            String farmerUid   = remoteMessage.getData().get("farmerUid");
            String orderId   = remoteMessage.getData().get("orderId");
            String customer_phone = remoteMessage.getData().get("customer_phone");
            String notificationTitle   = remoteMessage.getData().get("notificationTitle");
            String notificationMessage   = remoteMessage.getData().get("notificationMessage");

            if(currentUser.getUid().equals(farmerUid)){
                showNotification(customerUid,farmerUid,orderId,notificationTitle,notificationMessage,notificationType,customer_phone);
            }


        }
        if (notificationType.equals("OrderStatus")){
            String customerUid = remoteMessage.getData().get("customerUid");
            String farmerUid   = remoteMessage.getData().get("farmerUid");
            String orderId   = remoteMessage.getData().get("orderId");
            String customer_phone = remoteMessage.getData().get("customer_phone");
            String notificationTitle   = remoteMessage.getData().get("notificationTitle");
            String notificationMessage   = remoteMessage.getData().get("notificationMessage");

            if(currentUser.getUid().equals(customerUid)){
                showNotification(customerUid,farmerUid,orderId,notificationTitle,notificationMessage,notificationType,customer_phone);
            }
        }
        if (notificationType.equals("OrderStatusChangeByDealer")){
            String customerUid = remoteMessage.getData().get("customerUid");
            String farmerUid   = remoteMessage.getData().get("farmerUid");
            String orderId   = remoteMessage.getData().get("orderId");
            String customer_phone = remoteMessage.getData().get("customer_phone");
            String notificationTitle   = remoteMessage.getData().get("notificationTitle");
            String notificationMessage   = remoteMessage.getData().get("notificationMessage");

            if(currentUser.getUid().equals(customerUid)){
                showNotification(customerUid,farmerUid,orderId,notificationTitle,notificationMessage,notificationType,customer_phone);
            }
        }
        if (notificationType.equals("OrderStatusChangeFromFarmer")){
            String customerUid = remoteMessage.getData().get("customerUid");
            String farmerUid   = remoteMessage.getData().get("farmerUid");
            String orderId   = remoteMessage.getData().get("orderId");
            String customer_phone = remoteMessage.getData().get("customer_phone");
            String notificationTitle   = remoteMessage.getData().get("notificationTitle");
            String notificationMessage   = remoteMessage.getData().get("notificationMessage");

            if(currentUser.getUid().equals(customerUid)){
                showNotification(customerUid,farmerUid,orderId,notificationTitle,notificationMessage,notificationType,customer_phone);
            }
        }
        if (notificationType.equals("NewOrderToDealer")){
            String customerUid = remoteMessage.getData().get("customerUid");
            String farmerUid   = remoteMessage.getData().get("farmerUid");
            String orderId   = remoteMessage.getData().get("orderId");
            String customer_phone = remoteMessage.getData().get("customer_phone");
            String notificationTitle   = remoteMessage.getData().get("notificationTitle");
            String notificationMessage   = remoteMessage.getData().get("notificationMessage");

            if(currentUser.getUid().equals(farmerUid)){
                showNotification(customerUid,farmerUid,orderId,notificationTitle,notificationMessage,notificationType,customer_phone);
            }


        }
        if (notificationType.equals("NewOrderToFarmer")){
            String customerUid = remoteMessage.getData().get("customerUid");
            String farmerUid   = remoteMessage.getData().get("farmerUid");
            String orderId   = remoteMessage.getData().get("orderId");
            String customer_phone = remoteMessage.getData().get("customer_phone");
            String notificationTitle   = remoteMessage.getData().get("notificationTitle");
            String notificationMessage   = remoteMessage.getData().get("notificationMessage");

            if(currentUser.getUid().equals(farmerUid)){
                showNotification(customerUid,farmerUid,orderId,notificationTitle,notificationMessage,notificationType,customer_phone);
            }


        }









    }


    private void showNotification(String customerUid, String farmerUid, String orderId, String notificationTitle, String notificationMessage, String notificationType,String customer_phone) {

    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

    int notificationId = new Random().nextInt(3000);
    if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O){
        setupNotificationChannel(notificationManager);
    }


    Intent intent =null;
    if(notificationType.equals("NewOrder")){
        intent = new Intent(this, FarmerOrderDetails.class);
        intent.putExtra("OrderId",orderId);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    }


    if(notificationType.equals("OrderStatus")){
            intent = new Intent(this, CustomerMyOrder.class);
            intent.putExtra("OrderId",orderId);
            intent.putExtra("phone",customer_phone);
            intent.putExtra("farmer_id",farmerUid);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        }


    if(notificationType.equals("OrderStatusChangeFromFarmer")){
            intent = new Intent(this, DealerOrderList.class);
            intent.putExtra("OrderId",orderId);
            intent.putExtra("phone",customer_phone);
            intent.putExtra("farmer_id",farmerUid);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        }


    if(notificationType.equals("OrderStatusChangeByDealer")){
            intent = new Intent(this, Farmer_Order_Status.class);
            intent.putExtra("OrderId",orderId);
            intent.putExtra("phone",customer_phone);
            intent.putExtra("farmer_id",farmerUid);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        }


    if(notificationType.equals("NewOrderToDealer")){
            intent = new Intent(this, DealerOrderList.class);
            intent.putExtra("OrderId",orderId);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        }

        if(notificationType.equals("NewOrderToFarmer")){
            intent = new Intent(this, Farmer_Order_Status.class);
            intent.putExtra("OrderId",orderId);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        }






    PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_name);
        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(largeIcon)
                .setColor(Color.GREEN)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setSound(notificationUri)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        notificationManager.notify(notificationId,notificationBuilder.build());

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupNotificationChannel(NotificationManager notificationManager) {

        CharSequence channelName= "Some Sample Text";
        String channelDescription = "Channel Description Here";

        NotificationChannel notificationChannel= new NotificationChannel(NOTIFICATION_CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.setDescription(channelDescription);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);

        if(notificationManager!=null){
            notificationManager.createNotificationChannel(notificationChannel);
        }




    }
}
