package com.moon.pharm

import android.app.Application

class PharmMasterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        pharmMasterApplication = this
    }

    companion object {
        private lateinit var pharmMasterApplication: PharmMasterApplication
        fun getAppContext() = pharmMasterApplication
    }
}