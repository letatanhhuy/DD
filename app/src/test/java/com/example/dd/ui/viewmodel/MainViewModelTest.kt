
package com.example.dd.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.dd.MainCoroutineScopeRule
import com.example.dd.data.RestaurantRepo
import com.example.dd.model.Restaurant
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @Mock private lateinit var restaurantRepo: RestaurantRepo
    @Mock private lateinit var restaurantsLiveData: MutableLiveData<List<Restaurant>>
    @Mock private lateinit var loadingData: MutableLiveData<Boolean>
    @Mock private lateinit var showError: MutableLiveData<Boolean>


    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    private val restaurant by lazy {
        Restaurant(0.0f, "", "", 1, "test name", "test description")
    }

    private val restaurants by lazy {
        listOf(restaurant)
    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(restaurantsLiveData.postValue(Mockito.any())).then {  }
        Mockito.`when`(loadingData.postValue(Mockito.any())).then {  }
        Mockito.`when`(showError.postValue(Mockito.any())).then {  }
        Mockito.`when`(runBlocking {
            restaurantRepo.getRestaurants(0)
        }).thenReturn(runBlocking {
            restaurants
        })
    }

    @Test
    fun test_MainViewModel_init() {
        val mainViewModel = MainViewModel(restaurantRepo, coroutineScope.dispatcher)
        Truth.assertThat(mainViewModel.restaurantsLiveData).isNotNull()
        Truth.assertThat(mainViewModel.loadingData.value).isFalse()
        Truth.assertThat(mainViewModel.showError.value).isFalse()
    }

    @Test
    fun test_MainViewModel_getRestaurants_success() {
        val mainViewModel = MainViewModel(restaurantRepo, coroutineScope.dispatcher)
        runBlockingTest {
            mainViewModel.getRestaurants(0, restaurantsLiveData, loadingData, showError)
            Mockito.verify(restaurantRepo, Mockito.times(1)).getRestaurants(0)
            Mockito.verify(restaurantsLiveData, Mockito.times(1)).postValue(Mockito.any())
            Mockito.verify(loadingData, Mockito.times(1)).postValue(true)
            Mockito.verify(loadingData, Mockito.times(1)).postValue(false)
            Mockito.verify(showError, Mockito.times(1)).postValue(false)
            Mockito.verify(showError, Mockito.times(0)).postValue(true)
        }
    }

    @Test
    fun test_MainViewModel_getRestaurants_fail() {
        Mockito.`when`(runBlocking {
            restaurantRepo.getRestaurants(0)
        }).then {runBlocking {
            throw Exception() //simulate error
        }}
        val mainViewModel = MainViewModel(restaurantRepo, coroutineScope.dispatcher)
        runBlockingTest {
            mainViewModel.getRestaurants(0, restaurantsLiveData, loadingData, showError)
            Mockito.verify(restaurantRepo, Mockito.times(1)).getRestaurants(0)
            Mockito.verify(restaurantsLiveData, Mockito.times(0)).postValue(Mockito.any())
            Mockito.verify(loadingData, Mockito.times(1)).postValue(true)
            Mockito.verify(loadingData, Mockito.times(1)).postValue(false)
            Mockito.verify(showError, Mockito.times(1)).postValue(false)
            Mockito.verify(showError, Mockito.times(1)).postValue(true)
        }
    }

    @Test
    fun test_MainViewModel_getRestaurants_whileAlreadyLoading() {
        Mockito.`when`(loadingData.value).thenReturn(true)
        val mainViewModel = MainViewModel(restaurantRepo, coroutineScope.dispatcher)
        runBlockingTest {
            mainViewModel.getRestaurants(0, restaurantsLiveData, loadingData, showError)
            Mockito.verify(restaurantRepo, Mockito.times(0)).getRestaurants(0)
            Mockito.verify(restaurantsLiveData, Mockito.times(0)).postValue(Mockito.any())
            Mockito.verify(loadingData, Mockito.times(0)).postValue(Mockito.anyBoolean())
            Mockito.verify(showError, Mockito.times(0)).postValue(Mockito.anyBoolean())
        }
    }
}
