package xyz.vpscorelim.kisaan.Weather.Common;

import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID ="1fdf07e7b8ba6fc1d33078aa96782c36";
    public static Location current_location =null;

    public static double latitude =0;
    public static double longitude =0;



    public static String convertUnixToDate(Long dt) {

        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a || E || dd-MM-yy");
        String formatted = sdf.format(date);


        return formatted;

    }

    public static String convertUnixToHour(Long dt) {

        Date date = new Date(dt*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatted = sdf.format(date);


        return formatted;

    }
}
