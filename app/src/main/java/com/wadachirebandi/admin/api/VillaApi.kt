package com.wadachirebandi.admin.api

import com.wadachirebandi.admin.data.villa.Villa
import com.wadachirebandi.admin.data.villa.VillaEntry
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VillaApi {
    @GET("villas/getEveryVilla")
    suspend fun allVilla(): Response<Villa>

    @GET("villas/getVillaById/{villaId}")
    suspend fun villaEntry(
        @Path("villaId") villaId: String
    ): Response<VillaEntry>
}