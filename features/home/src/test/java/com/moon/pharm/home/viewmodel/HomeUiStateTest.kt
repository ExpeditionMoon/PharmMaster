package com.moon.pharm.home.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Test

class HomeUiStateTest {

    @Test
    fun `등록된 약이 없으면 첫 복약 알림 등록 상태를 표시한다`() {
        val state = HomeUiState(
            medicationRegistrationStatus = HomeMedicationRegistrationStatus.NoMedication,
            isTodayMedicationLoaded = true
        )

        assertEquals(HomeMedicationSummary.NoRegisteredMedication, state.medicationSummary)
    }

    @Test
    fun `오늘 복약 데이터가 있으면 실제 오늘 복약 건수를 표시한다`() {
        val state = HomeUiState(
            medicationRegistrationStatus = HomeMedicationRegistrationStatus.Ongoing,
            todayMedicationCount = 2,
            todayCompletedCount = 1,
            lastCompletedTime = 1_000L,
            isTodayMedicationLoaded = true
        )

        assertEquals(
            HomeMedicationSummary.TodayMedication(
                totalCount = 2,
                completedCount = 1,
                lastCompletedTime = 1_000L
            ),
            state.medicationSummary
        )
    }

    @Test
    fun `주간 복약률은 실제 완료 건수와 예정 건수로 계산한다`() {
        val state = HomeUiState(
            weeklyMedicationCount = 6,
            weeklyCompletedCount = 4
        )

        assertEquals(66, state.weeklyAdherencePercent)
    }

    @Test
    fun `복약 데이터 조회에 실패하면 오류 상태를 표시한다`() {
        val state = HomeUiState(isMedicationDataLoadFailed = true)

        assertEquals(HomeMedicationSummary.LoadFailed, state.medicationSummary)
    }

    @Test
    fun `진행 중인 복약이 없으면 새 복약 알림 등록 상태를 표시한다`() {
        val state = HomeUiState(
            medicationRegistrationStatus = HomeMedicationRegistrationStatus.NoOngoingMedication,
            isTodayMedicationLoaded = true
        )

        assertEquals(HomeMedicationSummary.NoOngoingMedication, state.medicationSummary)
    }
}
