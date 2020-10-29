package com.example.dd.ui.common

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dd.MainActivity

fun RecyclerView.onRecyclerViewBottomReachedListener(callback: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            val endHasBeenReached = lastVisible + 5 >= totalItemCount
            if (totalItemCount > 0 && endHasBeenReached) {
                callback()
            }
        }
    })
}

fun RecyclerView.onRecyclerViewTopReachedListener(callback: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        var currentScrollPosition = 0
        override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
            currentScrollPosition += dy
            if (currentScrollPosition == 0) {
                callback()
            }
        }
    })
}

fun Fragment.switchContent(id: Int, fragment: Fragment) {
    if (this.context is MainActivity) {
        Log.d("Fragment","switchContent")
        val mainActivity = this.context as MainActivity
        mainActivity.switchContent(id, fragment)
    }
}

fun AppCompatActivity.switchContent(id: Int, fragment: Fragment) {
    Log.d("AppCompatActivity","switchContent main activity")
    val ft = supportFragmentManager.beginTransaction()
    ft.replace(id, fragment, fragment.toString())
    ft.addToBackStack(null)
    ft.commit()
}
