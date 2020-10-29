package com.example.dd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dd.ui.fragment.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainframe, MainFragment())
                .commitNow()
        }
    }
    companion object {
        private const val TAG = "DDASH:MM:MA"
    }
}