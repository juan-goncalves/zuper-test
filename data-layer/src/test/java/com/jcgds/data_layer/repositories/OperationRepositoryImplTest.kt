package com.jcgds.data_layer.repositories

import com.jcgds.data_layer.errors.ExceptionHandler
import com.jcgds.data_layer.sources.OperationRunner
import com.jcgds.domain.entities.Message
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.entities.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test

class OperationRepositoryImplTest {

    private lateinit var SUT: OperationRepositoryImpl

    @Before
    fun setUp() {
        SUT = OperationRepositoryImpl(fakeRunner, ExceptionHandler())
    }

    @Test
    fun `initializeExecutor returns a Failure when the runner initialization throws an Exception`() =
        runBlocking {
            // Arrange
            fakeRunner.failInitialization = true

            // Act
            val result = SUT.initializeExecutor()

            // Assert
            assertThat(result is Result.Failure, `is`(true))
        }

    @Test
    fun `enqueueOperation returns a Failure when the runner throws an Exception`() = runBlocking {
        // Arrange
        fakeRunner.failStartOperation = true

        // Act
        val result = SUT.enqueueOperation("randomId")

        // Assert
        assertThat(result is Result.Failure, `is`(true))

    }

    @Test
    fun `initializeExecutor returns a Success when the runner is initialized correctly`() =
        runBlocking {
            // Arrange
            fakeRunner.failInitialization = false

            // Act
            val result = SUT.initializeExecutor()

            // Assert
            assertThat(result is Result.Success, `is`(true))
        }

    @Test
    fun `enqueueOperation returns a Success when the operation is enqueued correctly`() =
        runBlocking {
            // Arrange
            fakeRunner.failStartOperation = false

            // Act
            val result = SUT.enqueueOperation("randomId")

            // Assert
            assertThat(result is Result.Success, `is`(true))

        }

    @Test
    fun `operationsStream emits the correct Operations list according to the updates published by the runner`() =
        runBlocking {
            // Arrange
            fakeRunner.staticMessageQueue = flowOf(
                Message("op1", Operation.State.Ongoing, 0),
                Message("op2", Operation.State.Ongoing, 0),
                Message("op1", Operation.State.Ongoing, 30),
                Message("op1", Operation.State.Ongoing, 100),
                Message("op2", Operation.State.Completed.Error, 100),
                Message("op1", Operation.State.Completed.Success, 100),
            )

            // Act
            val result = SUT.operationsStream.toList()

            // Assert
            assertThat(
                result[0],
                Matchers.contains(
                    Operation("op1", Operation.State.Ongoing, 0),
                ),
            )
            assertThat(
                result[1],
                Matchers.contains(
                    Operation("op1", Operation.State.Ongoing, 0),
                    Operation("op2", Operation.State.Ongoing, 0),
                ),
            )
            assertThat(
                result[2],
                Matchers.contains(
                    Operation("op1", Operation.State.Ongoing, 30),
                    Operation("op2", Operation.State.Ongoing, 0),
                ),
            )
            assertThat(
                result[3],
                Matchers.contains(
                    Operation("op1", Operation.State.Ongoing, 100),
                    Operation("op2", Operation.State.Ongoing, 0),
                ),
            )
            assertThat(
                result[4],
                Matchers.contains(
                    Operation("op1", Operation.State.Ongoing, 100),
                    Operation("op2", Operation.State.Completed.Error, 100),
                ),
            )
            assertThat(
                result[5],
                Matchers.contains(
                    Operation("op1", Operation.State.Completed.Success, 100),
                    Operation("op2", Operation.State.Completed.Error, 100),
                ),
            )
        }

    //region Helpers
    private val fakeRunner = object : OperationRunner {
        var failInitialization = false
        var failStartOperation = false
        var staticMessageQueue: Flow<Message> = flowOf()

        override val messageQueue: Flow<Message>
            get() = staticMessageQueue

        override val errorsStream: Flow<Exception>
            get() = flowOf()

        override suspend fun initialize() {
            if (failInitialization) {
                throw RuntimeException("Force initialization failure")
            }
        }

        override suspend fun startOperation(id: String) {
            if (failStartOperation) {
                throw RuntimeException("Force startOperation failure")
            }
        }
    }
    //endregion

}