package com.wadachirebandi.api

import com.wadachirebandi.data.entry.Entry
import com.wadachirebandi.data.entry.TodayEntry
import retrofit2.Response
import retrofit2.http.*

interface EntryApi {

    @POST("entries/addNewEntry")
    suspend fun entry(
        @Body entry: Entry
    ): Response<Any>

    @GET("entries/getEntries")
    suspend fun dailyEntry(): Response<TodayEntry>

    @DELETE("entries/deleteEntries/{villaId}")
    suspend fun deleteEntry(
        @Path("villaId") villaId: String
    ): Response<Any>

}