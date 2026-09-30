package com.moon.pharm.profile.settings.di

import com.moon.pharm.profile.settings.repository.AndroidPermissionSettingsRepository
import com.moon.pharm.profile.settings.repository.PermissionSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    abstract fun bindPermissionSettingsRepository(
        repository: AndroidPermissionSettingsRepository
    ): PermissionSettingsRepository
}
