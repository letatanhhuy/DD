package com.example.dd.core

import android.app.Activity
import androidx.fragment.app.Fragment

interface CoreComponentProvider {
    fun getCoreComponent():CoreComponent
}

fun Activity.getCoreComponent(): CoreComponent = (application as CoreComponentProvider).getCoreComponent()
fun Fragment.getCoreComponent(): CoreComponent = activity?.getCoreComponent()
    ?: throw IllegalStateException("There is no current activity attached to this fragment to lookup for a CoreComponentProvider")