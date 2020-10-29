package com.example.dd.core

import com.example.dd.constant.ProjectConstant
import com.example.dd.network.RestaurantApi
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoreComponent {
    private val restaurantApi: RestaurantApi by lazy {
            Retrofit.Builder()
                .baseUrl(ProjectConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RestaurantApi::class.java)
    }

    private val gson: Gson by lazy {
        Gson()
    }

    fun getApplicationGSON():Gson = gson
    fun getApplicationRestaurantApi():RestaurantApi = restaurantApi
}