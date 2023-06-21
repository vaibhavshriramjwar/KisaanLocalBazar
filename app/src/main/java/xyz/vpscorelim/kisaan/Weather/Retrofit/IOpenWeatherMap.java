package xyz.vpscorelim.kisaan.Weather.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.vpscorelim.kisaan.Weather.Model.WeatherResult;

public interface IOpenWeatherMap {

    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat")String lat,
                                                 @Query("lon")String lng,
                                                 @Query("appid")String appid,
                                                 @Query("units")String units);


}
