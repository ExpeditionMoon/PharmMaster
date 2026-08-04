package com.moon.pharm.data.repository

import com.moon.pharm.data.datasource.AiDataSource
import com.moon.pharm.domain.model.prescription.PrescriptionException
import com.moon.pharm.domain.result.DataResourceResult
import com.squareup.moshi.Moshi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class DdiAiRepositoryImplTest {

    @Test
    fun `마크다운 코드 블록으로 감싼 AI 약 이름 응답을 파싱한다`() = runTest {
        val repository = DdiAiRepositoryImpl(
            aiDataSource = FakeAiDataSource("""
                ```json
                ["Tylenol", "Amoxicillin"]
                ```
            """.trimIndent()),
            moshi = Moshi.Builder().build()
        )

        val result = repository.extractDrugNamesFromText("prescription OCR text")

        assertEquals(
            DataResourceResult.Success(listOf("Tylenol", "Amoxicillin")),
            result
        )
    }

    @Test
    fun `빈 약 이름 응답은 약 이름 미발견 오류로 반환한다`() = runTest {
        val repository = DdiAiRepositoryImpl(
            aiDataSource = FakeAiDataSource("[]"),
            moshi = Moshi.Builder().build()
        )

        val result = repository.extractDrugNamesFromText("prescription OCR text")

        assertTrue(
            (result as DataResourceResult.Failure).exception is PrescriptionException.DrugNameNotFound
        )
    }

    @Test
    fun `감싸진 네트워크 원인이 있으면 네트워크 오류로 반환한다`() = runTest {
        val repository = DdiAiRepositoryImpl(
            aiDataSource = object : AiDataSource {
                override suspend fun analyzeDdi(drugs: List<String>): String =
                    throw AssertionError("Unexpected DDI analysis request")

                override suspend fun extractDrugNames(safeOcrText: String): String =
                    throw IllegalStateException(IOException())
            },
            moshi = Moshi.Builder().build()
        )

        val result = repository.extractDrugNamesFromText("prescription OCR text")

        assertTrue(
            (result as DataResourceResult.Failure).exception is PrescriptionException.Network
        )
    }

    private class FakeAiDataSource(
        private val extractResponse: String
    ) : AiDataSource {

        override suspend fun analyzeDdi(drugs: List<String>): String {
            throw AssertionError("Unexpected DDI analysis request")
        }

        override suspend fun extractDrugNames(safeOcrText: String): String = extractResponse
    }
}
