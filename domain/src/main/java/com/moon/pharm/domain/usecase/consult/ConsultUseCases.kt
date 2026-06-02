package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.usecase.pharmacy.SearchPharmacyUseCase
import javax.inject.Inject

data class ConsultUseCases @Inject constructor(
    val getConsultList: GetConsultItemsUseCase,
    val getConsultDetail: GetConsultDetailUseCase,
    val registerAnswer: RegisterAnswerUseCase,
    val searchPharmacy: SearchPharmacyUseCase,
    val validateConsultForm: ValidateConsultFormUseCase,
    val getCurrentUserConsultProfile: GetCurrentUserConsultProfileUseCase,
    val getMyConsultList: GetMyConsultListUseCase,
    val getConsultForEdit: GetConsultForEditUseCase,
    val searchPharmacistsByPlaceId: SearchPharmacistsByPlaceIdUseCase,
    val createConsult: CreateConsultUseCase,
    val updateConsult: UpdateConsultUseCase,
    val deleteConsult: DeleteConsultUseCase,
    val deleteConsultAnswer: DeleteConsultAnswerUseCase,
    val sendAnswerNotification: SendAnswerNotificationUseCase,
    val sendNewConsultNotification: SendNewConsultNotificationUseCase
)
