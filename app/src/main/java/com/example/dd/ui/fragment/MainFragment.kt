package com.example.dd.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dd.R
import com.example.dd.constant.ProjectConstant
import com.example.dd.core.getCoreComponent
import com.example.dd.data.RestaurantRepo
import com.example.dd.model.Restaurant
import com.example.dd.ui.adapter.MainListAdapter
import com.example.dd.ui.common.onRecyclerViewBottomReachedListener
import com.example.dd.ui.common.onRecyclerViewTopReachedListener
import com.example.dd.ui.common.switchContent
import com.example.dd.ui.viewmodel.MainViewModel
import com.example.dd.ui.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.main_view.view.*

class MainFragment : Fragment() {
    @VisibleForTesting
    lateinit var mainListAdapter:MainListAdapter
    @VisibleForTesting
    var restaurantList = emptyList<Restaurant>()
    @VisibleForTesting
    lateinit var mainViewModel: MainViewModel

    private var sharedpreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(RestaurantRepo(getCoreComponent().getApplicationRestaurantApi()))
        ).get(MainViewModel::class.java)

        if (savedInstanceState == null) {
            mainViewModel.getRestaurants()
        }
        sharedpreferences = this.context?.getSharedPreferences("DD_SharedPerferences", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_view, container, false)
        val clickListener = View.OnClickListener {
            val listRowView = it as View
            val position: Int = view.recyclerViewRestaurant.getChildAdapterPosition(listRowView)
            val restaurant = restaurantList[position]
            Log.d(TAG, "clickListener: ${restaurant.id}")
            val bundle = Bundle()
            bundle.putLong(ProjectConstant.RESTAURANT_ID_TAG, restaurant.id)
            val detailsFragment = DetailsFragment()
            detailsFragment.arguments = bundle
            switchContent(R.id.mainframe, detailsFragment)
        }

        val checkBoxListener = object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

            }

        }

        val restSet = sharedpreferences?.getStringSet(ProjectConstant.FAV_LIST, emptySet())
        val tempSet = mutableSetOf<String>()
        Log.d(TAG, "load fav list: ${restSet?.size}")
        if (restSet != null) {
            tempSet.addAll(restSet)
            Log.d(TAG, "check: ${tempSet.size}")
        }
        mainListAdapter = MainListAdapter(clickListener,sharedpreferences, tempSet)

        view.recyclerViewRestaurant.apply {
            val mainLayoutManager = LinearLayoutManager(this.context)
            layoutManager = mainLayoutManager
            adapter = mainListAdapter
            addItemDecoration(DividerItemDecoration(context, mainLayoutManager.orientation))
        }

        mainViewModel.loadingData.observe(viewLifecycleOwner, {
            setLoadingRestaurants(view, it)
        })

        mainViewModel.showError.observe(viewLifecycleOwner, {
            if (it) {
                Toast.makeText(this.context, "NETWORK ERROR", Toast.LENGTH_SHORT).show()
                mainViewModel.showError.value = false
            }
        })

        view.recyclerViewRestaurant.onRecyclerViewBottomReachedListener {
            Log.d(TAG, "onRecyclerViewBottomReachedListener")
            mainViewModel.getRestaurants(restaurantList.size)
        }

        view.recyclerViewRestaurant.onRecyclerViewTopReachedListener {
            Log.d(TAG, "onRecyclerViewTopReachedListener")
        }

        mainViewModel.restaurantsLiveData.observe(this.viewLifecycleOwner, {
            mainListAdapter.updateRestaurantList(it)
            restaurantList = it
        })

        return view
    }

    private fun setLoadingRestaurants(view: View, isLoading: Boolean) {
        if (isLoading) {
            view.progress_bar.visibility = View.VISIBLE
        } else {
            view.progress_bar.visibility = View.INVISIBLE
        }
    }

    companion object {
        private const val TAG = "DDASH:UI:MFM"
    }
}