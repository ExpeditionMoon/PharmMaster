package com.moon.pharm

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.google.android.gms.maps.MapsInitializer
import com.moon.pharm.common.AppConstants.CACHE_MEMORY_PERCENT
import com.moon.pharm.common.AppConstants.CACHE_SIZE_BYTES
import com.moon.pharm.common.AppConstants.DIR_IMAGE_CACHE
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PharmMasterApplication : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        try {
            MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, CACHE_MEMORY_PERCENT)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve(DIR_IMAGE_CACHE))
                    .maxSizeBytes(CACHE_SIZE_BYTES)
                    .build()
            }
            .crossfade(true)
            .build()
    }
}