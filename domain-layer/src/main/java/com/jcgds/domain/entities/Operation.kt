package com.jcgds.domain.entities

data class Operation(
    val id: String,
    val state: State,
    val progress: Int,
) {
    sealed class State {
        object Ongoing : State()

        sealed class Completed : State() {
            object Success : Completed()
            object Error : Completed()
        }
    }
}