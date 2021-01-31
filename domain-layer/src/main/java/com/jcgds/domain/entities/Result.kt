package com.jcgds.domain.entities

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Failure<out T>(val message: String) : Result<T>()
}