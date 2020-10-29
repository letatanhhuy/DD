package com.example.dd.ui.fragment

import android.content.Context
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.dd.model.Restaurant
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFragmentInstrumentTest {
    private val status = "status"
    private val name = "name"
    private val url = "url"
    private val description = "description"
    private val id = 1L
    private val deliveryFee = 10.0f
    private lateinit var appContext: Context
    private lateinit var restaurants: List<Restaurant>


    @Before
    fun begin() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        restaurants = listOf(Restaurant(
            deliveryFee,
            status,
            url,
            id,
            name,
            description
        ))
    }

    @Test
    fun test_MainFragment_Basic() {
        val scenario = launchFragment<MainFragment>()
        scenario.onFragment { fragment ->
            fragment.mainViewModel.restaurantsLiveData.value = restaurants
            Truth.assertThat(restaurants.size).isEqualTo(fragment.mainListAdapter.itemCount)
        }

    }
}