package com.moon.pharm.data.common

// 기본 유틸리티 (Basic Utilities)
const val EMPTY_STRING = ""
const val TIME_DELIMITER = ":"

// 위치 및 지도 설정 (Location & Maps)
const val DEFAULT_LOCATION_COORDINATE = 0.0

// Firestore 컬렉션 이름 (Collections)
const val USER_COLLECTION = "users"
const val PHARMACIST_COLLECTION = "pharmacists"
const val PHARMACY_COLLECTION = "pharmacies"
const val SETTING_COLLECTION = "settings"
const val CONSULT_COLLECTION = "consults"
const val MEDICATION_COLLECTION = "medications"
const val INTAKE_RECORDS_COLLECTION = "intakeRecords"

// Firestore 필드 및 문서 키 (Fields & Keys)
const val DOCUMENT_LIFESTYLE = "lifeStyle"

const val FIELD_USER_ID = "userId"
const val FIELD_PHARMACIST_ID = "pharmacistId"
const val FIELD_USER_EMAIL = "email"
const val FIELD_NICKNAME = "nickName"
const val FIELD_CREATED_AT = "createdAt"
const val FIELD_PLACE_ID = "placeId"
const val FIELD_MEDICATION_ID = "medicationId"
const val FIELD_SCHEDULE_ID = "scheduleId"
const val FIELD_RECORD_DATE = "recordDate"
const val FIELD_STATUS = "status"
const val FIELD_ANSWER = "answer"
const val FIELD_ANSWER_PHARMACIST_NAME = "answer.pharmacistName"
const val FIELD_FCM_TOKEN = "fcmToken"

// 에러 메시지 (Error Messages)
const val ERROR_MSG_PHARMACY_NOT_FOUND = "Pharmacy not found"
const val ERROR_MSG_UPLOAD_FAILED = "Upload failed"
const val ERROR_MSG_CONSULT_NOT_FOUND = "Consult document not found"

// Firebase Storage 설정 (Storage)
const val STORAGE_CONSULT_IMAGES = "consult_images"

// Alarm
const val KEY_MEDICATION_NAME = "key_medication_name"
const val KEY_DOSAGE = "key_dosage"
const val KEY_ALARM_TIME = "key_alarm_time"
const val KEY_IS_GROUPED = "IS_GROUPED"

const val NOTIFICATION_CHANNEL_ID = "medication_alarm_channel"
const val NOTIFICATION_CHANNEL_NAME = "복약 알림"
const val TIME_PARTS_SIZE = 2
const val ALARM_NEXT_DAY_OFFSET = 1L