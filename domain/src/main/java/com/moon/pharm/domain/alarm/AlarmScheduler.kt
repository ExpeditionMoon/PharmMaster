package com.moon.pharm.domain.alarm

import com.moon.pharm.domain.model.medication.Medication

interface AlarmScheduler {
    fun schedule(medication: Medication)
    fun cancel(medication: Medication)
}