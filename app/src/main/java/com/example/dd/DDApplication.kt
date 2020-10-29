package com.example.dd

import android.app.Application
import com.example.dd.core.CoreComponent
import com.example.dd.core.CoreComponentProvider

class DDApplication:Application(),CoreComponentProvider {
    private val coreComponent = CoreComponent()
    override fun getCoreComponent(): CoreComponent {
        return coreComponent
    }

}