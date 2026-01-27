package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.pharmacist.SavePharmacistUseCase
import com.moon.pharm.domain.usecase.pharmacy.SavePharmacyUseCase
import com.moon.pharm.domain.usecase.user.SaveUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val saveUserUseCase: SaveUserUseCase,
    private val savePharmacyUseCase: SavePharmacyUseCase,
    private val savePharmacistUseCase: SavePharmacistUseCase
) {
    operator fun invoke(
        user: User,
        password: String,
        pharmacist: Pharmacist?,
        pharmacy: Pharmacy?
    ): Flow<DataResourceResult<Unit>> = flow {

        emit(DataResourceResult.Loading)

        val result = authRepository.createAccount(user.email, password)

        if (result is DataResourceResult.Failure) {
            emit(DataResourceResult.Failure(result.exception))
            return@flow
        }

        if (result !is DataResourceResult.Success) {
            emit(DataResourceResult.Failure(Exception("알 수 없는 오류 발생")))
            return@flow
        }

        val uid = result.resultData

        try {
            val userWithId = user.copy(id = uid)

            val saveUserResult = saveUserUseCase(userWithId)
            if (saveUserResult is DataResourceResult.Failure) {
                throw saveUserResult.exception
            }

            if (user.userType == UserType.PHARMACIST && pharmacist != null) {
                if (pharmacy != null) {
                    val savePharmacyResult = savePharmacyUseCase(pharmacy)
                    if (savePharmacyResult is DataResourceResult.Failure) {
                        throw Exception("약국 저장 실패: ${savePharmacyResult.exception.message}")
                    }
                }
                val pharmacistWithId = pharmacist.copy(userId = uid)
                val savePharmResult = savePharmacistUseCase(pharmacistWithId)

                if (savePharmResult is DataResourceResult.Failure) {
                    throw savePharmResult.exception
                }
            }
            emit(DataResourceResult.Success(Unit))

        } catch (e: Exception) {
            authRepository.deleteAccount()
            emit(DataResourceResult.Failure(e))
        }
    }
}