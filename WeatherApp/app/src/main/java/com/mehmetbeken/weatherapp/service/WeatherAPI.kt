package com.mehmetbeken.weatherapp.service

import com.mehmetbeken.weatherapp.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/weather?units=metric&appid=272abbfb64cf25b9a3ce5166607ffbc0")
    fun getData(
        @Query("q") cityName:String
    ):Single<WeatherModel>

}