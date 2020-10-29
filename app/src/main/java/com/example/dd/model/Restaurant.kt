package com.example.dd.model
import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("delivery_fee")
    val deliveryFee: Float,
    @SerializedName("status")
    val status: String,
    @SerializedName("cover_img_url")
    val coverImageUrl: String,
    @SerializedName("id")
    val  id: Long,
    @SerializedName("name")
    val  name: String,
    @SerializedName("description")
    val  description: String
)