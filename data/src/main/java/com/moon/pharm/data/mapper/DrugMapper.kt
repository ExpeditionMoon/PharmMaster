package com.moon.pharm.data.mapper

import com.moon.pharm.data.datasource.remote.openapi.DrugSearchItem
import com.moon.pharm.data.datasource.remote.openapi.OpenApiConst
import com.moon.pharm.data.datasource.remote.openapi.removeHtmlTags
import com.moon.pharm.domain.model.drug.Drug

fun DrugSearchItem.toDomain(): Drug = Drug(
    itemSeq = itemSeq.orEmpty(),
    itemName = itemName ?: OpenApiConst.DEFAULT_ITEM_NAME,
    companyName = entpName ?: OpenApiConst.DEFAULT_COMPANY_NAME,
    efficacy = efcyQesitm?.removeHtmlTags() ?: OpenApiConst.DEFAULT_EFFICACY,
    interaction = intrcQesitm?.removeHtmlTags() ?: OpenApiConst.DEFAULT_INTERACTION,
    imageUrl = itemImage.orEmpty()
)
