package com.moon.pharm.data.mapper

import com.google.firebase.Timestamp
import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.data.common.FIELD_ANSWER
import com.moon.pharm.data.common.FIELD_CONTENT
import com.moon.pharm.data.common.FIELD_CREATED_AT
import com.moon.pharm.data.common.FIELD_NICKNAME
import com.moon.pharm.data.common.FIELD_PHARMACIST_ID
import com.moon.pharm.data.common.FIELD_PUBLIC
import com.moon.pharm.data.common.FIELD_STATUS
import com.moon.pharm.data.common.FIELD_TITLE
import com.moon.pharm.data.common.FIELD_USER_ID
import com.moon.pharm.data.datasource.remote.dto.ConsultAnswerDTO
import com.moon.pharm.data.datasource.remote.dto.ConsultImageDTO
import com.moon.pharm.data.datasource.remote.dto.ConsultItemDTO
import com.moon.pharm.domain.model.consult.ConsultStatus

private const val FIELD_IMAGES = "images"
private const val FIELD_IMAGE_NAME = "imageName"
private const val FIELD_IMAGE_URL = "imageUrl"
private const val FIELD_ANSWER_ID = "answerId"
private const val FIELD_ANSWER_PHARMACIST_NAME = "pharmacistName"

/**
 * 기존 상담 문서의 문자열 기반 이미지 목록을 감지한다.
 *
 * 현재 객체 스키마는 Firestore의 기본 DTO 역직렬화를 유지하고, 이 형식일 때만
 * 호환 매퍼를 사용한다.
 */
internal fun Map<String, Any?>.hasLegacyConsultImageUrls(): Boolean =
    (this[FIELD_IMAGES] as? List<*>)?.any { it is String } == true

/**
 * 문자열 기반 이미지 목록을 가진 기존 Firestore 상담 문서를 DTO로 변환한다.
 */
internal fun Map<String, Any?>.toLegacyConsultItemDto(documentId: String): ConsultItemDTO = ConsultItemDTO(
    id = documentId,
    userId = stringValue(FIELD_USER_ID),
    nickName = stringValue(FIELD_NICKNAME),
    pharmacistId = nullableStringValue(FIELD_PHARMACIST_ID),
    title = stringValue(FIELD_TITLE),
    content = stringValue(FIELD_CONTENT),
    status = stringValue(FIELD_STATUS).ifBlank { ConsultStatus.WAITING.name },
    isPublic = this[FIELD_PUBLIC] as? Boolean,
    images = imageDtos(),
    createdAt = this[FIELD_CREATED_AT] as? Timestamp,
    answer = answerDto()
)

private fun Map<String, Any?>.imageDtos(): List<ConsultImageDTO> =
    (this[FIELD_IMAGES] as? List<*>)
        .orEmpty()
        .mapNotNull { image ->
            when (image) {
                is String -> ConsultImageDTO(
                    imageName = image.fileNameFromUrl(),
                    imageUrl = image
                )

                is Map<*, *> -> ConsultImageDTO(
                    imageName = image.stringValue(FIELD_IMAGE_NAME),
                    imageUrl = image.stringValue(FIELD_IMAGE_URL)
                )

                else -> null
            }
        }

private fun Map<String, Any?>.answerDto(): ConsultAnswerDTO? =
    (this[FIELD_ANSWER] as? Map<*, *>)?.let { answer ->
        ConsultAnswerDTO(
            answerId = answer.stringValue(FIELD_ANSWER_ID),
            pharmacistId = answer.stringValue(FIELD_PHARMACIST_ID),
            pharmacistName = answer.stringValue(FIELD_ANSWER_PHARMACIST_NAME),
            content = answer.stringValue(FIELD_CONTENT),
            createdAt = answer[FIELD_CREATED_AT] as? Timestamp
        )
    }

private fun Map<*, *>.stringValue(key: String): String = this[key] as? String ?: EMPTY_STRING

private fun Map<String, Any?>.nullableStringValue(key: String): String? = this[key] as? String

private fun String.fileNameFromUrl(): String = substringBefore("?").substringAfterLast("/")
