package com.moon.pharm.domain.alarm

interface AlarmNotificationService {
    fun showMedicationAlarm(name: String, dosage: String, time: String, isGrouped: Boolean)
}