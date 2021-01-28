package com.jcgds.domain.entities

data class Operation(
    val id: String,
    val state: State,
    val progress: Int,
) {
    sealed class State {
        object Success : State()
        object Error : State()
        object Unknown : State()
    }
}