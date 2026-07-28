package com.moon.pharm.di

import com.moon.pharm.domain.alarm.AlarmScheduler
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.repository.DrugSearchRepository
import com.moon.pharm.domain.repository.LocationRepository
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.auth.LoginUseCase
import com.moon.pharm.domain.usecase.auth.LogoutUseCase
import com.moon.pharm.domain.usecase.auth.SignUpUseCase
import com.moon.pharm.domain.usecase.auth.ValidateLoginFormUseCase
import com.moon.pharm.domain.usecase.auth.ValidateSignUpFormUseCase
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import com.moon.pharm.domain.usecase.consult.CreateConsultUseCase
import com.moon.pharm.domain.usecase.consult.DeleteConsultAnswerUseCase
import com.moon.pharm.domain.usecase.consult.DeleteConsultUseCase
import com.moon.pharm.domain.usecase.consult.GetConsultDetailUseCase
import com.moon.pharm.domain.usecase.consult.GetConsultForEditUseCase
import com.moon.pharm.domain.usecase.consult.GetConsultItemsUseCase
import com.moon.pharm.domain.usecase.consult.GetCurrentUserConsultProfileUseCase
import com.moon.pharm.domain.usecase.consult.GetMyConsultListUseCase
import com.moon.pharm.domain.usecase.consult.RegisterAnswerUseCase
import com.moon.pharm.domain.usecase.consult.SearchPharmacistsByPlaceIdUseCase
import com.moon.pharm.domain.usecase.consult.SendAnswerNotificationUseCase
import com.moon.pharm.domain.usecase.consult.SendNewConsultNotificationUseCase
import com.moon.pharm.domain.usecase.consult.UpdateConsultUseCase
import com.moon.pharm.domain.usecase.consult.UploadConsultImagesUseCase
import com.moon.pharm.domain.usecase.consult.ValidateConsultFormUseCase
import com.moon.pharm.domain.usecase.ddi.AnalyzeDdiUseCase
import com.moon.pharm.domain.usecase.drug.SearchDrugUseCase
import com.moon.pharm.domain.usecase.medication.DeleteMedicationUseCase
import com.moon.pharm.domain.usecase.medication.GetDailyIntakeRecordsUseCase
import com.moon.pharm.domain.usecase.medication.GetMedicationHistoryItemsUseCase
import com.moon.pharm.domain.usecase.medication.GetMedicationsUseCase
import com.moon.pharm.domain.usecase.medication.GetMonthlyIntakeRecordsUseCase
import com.moon.pharm.domain.usecase.medication.ObserveTodayMedicationItemsUseCase
import com.moon.pharm.domain.usecase.medication.SaveMedicationUseCase
import com.moon.pharm.domain.usecase.medication.ToggleIntakeCheckUseCase
import com.moon.pharm.domain.usecase.medication.ValidateMedicationEntryUseCase
import com.moon.pharm.domain.usecase.pharmacy.GetNearbyPharmaciesCurrentLocationUseCase
import com.moon.pharm.domain.usecase.pharmacy.SearchNearbyPharmaciesUseCase
import com.moon.pharm.domain.usecase.pharmacy.SearchPharmacyUseCase
import com.moon.pharm.domain.usecase.prescription.ExtractDrugNamesFromOcrUseCase
import com.moon.pharm.domain.usecase.user.CheckEmailDuplicatedUseCase
import com.moon.pharm.domain.usecase.user.GetUserOnceUseCase
import com.moon.pharm.domain.usecase.user.ObserveCurrentUserNicknameUseCase
import com.moon.pharm.domain.usecase.user.ObserveCurrentUserUseCase
import com.moon.pharm.domain.usecase.user.ObserveMyPageDataUseCase
import com.moon.pharm.domain.usecase.user.SaveUserUseCase
import com.moon.pharm.domain.usecase.user.SyncFcmTokenUseCase
import com.moon.pharm.domain.usecase.user.UpdateNicknameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object UseCaseModule {

    @Provides
    fun provideGetCurrentUserIdUseCase(authRepository: AuthRepository): GetCurrentUserIdUseCase {
        return GetCurrentUserIdUseCase(authRepository)
    }

    @Provides
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    fun provideLogoutUseCase(authRepository: AuthRepository): LogoutUseCase {
        return LogoutUseCase(authRepository)
    }

    @Provides
    fun provideSignUpUseCase(
        authRepository: AuthRepository,
        saveUserUseCase: SaveUserUseCase,
        pharmacyRepository: PharmacyRepository,
        pharmacistRepository: PharmacistRepository
    ): SignUpUseCase {
        return SignUpUseCase(authRepository, saveUserUseCase, pharmacyRepository, pharmacistRepository)
    }

    @Provides
    fun provideValidateLoginFormUseCase(): ValidateLoginFormUseCase {
        return ValidateLoginFormUseCase()
    }

    @Provides
    fun provideValidateSignUpFormUseCase(): ValidateSignUpFormUseCase {
        return ValidateSignUpFormUseCase()
    }

    @Provides
    fun provideAnalyzeDdiUseCase(ddiRepository: DdiRepository): AnalyzeDdiUseCase {
        return AnalyzeDdiUseCase(ddiRepository)
    }

    @Provides
    fun provideSearchDrugUseCase(drugSearchRepository: DrugSearchRepository): SearchDrugUseCase {
        return SearchDrugUseCase(drugSearchRepository)
    }

    @Provides
    fun provideDeleteMedicationUseCase(medicationRepository: MedicationRepository): DeleteMedicationUseCase {
        return DeleteMedicationUseCase(medicationRepository)
    }

    @Provides
    fun provideGetDailyIntakeRecordsUseCase(
        medicationRepository: MedicationRepository
    ): GetDailyIntakeRecordsUseCase {
        return GetDailyIntakeRecordsUseCase(medicationRepository)
    }

    @Provides
    fun provideGetMedicationsUseCase(medicationRepository: MedicationRepository): GetMedicationsUseCase {
        return GetMedicationsUseCase(medicationRepository)
    }

    @Provides
    fun provideObserveTodayMedicationItemsUseCase(
        medicationRepository: MedicationRepository
    ): ObserveTodayMedicationItemsUseCase {
        return ObserveTodayMedicationItemsUseCase(medicationRepository)
    }

    @Provides
    fun provideGetMonthlyIntakeRecordsUseCase(
        medicationRepository: MedicationRepository
    ): GetMonthlyIntakeRecordsUseCase {
        return GetMonthlyIntakeRecordsUseCase(medicationRepository)
    }

    @Provides
    fun provideGetMedicationHistoryItemsUseCase(
        medicationRepository: MedicationRepository
    ): GetMedicationHistoryItemsUseCase {
        return GetMedicationHistoryItemsUseCase(medicationRepository)
    }

    @Provides
    fun provideSaveMedicationUseCase(
        medicationRepository: MedicationRepository,
        alarmScheduler: AlarmScheduler
    ): SaveMedicationUseCase {
        return SaveMedicationUseCase(medicationRepository, alarmScheduler)
    }

    @Provides
    fun provideToggleIntakeCheckUseCase(medicationRepository: MedicationRepository): ToggleIntakeCheckUseCase {
        return ToggleIntakeCheckUseCase(medicationRepository)
    }

    @Provides
    fun provideValidateMedicationEntryUseCase(): ValidateMedicationEntryUseCase {
        return ValidateMedicationEntryUseCase()
    }

    @Provides
    fun provideSearchNearbyPharmaciesUseCase(pharmacyRepository: PharmacyRepository): SearchNearbyPharmaciesUseCase {
        return SearchNearbyPharmaciesUseCase(pharmacyRepository)
    }

    @Provides
    fun provideSearchPharmacyUseCase(pharmacyRepository: PharmacyRepository): SearchPharmacyUseCase {
        return SearchPharmacyUseCase(pharmacyRepository)
    }

    @Provides
    fun provideGetNearbyPharmaciesCurrentLocationUseCase(
        locationRepository: LocationRepository,
        pharmacyRepository: PharmacyRepository
    ): GetNearbyPharmaciesCurrentLocationUseCase {
        return GetNearbyPharmaciesCurrentLocationUseCase(locationRepository, pharmacyRepository)
    }

    @Provides
    fun provideExtractDrugNamesFromOcrUseCase(ddiRepository: DdiRepository): ExtractDrugNamesFromOcrUseCase {
        return ExtractDrugNamesFromOcrUseCase(ddiRepository)
    }

    @Provides
    fun provideCheckEmailDuplicatedUseCase(userRepository: UserRepository): CheckEmailDuplicatedUseCase {
        return CheckEmailDuplicatedUseCase(userRepository)
    }

    @Provides
    fun provideGetUserOnceUseCase(userRepository: UserRepository): GetUserOnceUseCase {
        return GetUserOnceUseCase(userRepository)
    }

    @Provides
    fun provideObserveCurrentUserUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): ObserveCurrentUserUseCase {
        return ObserveCurrentUserUseCase(authRepository, userRepository)
    }

    @Provides
    fun provideObserveCurrentUserNicknameUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): ObserveCurrentUserNicknameUseCase {
        return ObserveCurrentUserNicknameUseCase(authRepository, userRepository)
    }

    @Provides
    fun provideObserveMyPageDataUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        consultRepository: ConsultRepository
    ): ObserveMyPageDataUseCase {
        return ObserveMyPageDataUseCase(authRepository, userRepository, consultRepository)
    }

    @Provides
    fun provideSaveUserUseCase(userRepository: UserRepository): SaveUserUseCase {
        return SaveUserUseCase(userRepository)
    }

    @Provides
    fun provideSyncFcmTokenUseCase(
        userRepository: UserRepository,
        authRepository: AuthRepository
    ): SyncFcmTokenUseCase {
        return SyncFcmTokenUseCase(userRepository, authRepository)
    }

    @Provides
    fun provideUpdateNicknameUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository,
        consultRepository: ConsultRepository,
        pharmacistRepository: PharmacistRepository
    ): UpdateNicknameUseCase {
        return UpdateNicknameUseCase(authRepository, userRepository, consultRepository, pharmacistRepository)
    }

    @Provides
    fun provideCreateConsultUseCase(consultRepository: ConsultRepository): CreateConsultUseCase {
        return CreateConsultUseCase(consultRepository)
    }

    @Provides
    fun provideDeleteConsultUseCase(consultRepository: ConsultRepository): DeleteConsultUseCase {
        return DeleteConsultUseCase(consultRepository)
    }

    @Provides
    fun provideDeleteConsultAnswerUseCase(consultRepository: ConsultRepository): DeleteConsultAnswerUseCase {
        return DeleteConsultAnswerUseCase(consultRepository)
    }

    @Provides
    fun provideGetConsultForEditUseCase(consultRepository: ConsultRepository): GetConsultForEditUseCase {
        return GetConsultForEditUseCase(consultRepository)
    }

    @Provides
    fun provideGetConsultItemsUseCase(
        consultRepository: ConsultRepository,
        userRepository: UserRepository
    ): GetConsultItemsUseCase {
        return GetConsultItemsUseCase(consultRepository, userRepository)
    }

    @Provides
    fun provideGetConsultDetailUseCase(
        consultRepository: ConsultRepository,
        userRepository: UserRepository,
        pharmacistRepository: PharmacistRepository,
        authRepository: AuthRepository
    ): GetConsultDetailUseCase {
        return GetConsultDetailUseCase(consultRepository, userRepository, pharmacistRepository, authRepository)
    }

    @Provides
    fun provideGetCurrentUserConsultProfileUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): GetCurrentUserConsultProfileUseCase {
        return GetCurrentUserConsultProfileUseCase(authRepository, userRepository)
    }

    @Provides
    fun provideGetMyConsultListUseCase(
        authRepository: AuthRepository,
        consultRepository: ConsultRepository,
        userRepository: UserRepository
    ): GetMyConsultListUseCase {
        return GetMyConsultListUseCase(authRepository, consultRepository, userRepository)
    }

    @Provides
    fun provideRegisterAnswerUseCase(
        consultRepository: ConsultRepository,
        authRepository: AuthRepository
    ): RegisterAnswerUseCase {
        return RegisterAnswerUseCase(consultRepository, authRepository)
    }

    @Provides
    fun provideSearchPharmacistsByPlaceIdUseCase(
        pharmacistRepository: PharmacistRepository
    ): SearchPharmacistsByPlaceIdUseCase {
        return SearchPharmacistsByPlaceIdUseCase(pharmacistRepository)
    }

    @Provides
    fun provideSendAnswerNotificationUseCase(
        userRepository: UserRepository,
        consultRepository: ConsultRepository
    ): SendAnswerNotificationUseCase {
        return SendAnswerNotificationUseCase(userRepository, consultRepository)
    }

    @Provides
    fun provideSendNewConsultNotificationUseCase(
        userRepository: UserRepository,
        consultRepository: ConsultRepository
    ): SendNewConsultNotificationUseCase {
        return SendNewConsultNotificationUseCase(userRepository, consultRepository)
    }

    @Provides
    fun provideUpdateConsultUseCase(consultRepository: ConsultRepository): UpdateConsultUseCase {
        return UpdateConsultUseCase(consultRepository)
    }

    @Provides
    fun provideUploadConsultImagesUseCase(consultRepository: ConsultRepository): UploadConsultImagesUseCase {
        return UploadConsultImagesUseCase(consultRepository)
    }

    @Provides
    fun provideValidateConsultFormUseCase(): ValidateConsultFormUseCase {
        return ValidateConsultFormUseCase()
    }

    @Provides
    fun provideConsultUseCases(
        getConsultItemsUseCase: GetConsultItemsUseCase,
        getConsultDetailUseCase: GetConsultDetailUseCase,
        registerAnswerUseCase: RegisterAnswerUseCase,
        searchPharmacyUseCase: SearchPharmacyUseCase,
        validateConsultFormUseCase: ValidateConsultFormUseCase,
        getCurrentUserConsultProfileUseCase: GetCurrentUserConsultProfileUseCase,
        getMyConsultListUseCase: GetMyConsultListUseCase,
        getConsultForEditUseCase: GetConsultForEditUseCase,
        searchPharmacistsByPlaceIdUseCase: SearchPharmacistsByPlaceIdUseCase,
        createConsultUseCase: CreateConsultUseCase,
        updateConsultUseCase: UpdateConsultUseCase,
        deleteConsultUseCase: DeleteConsultUseCase,
        deleteConsultAnswerUseCase: DeleteConsultAnswerUseCase,
        sendAnswerNotificationUseCase: SendAnswerNotificationUseCase,
        sendNewConsultNotificationUseCase: SendNewConsultNotificationUseCase
    ): ConsultUseCases {
        return ConsultUseCases(
            getConsultList = getConsultItemsUseCase,
            getConsultDetail = getConsultDetailUseCase,
            registerAnswer = registerAnswerUseCase,
            searchPharmacy = searchPharmacyUseCase,
            validateConsultForm = validateConsultFormUseCase,
            getCurrentUserConsultProfile = getCurrentUserConsultProfileUseCase,
            getMyConsultList = getMyConsultListUseCase,
            getConsultForEdit = getConsultForEditUseCase,
            searchPharmacistsByPlaceId = searchPharmacistsByPlaceIdUseCase,
            createConsult = createConsultUseCase,
            updateConsult = updateConsultUseCase,
            deleteConsult = deleteConsultUseCase,
            deleteConsultAnswer = deleteConsultAnswerUseCase,
            sendAnswerNotification = sendAnswerNotificationUseCase,
            sendNewConsultNotification = sendNewConsultNotificationUseCase
        )
    }
}
