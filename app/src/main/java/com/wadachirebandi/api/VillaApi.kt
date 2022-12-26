package com.wadachirebandi.api

import com.wadachirebandi.data.villa.Villa
import retrofit2.Response
import retrofit2.http.GET

interface VillaApi {

    @GET("villas/getAllVilla")
    suspend fun allVilla(): Response<Villa>

}