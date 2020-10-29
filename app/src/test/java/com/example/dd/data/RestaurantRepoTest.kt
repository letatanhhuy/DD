package com.example.dd.data

import com.example.dd.constant.ProjectConstant
import com.example.dd.model.Restaurant
import com.example.dd.network.RestaurantApi
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class RestaurantRepoTest {
    @Mock
    lateinit var restaurantApi: RestaurantApi

    private val restaurant by lazy {
        Restaurant(0.0f, "", "", 1, "test name", "test description")
    }

    private val restaurants by lazy {
        listOf(restaurant)
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(runBlocking {
            restaurantApi.getRestaurant(Mockito.anyLong())
        }).thenReturn(runBlocking {
            restaurant
        })
        Mockito.`when`(runBlocking {
            restaurantApi.getRestaurants(
                ProjectConstant.DOORDASH_HQ_LAT,
                ProjectConstant.DOORDASH_HQ_LON,
                0,
                ProjectConstant.DOORDASH_MAINVIEW_LIMIT
            )
        }).thenReturn(runBlocking {
            restaurants
        })
    }

    @Test
    fun test_RestaurantRepo_getRestaurant() {
        val restaurantRepo = RestaurantRepo(restaurantApi)
        runBlockingTest {
            restaurantRepo.getRestaurant(Mockito.anyLong())
            Mockito.verify(restaurantApi, Mockito.times(1)).getRestaurant(Mockito.anyLong())
            Truth.assertThat(restaurantRepo.getRestaurant(Mockito.anyLong())).isEqualTo(restaurant)
        }
    }

    @Test
    fun test_RestaurantRepo_getRestaurants() {
        val restaurantRepo = RestaurantRepo(restaurantApi)
        runBlockingTest {
            restaurantRepo.getRestaurants(0)
            Mockito.verify(restaurantApi, Mockito.times(1))
                .getRestaurants(
                    ProjectConstant.DOORDASH_HQ_LAT,
                    ProjectConstant.DOORDASH_HQ_LON,
                    0,
                    ProjectConstant.DOORDASH_MAINVIEW_LIMIT
                )
            Truth.assertThat(restaurantRepo.getRestaurants(0)).isEqualTo(restaurants)
        }
    }
}