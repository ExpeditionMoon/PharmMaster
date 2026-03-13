package com.moon.pharm.search.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moon.pharm.search.R
import com.moon.pharm.search.model.SearchUiMessage

@Composable
fun SearchUiMessage.asString(): String {
    return when (this) {
        SearchUiMessage.SearchFailed -> stringResource(R.string.search_error_failed)
        SearchUiMessage.EmptyQuery -> stringResource(R.string.search_error_empty_query)
        is SearchUiMessage.DynamicError -> this.message
    }
}