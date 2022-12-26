package com.wadachirebandi.admin.api

import com.wadachirebandi.admin.data.entry.Entry
import com.wadachirebandi.admin.data.entry.SingleEntry
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EntryApi {

    @GET("entries/getEntryByDate/{villaId}")
    suspend fun entryByDate(
        @Path("villaId") villaId: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<Entry>

    @GET("entries/getEntryById/{entryId}")
    suspend fun singleEntry(
        @Path("entryId") entryId: String
    ): Response<SingleEntry>

}