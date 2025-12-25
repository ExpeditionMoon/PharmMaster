package com.moon.pharm

import android.app.Application
import com.moon.pharm.di.AppContainer
import com.moon.pharm.di.AppContainerImpl
import com.moon.pharm.di.AppContainerProvider

class PharmMasterApplication : Application(), AppContainerProvider {

    private lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        pharmMasterApplication = this

        appContainer = AppContainerImpl()
    }

    override fun provideAppContainer(): AppContainer {
        return appContainer
    }

    companion object {
        private lateinit var pharmMasterApplication: PharmMasterApplication
        fun getAppContext() = pharmMasterApplication
    }
}