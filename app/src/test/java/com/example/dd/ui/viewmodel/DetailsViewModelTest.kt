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
class DetailsViewModelTest {
    @Mock private lateinit var restaurantRepo: RestaurantRepo
    @Mock private lateinit var restaurantLiveData: MutableLiveData<Restaurant>
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
        Mockito.`when`(restaurantLiveData.postValue(Mockito.any())).then {  }
        Mockito.`when`(loadingData.postValue(Mockito.any())).then {  }
        Mockito.`when`(showError.postValue(Mockito.any())).then {  }
        Mockito.`when`(runBlocking {
            restaurantRepo.getRestaurant(Mockito.anyLong())
        }).thenReturn(runBlocking {
            restaurant
        })
    }

    @Test
    fun test_DetailsViewModel_init() {
        val detailsViewModel = DetailsViewModel(restaurantRepo, coroutineScope.dispatcher)
        Truth.assertThat(detailsViewModel.restaurantLiveData).isNotNull()
        Truth.assertThat(detailsViewModel.loadingData.value).isFalse()
        Truth.assertThat(detailsViewModel.showError.value).isFalse()
    }

    @Test
    fun test_DetailsViewModel_fetchRestaurantDetails_success() {
        val detailsViewModel = DetailsViewModel(restaurantRepo, coroutineScope.dispatcher)
        runBlockingTest {
            detailsViewModel.fetchRestaurantDetails(0, restaurantLiveData, loadingData, showError)
            Mockito.verify(restaurantRepo, Mockito.times(1)).getRestaurant(0)
            Mockito.verify(restaurantLiveData, Mockito.times(1)).postValue(Mockito.any())
            Mockito.verify(loadingData, Mockito.times(1)).postValue(true)
            Mockito.verify(loadingData, Mockito.times(1)).postValue(false)
            Mockito.verify(showError, Mockito.times(1)).postValue(false)
            Mockito.verify(showError, Mockito.times(0)).postValue(true)
        }
    }

    @Test
    fun test_DetailsViewModel_fetchRestaurantDetails_fail() {
        Mockito.`when`(runBlocking {
            restaurantRepo.getRestaurant(Mockito.anyLong())
        }).then {runBlocking {
            throw Exception() //simulate error
        }}
        val detailsViewModel = DetailsViewModel(restaurantRepo, coroutineScope.dispatcher)
        runBlockingTest {
            detailsViewModel.fetchRestaurantDetails(0, restaurantLiveData, loadingData, showError)
            Mockito.verify(restaurantRepo, Mockito.times(1)).getRestaurant(0)
            Mockito.verify(restaurantLiveData, Mockito.times(0)).postValue(Mockito.any())
            Mockito.verify(loadingData, Mockito.times(1)).postValue(true)
            Mockito.verify(loadingData, Mockito.times(1)).postValue(false)
            Mockito.verify(showError, Mockito.times(1)).postValue(false)
            Mockito.verify(showError, Mockito.times(1)).postValue(true)
        }
    }

    @Test
    fun test_DetailsViewModel_fetchRestaurantDetails_whileAlreadyLoading() {
        Mockito.`when`(loadingData.value).thenReturn(true)
        val detailsViewModel = DetailsViewModel(restaurantRepo, coroutineScope.dispatcher)
        runBlockingTest {
            detailsViewModel.fetchRestaurantDetails(0, restaurantLiveData, loadingData, showError)
            Mockito.verify(restaurantRepo, Mockito.times(0)).getRestaurant(0)
            Mockito.verify(restaurantLiveData, Mockito.times(0)).postValue(Mockito.any())
            Mockito.verify(loadingData, Mockito.times(0)).postValue(Mockito.anyBoolean())
            Mockito.verify(showError, Mockito.times(0)).postValue(Mockito.anyBoolean())
        }
    }
}
