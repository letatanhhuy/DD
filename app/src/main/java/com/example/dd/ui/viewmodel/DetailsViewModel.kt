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

class DetailsViewModel(
    private val restaurantRepo: RestaurantRepo,
    private val ioCoroutineScope: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    var restaurantLiveData: MutableLiveData<Restaurant> = MutableLiveData()
        private set
    var loadingData: MutableLiveData<Boolean> = MutableLiveData(false)
        private set
    var showError: MutableLiveData<Boolean> = MutableLiveData(false)
        private set

    fun fetchRestaurantDetails(id: Long) {
        fetchRestaurantDetails(id, restaurantLiveData, loadingData, showError)
    }

    @VisibleForTesting
    fun fetchRestaurantDetails(
        _id: Long,
        _restaurantLiveData: MutableLiveData<Restaurant>,
        _loadingData: MutableLiveData<Boolean>,
        _showError: MutableLiveData<Boolean>
    ) {
        Log.d(TAG, "fetchRestaurantDetails with id: $_id")
        if (_loadingData.value == true) {
            return
        }
        viewModelScope.launch(ioCoroutineScope) {
            runCatching {
                _loadingData.postValue(true)
                _showError.postValue(false)
                restaurantRepo.getRestaurant(_id)
            }.onSuccess {
                Log.d(TAG, "fetchRestaurantDetails succeed")
                _loadingData.postValue(false)
                _restaurantLiveData.postValue(it)
            }.onFailure {
                Log.e(TAG, "fetch restaurant details failed with error: ${it.message}")
                _loadingData.postValue(false)
                _showError.postValue(true)
            }
        }
    }

    companion object {
        private const val TAG = "DDASH:UI:DVM"
    }
}