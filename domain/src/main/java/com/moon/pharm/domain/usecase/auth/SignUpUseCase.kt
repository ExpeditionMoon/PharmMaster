package com.moon.pharm.domain.usecase.auth

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(user: User, password: String, pharmacist: Pharmacist? = null) =
        repository.signUp(user, password, pharmacist)
}