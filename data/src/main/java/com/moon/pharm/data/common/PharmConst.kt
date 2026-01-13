package com.moon.pharm.data.common

const val DEFAULT_LOCATION_COORDINATE = 0.0
const val EMPTY_STRING = ""
const val TIME_DELIMITER = ":"

const val USER_COLLECTION = "users"
const val PHARMACIST_COLLECTION = "pharmacists"
const val PHARMACY_COLLECTION = "pharmacies"
const val SETTING_COLLECTION = "settings"
const val CONSULT_COLLECTION = "consults"
const val MEDICATION_COLLECTION = "medications"

const val INTAKE_RECORDS_COLLECTION = "intakeRecords"

const val DOCUMENT_LIFESTYLE = "lifeStyle"
const val FIELD_USER_ID = "userId"
const val FIELD_USER_EMAIL = "email"
const val FIELD_CREATED_AT = "createdAt"
const val FIELD_USER_TYPE = "userType"
const val FIELD_PLACE_ID = "placeId"
const val FIELD_MEDICATION_ID = "medicationId"
const val FIELD_SCHEDULE_ID = "scheduleId"

const val FIELD_RECORD_DATE = "recordDate"

const val USER_TYPE_PHARMACIST = "PHARMACIST"


private const val INTERNAL_PW_PREFIX = "auto_pass_"

fun generateInternalPassword(email: String): String {
    val hash = email.hashCode().toString().replace("-", "")
    return (INTERNAL_PW_PREFIX + hash + "000000").take(10)
}