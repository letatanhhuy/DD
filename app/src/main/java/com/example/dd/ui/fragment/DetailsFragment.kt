package com.example.dd.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dd.R
import com.example.dd.constant.ProjectConstant
import com.example.dd.core.getCoreComponent
import com.example.dd.data.RestaurantRepo
import com.example.dd.model.Restaurant
import com.example.dd.ui.viewmodel.DetailsViewModel
import com.example.dd.ui.viewmodel.DetailsViewModelFactory
import com.example.dd.ui.viewmodel.MainViewModel
import com.example.dd.ui.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.details_view.view.*
import kotlinx.android.synthetic.main.details_view.view.progress_bar
import kotlinx.android.synthetic.main.details_view.view.txtRestaurantDescription
import kotlinx.android.synthetic.main.details_view.view.txtRestaurantName
import kotlinx.android.synthetic.main.main_view.view.*

class DetailsFragment : Fragment() {
    @VisibleForTesting
    lateinit var detailsViewModel: DetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsViewModel = ViewModelProvider(
            this,
            DetailsViewModelFactory(RestaurantRepo(getCoreComponent().getApplicationRestaurantApi()))
        ).get(DetailsViewModel::class.java)
        val restaurantID = this.arguments?.getLong(ProjectConstant.RESTAURANT_ID_TAG)
        if (savedInstanceState == null) {
            if (restaurantID != null) {
                detailsViewModel.fetchRestaurantDetails(restaurantID.toLong())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsViewModel.loadingData.observe(viewLifecycleOwner, {
            setLoadingRestaurants(view, it)
        })

        detailsViewModel.showError.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(this.context, R.string.network_error, Toast.LENGTH_SHORT).show()
                detailsViewModel.showError.value = false
            }
        })
        detailsViewModel.restaurantLiveData.observe(this.viewLifecycleOwner, {
            setRestaurantDetails(view, it)
        })
    }

    private fun setRestaurantDetails(view: View, restaurant: Restaurant) {
        view.txtRestaurantName.text = restaurant.name
        view.txtRestaurantFee.text = if (restaurant.deliveryFee == 0.0f) {
            getString(R.string.free_text)
        } else {
            restaurant.deliveryFee.toString()
        }
        view.txtRestaurantStatus.text = restaurant.status
        view.txtRestaurantDescription.text = restaurant.description
        Glide.with(view)
            .load(restaurant.coverImageUrl)
            .error(Glide.with(view).load(R.drawable.ic_image_not_found))
            .into(view.imageRestaurantCoverDetails)
    }

    private fun setLoadingRestaurants(view: View, isLoading: Boolean) {
        view.progress_bar.visibility = if (isLoading) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    companion object {
        private const val TAG = "DDASH:UI:DTF"
    }
}