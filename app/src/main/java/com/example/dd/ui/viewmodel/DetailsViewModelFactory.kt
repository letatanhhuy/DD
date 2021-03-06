package com.example.dd.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dd.data.RestaurantRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class DetailsViewModelFactory(
    private val restaurantRepo: RestaurantRepo,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(restaurantRepo, ioDispatcher) as T
    }
}
