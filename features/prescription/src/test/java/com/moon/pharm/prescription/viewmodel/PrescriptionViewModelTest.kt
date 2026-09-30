package com.moon.pharm.prescription.viewmodel

import android.net.Uri
import com.moon.pharm.domain.model.ddi.DdiResult
import com.moon.pharm.domain.model.prescription.PrescriptionException
import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.prescription.ExtractDrugNamesFromOcrUseCase
import com.moon.pharm.prescription.ocr.OcrTextExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
class PrescriptionViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var ocrTextExtractor: FakeOcrTextExtractor
    private lateinit var ddiRepository: FakeDdiRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        ocrTextExtractor = FakeOcrTextExtractor()
        ddiRepository = FakeDdiRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `AI가 추출한 약 이름을 검토 화면으로 전달한다`() = runTest(dispatcher) {
        ddiRepository.extractResult = DataResourceResult.Success(listOf("타이레놀", "아목시실린"))
        val viewModel = createViewModel()
        val event = async { viewModel.uiEvent.first() }

        viewModel.onTextRecognized("타이레놀 아목시실린")
        advanceUntilIdle()

        assertEquals(
            PrescriptionUiEvent.NavigateToMedicationReview(listOf("타이레놀", "아목시실린")),
            event.await()
        )
    }

    @Test
    fun `갤러리 OCR 결과를 검토 화면으로 전달한다`() = runTest(dispatcher) {
        val imageUri: Uri = mock()
        ocrTextExtractor.result = Result.success("처방전 약 이름")
        ddiRepository.extractResult = DataResourceResult.Success(listOf("타이레놀"))
        val viewModel = createViewModel()
        val event = async { viewModel.uiEvent.first() }

        viewModel.analyzeImageFromUri(imageUri)
        advanceUntilIdle()

        assertEquals(PrescriptionUiEvent.NavigateToMedicationReview(listOf("타이레놀")), event.await())
        assertEquals(imageUri, ocrTextExtractor.lastUri)
    }

    @Test
    fun `AI 추출 네트워크 오류를 별도 오류 상태로 표시한다`() = runTest(dispatcher) {
        ddiRepository.extractResult = DataResourceResult.Failure(PrescriptionException.Network)
        val viewModel = createViewModel()

        viewModel.onTextRecognized("처방전 약 이름")
        advanceUntilIdle()

        assertEquals(PrescriptionUiState.Error(PrescriptionError.NETWORK), viewModel.uiState.value)
        assertEquals(1, ddiRepository.extractCalls)
    }

    @Test
    fun `빈 OCR 결과는 OCR 오류 UI 상태로 표시된다`() = runTest(dispatcher) {
        val viewModel = createViewModel()

        viewModel.onTextRecognized(" ")
        advanceUntilIdle()

        assertEquals(PrescriptionUiState.Error(PrescriptionError.OCR), viewModel.uiState.value)
        assertEquals(0, ddiRepository.extractCalls)
    }

    @Test
    fun `빈 AI 추출 결과를 별도 오류 상태로 표시한다`() = runTest(dispatcher) {
        ddiRepository.extractResult = DataResourceResult.Success(listOf(" ", ""))
        val viewModel = createViewModel()

        viewModel.onTextRecognized("처방전 약 이름")
        advanceUntilIdle()

        assertEquals(PrescriptionUiState.Error(PrescriptionError.EMPTY_RESULT), viewModel.uiState.value)
    }

    @Test
    fun `부분 인식 결과는 비어 있는 항목과 중복을 제거해 검토 화면으로 이동한다`() = runTest(dispatcher) {
        ddiRepository.extractResult = DataResourceResult.Success(listOf("타이레놀", " ", "타이레놀"))
        val viewModel = createViewModel()
        val event = async { viewModel.uiEvent.first() }

        viewModel.onTextRecognized("처방전 약 이름")
        advanceUntilIdle()

        assertEquals(PrescriptionUiEvent.NavigateToMedicationReview(listOf("타이레놀")), event.await())
    }

    @Test
    fun `네트워크 오류 후 다시 시도하면 검토 화면으로 이동한다`() = runTest(dispatcher) {
        ddiRepository.extractResult = DataResourceResult.Failure(PrescriptionException.Network)
        val viewModel = createViewModel()

        viewModel.onTextRecognized("처방전 약 이름")
        advanceUntilIdle()
        ddiRepository.extractResult = DataResourceResult.Success(listOf("타이레놀"))
        val event = async { viewModel.uiEvent.first() }

        viewModel.retry()
        advanceUntilIdle()

        assertEquals(PrescriptionUiEvent.NavigateToMedicationReview(listOf("타이레놀")), event.await())
    }

    @Test
    fun `직접 입력을 선택하면 빈 검토 화면으로 이동한다`() = runTest(dispatcher) {
        val viewModel = createViewModel()
        val event = async { viewModel.uiEvent.first() }

        viewModel.openManualMedicationReview()
        advanceUntilIdle()

        assertEquals(PrescriptionUiEvent.NavigateToMedicationReview(emptyList()), event.await())
        assertEquals(PrescriptionUiState.Idle, viewModel.uiState.value)
    }

    private fun createViewModel(): PrescriptionViewModel = PrescriptionViewModel(
        ocrTextExtractor = ocrTextExtractor,
        extractDrugNamesUseCase = ExtractDrugNamesFromOcrUseCase(ddiRepository)
    )

    private class FakeOcrTextExtractor : OcrTextExtractor {
        var result: Result<String> = Result.success("")
        var lastUri: Uri? = null

        override suspend fun extractTextFromUri(imageUri: Uri): Result<String> {
            lastUri = imageUri
            return result
        }
    }

    private class FakeDdiRepository : DdiRepository {
        var extractResult: DataResourceResult<List<String>> = DataResourceResult.Success(emptyList())
        var extractCalls = 0

        override suspend fun analyzeDrugInteractions(drugs: List<String>): DataResourceResult<DdiResult> {
            throw AssertionError("Unexpected DDI analysis request")
        }

        override suspend fun extractDrugNamesFromText(ocrRawText: String): DataResourceResult<List<String>> {
            extractCalls += 1
            return extractResult
        }
    }
}
