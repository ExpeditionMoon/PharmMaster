package com.moon.pharm.alarm

object AlarmConstants {
    const val ACTION_MEDICATION_ALARM = "com.moon.pharm.action.MEDICATION_ALARM"

    const val NOTIFICATION_CHANNEL_ID = "medication_alarm_channel"
    const val NOTIFICATION_CHANNEL_NAME = "복약 알림"

    const val EXTRA_MEDICATION_NAME = "key_medication_name"
    const val EXTRA_DOSAGE = "key_dosage"
    const val EXTRA_ALARM_TIME = "key_alarm_time"
    const val EXTRA_IS_GROUPED = "IS_GROUPED"

    const val TIME_DELIMITER = ":"
    const val TIME_PARTS_SIZE = 2
    const val NEXT_DAY_OFFSET = 1L

    // 알림 인텐트 Key
    const val KEY_IS_FROM_ALARM = "IS_FROM_ALARM"
    const val KEY_TARGET_FRAGMENT = "TARGET_FRAGMENT"

    const val GROUP_KEY_PREFIX_TIME = "com.moon.pharm.GROUP_TIME_"
    const val GROUP_KEY_PREFIX_SINGLE = "com.moon.pharm.GROUP_SINGLE_"

    // 목적지
    const val FRAGMENT_MEDICATION = "MEDICATION_FRAGMENT"
}
