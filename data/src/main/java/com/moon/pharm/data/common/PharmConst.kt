package com.moon.pharm.data.common

// 1. 기본 유틸리티 (Basic Utilities)
const val EMPTY_STRING = ""
const val TIME_DELIMITER = ":"

// 2. 위치 및 지도 설정 (Location & Maps)
const val DEFAULT_LOCATION_COORDINATE = 0.0

// Google Places API 관련 설정
const val PLACE_TYPE_PHARMACY = "PHARMACY"
const val QUERY_PHARMACY_KOREAN = "약국"
const val SEARCH_RADIUS_METERS = 1000.0
const val SEARCH_MAX_RESULT_COUNT = 20

// 3. Firestore 컬렉션 이름 (Collections)
const val USER_COLLECTION = "users"
const val PHARMACIST_COLLECTION = "pharmacists"
const val PHARMACY_COLLECTION = "pharmacies"
const val SETTING_COLLECTION = "settings"
const val CONSULT_COLLECTION = "consults"
const val MEDICATION_COLLECTION = "medications"
const val INTAKE_RECORDS_COLLECTION = "intakeRecords"

// 4. Firestore 필드 및 문서 키 (Fields & Keys)
const val DOCUMENT_LIFESTYLE = "lifeStyle"

const val FIELD_USER_ID = "userId"
const val FIELD_USER_EMAIL = "email"
const val FIELD_CREATED_AT = "createdAt"
const val FIELD_USER_TYPE = "userType"
const val FIELD_PLACE_ID = "placeId"
const val FIELD_MEDICATION_ID = "medicationId"
const val FIELD_SCHEDULE_ID = "scheduleId"
const val FIELD_RECORD_DATE = "recordDate"

// 5. 타입 및 상태 상수 (Types & Status)
const val USER_TYPE_PHARMACIST = "PHARMACIST"

// 6. 에러 메시지 (Error Messages)
const val ERROR_MSG_PHARMACY_NOT_FOUND = "Pharmacy not found"
const val ERROR_MSG_UPLOAD_FAILED = "Upload failed"

// 7. Firebase Storage 설정 (Storage)
const val STORAGE_CONSULT_IMAGES = "consult_images"