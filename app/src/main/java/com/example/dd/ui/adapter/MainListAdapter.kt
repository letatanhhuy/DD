package com.example.dd.ui.adapter

import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dd.R
import com.example.dd.constant.ProjectConstant
import com.example.dd.model.Restaurant
import kotlinx.android.synthetic.main.main_view_row.view.*

class MainListAdapter(private var clickListener: View.OnClickListener, private var sharedPreferences: SharedPreferences?, private var favSet: Set<String>) :
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

        val id = restaurantList[position].id.toString()

        val checkBoxListener = object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(p0: CompoundButton?, checkValue: Boolean) {
                Log.d(TAG, "favourite restaurant id ${restaurantList[position].id} check value:$checkValue")
                if (sharedPreferences != null) {
                    val restSet = sharedPreferences?.getStringSet(ProjectConstant.FAV_LIST, emptySet())
                    val tempSet = mutableSetOf<String>()
                    if (restSet != null) {
                        tempSet.addAll(restSet)
                    }

                    Log.d(TAG, "before save ${tempSet.size}")
                    if (id.isNotEmpty()) {
                        if (checkValue) {
                            if (!tempSet.contains(id)) {
                                tempSet.add(id)
                            }
                        } else {
                            if (tempSet.contains(id)) {
                                tempSet.remove(id)
                            }
                        }
                        Log.d(TAG, "after save ${tempSet.size}")
                        val editor = sharedPreferences?.edit()
                        editor?.apply {
                            putStringSet(ProjectConstant.FAV_LIST, tempSet)
                            commit()
                        }
                    }

                }


            }
        }
        holder.itemView.checkboxFavourite.setOnCheckedChangeListener(checkBoxListener)
        holder.itemView.checkboxFavourite.isChecked = favSet.contains(id)
    }

    companion object {
        private const val TAG = "DDASH:UI:MLA"
    }
}
