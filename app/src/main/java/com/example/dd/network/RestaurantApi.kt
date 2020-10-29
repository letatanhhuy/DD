package com.example.dd.network

import com.example.dd.constant.ProjectConstant
import com.example.dd.model.Restaurant
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RestaurantApi {
    @GET(restaurantApiPath)
    suspend fun getRestaurants(
        @Query(value = "lat") lat: Double = ProjectConstant.DOORDASH_HQ_LAT,
        @Query(value = "lng") lon: Double = ProjectConstant.DOORDASH_HQ_LON,
        @Query(value = "offset") offset: Int = 0,
        @Query(value = "limit") limit: Int = ProjectConstant.DOORDASH_MAINVIEW_LIMIT
    ): List<Restaurant>

    @GET(restaurantApiPath.plus("/{id}"))
    suspend fun getRestaurant(
        @Path(value = "id") id: Long
    ): Restaurant

    companion object {
        private const val restaurantApiPath = "/v2/restaurant"
    }
}