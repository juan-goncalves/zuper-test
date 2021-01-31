package com.jcgds.data_layer.network.services

import okhttp3.ResponseBody
import retrofit2.http.GET

interface ZuperAwsService {

    @GET("tmp/interview_bundle_android.js")
    suspend fun downloadOperationsJsFile(): ResponseBody

}