package com.example.dd.data

import com.example.dd.model.Restaurant
import com.example.dd.network.RestaurantApi

class RestaurantRepo(private val restaurantApi: RestaurantApi) {
    suspend fun getRestaurants(offset:Int) : List<Restaurant> =
        restaurantApi.getRestaurants(offset = offset)
    suspend fun getRestaurant(id:Long) : Restaurant =
        restaurantApi.getRestaurant(id)
}