package com.moon.pharm.domain.model.auth

enum class UserType {
    GENERAL, PHARMACIST;

    companion object {
        fun from(value: String?): UserType {
            return entries.find { it.name == value } ?: GENERAL
        }
    }
}