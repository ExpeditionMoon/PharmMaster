package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserLifeStyle
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val pharmacistRepository: PharmacistRepository
) {
    operator fun invoke(
        user: User,
        password: String,
        pharmacist: Pharmacist? = null
    ): Flow<DataResourceResult<Unit>> = flow {

        emit(DataResourceResult.Loading)

        authRepository.createAccount(user.email, password).collect { result ->
            when (result) {
                is DataResourceResult.Success -> {
                    val uid = result.resultData

                    try {
                        val userWithId = user.copy(id = uid)
                        val saveUserResult = userRepository.saveUser(userWithId)

                        if (saveUserResult is DataResourceResult.Failure) {
                            throw saveUserResult.exception
                        }

                        val defaultLifeStyle = UserLifeStyle(userId = uid)
                        userRepository.saveUserLifeStyle(uid, defaultLifeStyle)

                        if (user.userType == UserType.PHARMACIST && pharmacist != null) {
                            val pharmacistWithId = pharmacist.copy(userId = uid)
                            val savePharmResult = pharmacistRepository.savePharmacist(pharmacistWithId)

                            if (savePharmResult is DataResourceResult.Failure) {
                                throw savePharmResult.exception
                            }
                        }
                        emit(DataResourceResult.Success(Unit))

                    } catch (e: Exception) {
                        authRepository.deleteAccount()

                        emit(DataResourceResult.Failure(Exception("회원가입 중 오류가 발생하여 취소되었습니다. (${e.message})")))
                    }
                }

                is DataResourceResult.Failure -> {
                    emit(DataResourceResult.Failure(result.exception))
                }

                is DataResourceResult.Loading -> {
                }
            }
        }
    }
}