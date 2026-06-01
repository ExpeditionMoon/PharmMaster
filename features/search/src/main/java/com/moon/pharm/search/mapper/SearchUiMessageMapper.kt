package com.moon.pharm.search.mapper

import android.content.Context
import com.moon.pharm.search.R
import com.moon.pharm.search.model.SearchUiMessage

fun SearchUiMessage.asString(context: Context): String {
    return when (this) {
        SearchUiMessage.SearchFailed -> context.getString(R.string.search_error_failed)
        SearchUiMessage.EmptyQuery -> context.getString(R.string.search_error_empty_query)
        is SearchUiMessage.DynamicError -> this.message
    }
}
