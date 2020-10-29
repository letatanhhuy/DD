package com.example.dd.model

import com.google.common.truth.Truth
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test

class RestaurantTest {
    private val status = "status"
    private val name = "name"
    private val url = "url"
    private val description = "description"
    private val id = 1L
    private val deliveryFee = 10.0f
    private val gson = Gson()
    private val gsonRestaurant = "{\"delivery_fee\":$deliveryFee,\"status\":\"$status\",\"cover_img_url\":\"$url\",\"id\":$id,\"name\":\"$name\",\"description\":\"$description\"}"
    @Before
    fun setUp() {

    }

    @Test
    fun test_Restaurant_constructor() {
        val restaurant = Restaurant(deliveryFee, status, url, id, name, description)
        Truth.assertThat(restaurant.id).isEqualTo(id)
        Truth.assertThat(restaurant.name).isEqualTo(name)
        Truth.assertThat(restaurant.status).isEqualTo(status)
        Truth.assertThat(restaurant.deliveryFee).isEqualTo(deliveryFee)
        Truth.assertThat(restaurant.description).isEqualTo(description)
    }

    @Test
    fun test_Restaurant_FromGson() {
        val restaurant = Restaurant(deliveryFee, status, url, id, name, description)
        val restaurantFromGson = gson.fromJson(gsonRestaurant, Restaurant::class.java)

        Truth.assertThat(restaurant.id).isEqualTo(restaurantFromGson.id)
        Truth.assertThat(restaurant.name).isEqualTo(restaurantFromGson.name)
        Truth.assertThat(restaurant.status).isEqualTo(restaurantFromGson.status)
        Truth.assertThat(restaurant.deliveryFee).isEqualTo(restaurantFromGson.deliveryFee)
        Truth.assertThat(restaurant.description).isEqualTo(restaurantFromGson.description)
    }

    @Test
    fun test_Restaurant_ToGson() {
        val restaurant = Restaurant(deliveryFee, status, url, id, name, description)
        val restaurantString = gson.toJson(restaurant)
        Truth.assertThat(restaurantString).isEqualTo(gsonRestaurant)
    }


}