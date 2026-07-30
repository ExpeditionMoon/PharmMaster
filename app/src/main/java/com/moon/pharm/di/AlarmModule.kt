package com.moon.pharm.di

import com.moon.pharm.alarm.AndroidAlarmScheduler
import com.moon.pharm.domain.alarm.AlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class AlarmModule {

    @Binds
    @Singleton
    abstract fun bindAlarmScheduler(
        impl: AndroidAlarmScheduler
    ): AlarmScheduler
}
