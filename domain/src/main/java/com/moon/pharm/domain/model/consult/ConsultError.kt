package com.moon.pharm.domain.model.consult

sealed class ConsultError : Exception() {
    class NotFound : ConsultError()
}