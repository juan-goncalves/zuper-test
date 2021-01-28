package com.jcgds.data_layer.sources

import com.jcgds.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface OperationDataSource {

    val messageQueue: Flow<Message>

}