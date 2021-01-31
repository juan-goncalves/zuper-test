package com.jcgds.data_layer.local.models

import com.jcgds.domain.entities.Operation
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MessageModelKtTest {

    //region Rules
    //endregion

    //region Mocks
    //endregion

    @Test
    fun `toDomain returns null when the message field is not 'progress' or 'completed'`() {
        // Arrange
        val model = MessageModel("op1", "unsupported", 50, null)

        // Act
        val result = model.toDomain()

        // Assert
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun `toDomain returns null when the message field is 'completed' but the state field is not 'error' or 'success'`() {
        // Arrange
        val model = MessageModel("op1", "completed", 50, "unsupported")

        // Act
        val result = model.toDomain()

        // Assert
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun `toDomain returns null if the message doesn't have an operation id`() {
        // Arrange
        val model = MessageModel(null, "progress", 50, null)

        // Act
        val result = model.toDomain()

        // Assert
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun `toDomain returns null if the message is 'progress' but it doesn't specify the progress percentage`() {
        // Arrange
        val model = MessageModel("op1", "progress", null, null)

        // Act
        val result = model.toDomain()

        // Assert
        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun `toDomain maps a progress message into a domain object correctly`() {
        // Arrange
        val model = MessageModel("op1", "progress", 10, null)

        // Act
        val result = model.toDomain()

        // Assert
        assertThat(result, `is`(notNullValue()))
        assertThat(result!!.operationId, `is`("op1"))
        assertThat(result.progress, `is`(10))
        assertThat(result.operationState, `is`(Operation.State.Ongoing))
    }

    @Test
    fun `toDomain maps a successfully completed operation message into a domain object correctly`() {
        // Arrange
        val model = MessageModel("op1", "completed", null, "success")

        // Act
        val result = model.toDomain()

        // Assert
        assertThat(result, `is`(notNullValue()))
        assertThat(result!!.operationId, `is`("op1"))
        assertThat(result.progress, `is`(100))
        assertThat(result.operationState, `is`(Operation.State.Completed.Success))
    }

    @Test
    fun `toDomain maps a failed operation message into a domain object correctly`() {
        // Arrange
        val model = MessageModel("op1", "completed", null, "error")

        // Act
        val result = model.toDomain()

        // Assert
        assertThat(result, `is`(notNullValue()))
        assertThat(result!!.operationId, `is`("op1"))
        assertThat(result.progress, `is`(100))
        assertThat(result.operationState, `is`(Operation.State.Completed.Error))
    }

    //region Helpers
    //endregion

}