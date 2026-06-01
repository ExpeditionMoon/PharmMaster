package com.moon.pharm.search.mapper

import com.moon.pharm.domain.model.drug.Drug
import com.moon.pharm.search.model.DrugUiModel

fun Drug.toUiModel(): DrugUiModel {
    return DrugUiModel(
        itemSeq = itemSeq,
        itemName = itemName,
        companyName = companyName,
        efficacy = efficacy,
        interaction = interaction,
        imageUrl = imageUrl
    )
}
