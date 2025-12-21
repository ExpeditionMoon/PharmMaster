package com.moon.pharm.consult.model

import com.moon.pharm.consult.R

enum class ConsultPrimaryTab(val title: Int, val index: Int) {
    LATEST(R.string.consult_filter_latest, 0),
    MY_CONSULT(R.string.consult_tab_my_consult, 1);

    companion object {
        fun fromIndex(index: Int) = entries.find { it.index == index } ?: LATEST
    }
}