package com.mehmetbeken.weatherapp.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.GetChars
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.mehmetbeken.weatherapp.R
import com.mehmetbeken.weatherapp.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_weather.*

class Weather : AppCompatActivity() {

    private lateinit var viewmodel:WeatherViewModel

    private lateinit var GET:SharedPreferences
    private lateinit var SET:SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        GET=getSharedPreferences(packageName, MODE_PRIVATE)
        SET=GET.edit()


        viewmodel=ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        var cName=GET.getString("cityName","istanbul")
        edt_city_name.setText(cName)

        viewmodel.refreshData(cName!!)

        getLiveData()


        swipe_refresh_layout.setOnRefreshListener {
            pb_loading.visibility=View.GONE
            tv_error.visibility=View.GONE
            ll_data_view.visibility=View.GONE

            var cityName=GET.getString("cityName",cName)
                edt_city_name.setText(cityName)
            viewmodel.refreshData(cityName!!)
            swipe_refresh_layout.isRefreshing=false

        }
        img_search_city.setOnClickListener {
            val cityName=edt_city_name.text.toString()
            SET.putString("cityName",cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()
        }

    }

    private fun getLiveData() {
        viewmodel.weatherData.observe(this, Observer {data->
            data?.let {
                ll_data_view.visibility= View.VISIBLE
                pb_loading.visibility=View.GONE
                tv_degree.text=it.main.temp.toString()
                tv_city_code.text=it.sys.country.toString()
                tv_city_name.text=it.name.toString()
                tv_humidity.text=it.main.humidity.toString()
                tv_wind_speed.text=it.wind.speed.toString()
                tv_lat.text=it.coord.lat.toString()
                tv_lon.text=it.coord.lon.toString()

                Glide.with(this).load("https://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(img_weather_pictures)



            }

        })
        viewmodel.weatherloading.observe(this, Observer {load->
            load?.let {
                if (it){
                    pb_loading.visibility=View.VISIBLE
                    tv_error.visibility=View.GONE
                    ll_data_view.visibility=View.GONE
                }else{
                    pb_loading.visibility=View.GONE

                }
            }

        })
        viewmodel.weatherError.observe(this, Observer { error ->
            error?.let {
                if(it){
                    pb_loading.visibility=View.GONE
                    tv_error.visibility=View.VISIBLE
                    ll_data_view.visibility=View.GONE
                }else{
                    tv_error.visibility=View.GONE
                }
            }
        })
    }
}