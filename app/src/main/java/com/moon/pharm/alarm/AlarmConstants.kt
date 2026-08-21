package com.moon.pharm.alarm

import com.moon.pharm.domain.alarm.NotificationChannelIds

object AlarmConstants {
    const val ACTION_MEDICATION_ALARM = "com.moon.pharm.action.MEDICATION_ALARM"

    const val NOTIFICATION_CHANNEL_ID = NotificationChannelIds.MEDICATION
    const val NOTIFICATION_CHANNEL_NAME = "복약 알림"

    const val EXTRA_MEDICATION_NAME = "key_medication_name"
    const val EXTRA_GROUP_MEDICATION_NAMES = "key_group_medication_names"
    const val EXTRA_MEDICATION_ID = "key_medication_id"
    const val EXTRA_INTAKE_GROUP_ID = "key_intake_group_id"
    const val EXTRA_REQUEST_CODE = "key_request_code"
    const val EXTRA_DOSAGE = "key_dosage"
    const val EXTRA_ALARM_TIME = "key_alarm_time"
    const val EXTRA_IS_GROUPED = "IS_GROUPED"
    const val EXTRA_REPEAT_TYPE = "key_repeat_type"
    const val EXTRA_WEEKLY_DAYS = "key_weekly_days"
    const val EXTRA_START_DATE = "key_start_date"
    const val EXTRA_END_DATE = "key_end_date"
    const val NO_DATE = Long.MIN_VALUE

    // 알림 인텐트 Key
    const val KEY_IS_FROM_ALARM = "IS_FROM_ALARM"
    const val KEY_TARGET_FRAGMENT = "TARGET_FRAGMENT"

    const val GROUP_KEY_PREFIX_TIME = "com.moon.pharm.GROUP_TIME_"
    const val GROUP_KEY_PREFIX_SINGLE = "com.moon.pharm.GROUP_SINGLE_"

    // 목적지
    const val FRAGMENT_MEDICATION = "MEDICATION_FRAGMENT"
}
