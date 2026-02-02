package com.moon.pharm.data.di

import com.moon.pharm.data.alarm.AndroidAlarmScheduler
import com.moon.pharm.domain.alarm.AlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmModule {

    @Binds
    @Singleton
    abstract fun bindAlarmScheduler(
        impl: AndroidAlarmScheduler
    ): AlarmScheduler
}