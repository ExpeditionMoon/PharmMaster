package com.moon.pharm

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PharmMasterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, null)
    }
}