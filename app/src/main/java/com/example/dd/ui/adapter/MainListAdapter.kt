package com.example.dd.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dd.R
import com.example.dd.model.Restaurant
import kotlinx.android.synthetic.main.main_view_row.view.*

class MainListAdapter(private var clickListener: View.OnClickListener) :
    RecyclerView.Adapter<MainListAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private var restaurantList = emptyList<Restaurant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_view_row, parent, false)
        )
    }

    fun updateRestaurantList(list: List<Restaurant>) {
        restaurantList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = restaurantList.size


    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(restaurantList[position].coverImageUrl)
            .error(Glide.with(holder.itemView).load(R.drawable.ic_image_not_found))
            .into(holder.itemView.imageRestaurantCover)

        holder.itemView.txtRestaurantName.text = restaurantList[position].name
        holder.itemView.txtRestaurantDescription.text = restaurantList[position].description
        holder.itemView.txtRestaurantTime.text = restaurantList[position].status
        holder.itemView.setOnClickListener(clickListener)
    }

    companion object {
        private const val TAG = "DDASH:UI:MLA"
    }
}
