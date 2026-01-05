package com.moon.pharm.data.common

const val USER_COLLECTION = "user_collection"
const val CONSULT_COLLECTION = "consult_collection"
const val MEDICATION_COLLECTION = "medication_collection"

const val FIELD_USER_EMAIL = "email"
const val FIELD_CREATED_AT = "createdAt"
const val FIELD_ALARM_TIME = "alarmTime"

private const val INTERNAL_PW_PREFIX = "auto_pass_"

fun generateInternalPassword(email: String): String {
    return "$INTERNAL_PW_PREFIX${email.hashCode()}"
}