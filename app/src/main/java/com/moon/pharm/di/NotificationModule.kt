package com.moon.pharm.di

import com.moon.pharm.alarm.AlarmNotificationServiceImpl
import com.moon.pharm.domain.alarm.AlarmNotificationService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindAlarmNotificationService(
        impl: AlarmNotificationServiceImpl
    ): AlarmNotificationService
}