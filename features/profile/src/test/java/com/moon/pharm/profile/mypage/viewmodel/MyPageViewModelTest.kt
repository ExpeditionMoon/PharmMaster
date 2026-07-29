package com.moon.pharm.profile.mypage.viewmodel

import com.moon.pharm.designsystem.common.UiMessage
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.LogoutUseCase
import com.moon.pharm.domain.usecase.user.ObserveCurrentUserUseCase
import com.moon.pharm.domain.usecase.user.ObserveMyPageConsultsUseCase
import com.moon.pharm.domain.usecase.user.UpdateNicknameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MyPageViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var authRepository: FakeAuthRepository
    private lateinit var userRepository: FakeUserRepository
    private lateinit var consultRepository: FakeConsultRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        authRepository = FakeAuthRepository()
        userRepository = FakeUserRepository()
        consultRepository = FakeConsultRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상담 목록 조회 실패에도 프로필을 유지한다`() = runTest(dispatcher) {
        consultRepository.myConsultResults += flowOf(DataResourceResult.Failure(IllegalStateException()))

        val viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(TEST_USER.nickName, viewModel.uiState.value.user?.nickName)
        assertTrue(viewModel.uiState.value.consultState is MyPageConsultState.Error)
    }

    @Test
    fun `상담 목록 재시도 시 새 조회 결과를 표시한다`() = runTest(dispatcher) {
        consultRepository.myConsultResults += flowOf(DataResourceResult.Failure(IllegalStateException()))
        consultRepository.myConsultResults += flowOf(DataResourceResult.Success(listOf(TEST_CONSULT)))
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.retryConsults()
        advanceUntilIdle()

        val consultState = viewModel.uiState.value.consultState as MyPageConsultState.Content
        assertEquals(1, consultState.consults.size)
        assertEquals(2, consultRepository.myConsultCalls)
    }

    @Test
    fun `상담 목록 조회 성공 시 상담 이력을 표시한다`() = runTest(dispatcher) {
        consultRepository.myConsultResults += flowOf(DataResourceResult.Success(listOf(TEST_CONSULT)))

        val viewModel = createViewModel()
        advanceUntilIdle()

        val consultState = viewModel.uiState.value.consultState as MyPageConsultState.Content
        assertEquals("1", consultState.historyText)
    }

    @Test
    fun `로그아웃 실패 시 현재 화면을 유지하고 오류 메시지를 표시한다`() = runTest(dispatcher) {
        authRepository.logoutResult = DataResourceResult.Failure(IllegalStateException("logout failed"))
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.logout()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLogoutSuccess)
        assertEquals(UiMessage.Error("logout failed"), viewModel.uiState.value.userMessage)
    }

    @Test
    fun `로그아웃 성공 시 로그인 화면 이동 상태를 전달한다`() = runTest(dispatcher) {
        authRepository.logoutResult = DataResourceResult.Success(Unit)
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.logout()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isLogoutSuccess)
    }

    @Test
    fun `닉네임 변경 실패 사유를 사용자 메시지로 표시한다`() = runTest(dispatcher) {
        userRepository.saveUserResult = DataResourceResult.Failure(IllegalStateException("nickname failed"))
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.updateNickname("new nickname")
        advanceUntilIdle()

        assertEquals(UiMessage.Error("nickname failed"), viewModel.uiState.value.userMessage)
    }

    private fun createViewModel(): MyPageViewModel {
        return MyPageViewModel(
            observeCurrentUserUseCase = ObserveCurrentUserUseCase(authRepository, userRepository),
            observeMyPageConsultsUseCase = ObserveMyPageConsultsUseCase(consultRepository),
            updateNicknameUseCase = UpdateNicknameUseCase(
                authRepository,
                userRepository,
                consultRepository,
                FakePharmacistRepository()
            ),
            logoutUseCase = LogoutUseCase(authRepository)
        )
    }

    private class FakeAuthRepository : AuthRepository {
        var logoutResult: DataResourceResult<Unit> = DataResourceResult.Success(Unit)

        override suspend fun createAccount(email: String, password: String) = DataResourceResult.Success(TEST_USER.id)
        override suspend fun login(email: String, password: String) = DataResourceResult.Success(TEST_USER.id)
        override suspend fun logout() = logoutResult
        override suspend fun deleteAccount() = DataResourceResult.Success(Unit)
        override fun getCurrentUserId(): String = TEST_USER.id
        override suspend fun sendPasswordResetEmail(email: String) = DataResourceResult.Success(Unit)
    }

    private class FakeUserRepository : UserRepository {
        var saveUserResult: DataResourceResult<Unit> = DataResourceResult.Success(Unit)

        override suspend fun saveUser(user: User) = saveUserResult
        override suspend fun saveUserLifeStyle(userId: String, lifeStyle: UserLifeStyle) = DataResourceResult.Success(Unit)
        override suspend fun isEmailDuplicated(email: String) = false
        override fun getUser(userId: String): Flow<DataResourceResult<User>> = flowOf(DataResourceResult.Success(TEST_USER))
        override suspend fun getUserOnce(userId: String): DataResourceResult<User> = DataResourceResult.Success(TEST_USER)
        override suspend fun getFcmToken(): String = ""
        override suspend fun updateFcmToken(userId: String, token: String) = DataResourceResult.Success(Unit)
    }

    private class FakeConsultRepository : ConsultRepository {
        val myConsultResults = mutableListOf<Flow<DataResourceResult<List<ConsultItem>>>>()
        var myConsultCalls = 0

        override fun createConsult(consultInfo: ConsultItem) = flowOf(DataResourceResult.Success(Unit))
        override fun getConsultItems() = flowOf<DataResourceResult<List<ConsultItem>>>(
            DataResourceResult.Success(emptyList())
        )
        override fun getConsultDetail(id: String) = flowOf(DataResourceResult.Failure(NoSuchElementException()))
        override fun getMyConsult(userId: String): Flow<DataResourceResult<List<ConsultItem>>> {
            return myConsultResults.getOrElse(myConsultCalls++) { flowOf(DataResourceResult.Success(emptyList())) }
        }
        override fun updateConsult(consultId: String, title: String, content: String, isPublic: Boolean) = flowOf(DataResourceResult.Success(Unit))
        override fun deleteConsult(consultId: String) = flowOf(DataResourceResult.Success(Unit))
        override fun getMyAnsweredConsultList(userId: String) = flowOf<DataResourceResult<List<ConsultItem>>>(
            DataResourceResult.Success(emptyList())
        )
        override fun registerAnswer(consultId: String, answer: ConsultAnswer) = flowOf(DataResourceResult.Failure(UnsupportedOperationException()))
        override fun deleteConsultAnswer(consultId: String) = flowOf(DataResourceResult.Success(Unit))
        override suspend fun uploadImage(uri: String, userId: String) = uri
        override suspend fun sendAnswerNotification(targetUserToken: String, consultId: String) = DataResourceResult.Success(Unit)
        override suspend fun sendNewConsultNotification(targetToken: String, consultId: String) = DataResourceResult.Success(Unit)
        override suspend fun updatePharmacistNicknameInAnswers(userId: String, newNickname: String) = DataResourceResult.Success(Unit)
    }

    private class FakePharmacistRepository : PharmacistRepository {
        override suspend fun savePharmacist(pharmacist: Pharmacist) = DataResourceResult.Success(Unit)
        override fun getPharmacistById(pharmacistId: String) = flowOf(DataResourceResult.Failure(NoSuchElementException()))
        override fun getPharmacistsByPlaceId(placeId: String) = flowOf<DataResourceResult<List<Pharmacist>>>(
            DataResourceResult.Success(emptyList())
        )
        override suspend fun updatePharmacistNickname(userId: String, newNickname: String) = DataResourceResult.Success(Unit)
    }

    private companion object {
        val TEST_USER = User(
            id = "user-id",
            email = "user@example.com",
            nickName = "nickname",
            userType = UserType.GENERAL,
            createdAt = 0L
        )
        val TEST_CONSULT = ConsultItem(
            id = "consult-id",
            userId = TEST_USER.id,
            nickName = TEST_USER.nickName,
            title = "title",
            content = "content",
            status = ConsultStatus.WAITING,
            isPublic = true,
            createdAt = 0L,
            images = emptyList<ConsultImage>()
        )
    }
}
