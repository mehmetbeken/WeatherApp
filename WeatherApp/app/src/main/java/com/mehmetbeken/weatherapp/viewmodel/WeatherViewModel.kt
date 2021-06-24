package com.mehmetbeken.weatherapp.viewmodel

import WeatherAPIService
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mehmetbeken.weatherapp.model.WeatherModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class WeatherViewModel: ViewModel() {

    private val weatherAPIService=WeatherAPIService()
    private val disposable=CompositeDisposable()

    val weatherData=MutableLiveData<WeatherModel>()
    val weatherError=MutableLiveData<Boolean>()
    val weatherloading=MutableLiveData<Boolean>()

    fun refreshData(cityName: String){
        getDataFromAPI(cityName)

    }

    private fun getDataFromAPI(cityName:String) {
        weatherloading.value=true
        disposable.add(
            weatherAPIService.getDataService(cityName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :DisposableSingleObserver<WeatherModel>(){
                    override fun onSuccess(t: WeatherModel) {
                      weatherData.value=t
                      weatherloading.value=false
                      weatherError.value=false
                    }

                    override fun onError(e: Throwable) {
                        weatherError.value=true
                        weatherloading.value=false


                    }

                })
        )
    }

}