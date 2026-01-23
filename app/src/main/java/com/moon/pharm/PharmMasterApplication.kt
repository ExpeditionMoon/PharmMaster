package com.moon.pharm

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PharmMasterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}