package com.example.dd.ui.fragment

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.dd.R
import com.example.dd.constant.ProjectConstant
import com.example.dd.model.Restaurant
import com.google.common.truth.Truth
import kotlinx.android.synthetic.main.details_view.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DetailsFragmentInstrumentTest {
    private val status = "status"
    private val name = "name"
    private val url = "url"
    private val description = "description"
    private val id = 1L
    private val deliveryFee = 10.0f
    private lateinit var appContext:Context
    private lateinit var resources: Resources


    @Before
    fun begin() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        resources = appContext.resources
    }

    @Test
    fun test_DetailsFragment_Basic() {
        val fragmentArgs = Bundle().apply {
            putLong(ProjectConstant.RESTAURANT_ID_TAG, id)
        }

        val scenario = launchFragment<DetailsFragment>(fragmentArgs)

        scenario.onFragment { fragment ->
            val foundId = fragmentArgs.getLong(ProjectConstant.RESTAURANT_ID_TAG)
            fragment.detailsViewModel.restaurantLiveData.value = Restaurant(
                deliveryFee,
                status,
                url,
                foundId,
                name,
                description
            )

            Truth.assertThat(id).isEqualTo(foundId)
            Truth.assertThat(deliveryFee.toString()).isEqualTo(fragment.txtRestaurantFee.text)
            Truth.assertThat(description).isEqualTo(fragment.txtRestaurantDescription.text)
            Truth.assertThat(name).isEqualTo(fragment.txtRestaurantName.text)
            Truth.assertThat(status).isEqualTo(fragment.txtRestaurantStatus.text)
        }
    }

    @Test
    fun test_DetailsFragment_ZeroDeliverFee() {
        val scenario = launchFragment<DetailsFragment>()
        scenario.onFragment { fragment ->
            fragment.detailsViewModel.restaurantLiveData.value = Restaurant(
                0.0f,
                status,
                url,
                id,
                name,
                description
            )
            Truth.assertThat(resources.getString(R.string.free_text)).isEqualTo(fragment.txtRestaurantFee.text)
            Truth.assertThat(description).isEqualTo(fragment.txtRestaurantDescription.text)
            Truth.assertThat(name).isEqualTo(fragment.txtRestaurantName.text)
            Truth.assertThat(status).isEqualTo(fragment.txtRestaurantStatus.text)
        }
    }
}