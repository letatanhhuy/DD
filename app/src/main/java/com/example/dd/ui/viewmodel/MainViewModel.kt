package com.example.dd.ui.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dd.data.RestaurantRepo
import com.example.dd.model.Restaurant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val restaurantRepo: RestaurantRepo,
    private val ioCoroutineScope: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    var restaurantsLiveData: MutableLiveData<List<Restaurant>> = MutableLiveData()
        private set
    var loadingData: MutableLiveData<Boolean> = MutableLiveData(false)
        private set
    var showError: MutableLiveData<Boolean> = MutableLiveData(false)
        private set


    fun getRestaurants(offset: Int = 0) {
        getRestaurants(offset, restaurantsLiveData, loadingData, showError)
    }

    @VisibleForTesting
    fun getRestaurants(
        offset: Int = 0,
        _restaurantsLiveData: MutableLiveData<List<Restaurant>>,
        _loadingData: MutableLiveData<Boolean>,
        _showError: MutableLiveData<Boolean>
    ) {
        Log.d(TAG, "getRestaurants with offset $offset")
        if (_loadingData.value == true) {
            return
        }
        viewModelScope.launch(ioCoroutineScope) {
            runCatching {
                _loadingData.postValue(true)
                _showError.postValue(false)
                restaurantRepo.getRestaurants(offset)
            }.onSuccess { newRestaurantList ->
                Log.d(TAG, "getRestaurants succeed")
                _loadingData.postValue(false)
                val newList =
                    _restaurantsLiveData.value.orEmpty().toMutableList().plus(newRestaurantList)
                _restaurantsLiveData.postValue(newList)
            }.onFailure {
                Log.e(TAG, "get data failed with error: ${it.message}")
                _loadingData.postValue(false)
                _showError.postValue(true)
            }
        }
    }

    companion object {
        private const val TAG = "DDASH:UI:MVM"
    }
}