package com.moon.pharm.domain.usecase.auth

import javax.inject.Inject

data class AuthUseCases @Inject constructor(
    val signUp: SignUpUseCase,
    val getCurrentUserId: GetCurrentUserIdUseCase,
    val checkEmailDuplicate: CheckEmailDuplicateUseCase,

    val getPharmacistDetail: GetPharmacistDetailUseCase,
    val getPharmacistsByPharmacy: GetPharmacistsByPharmacyUseCase
)