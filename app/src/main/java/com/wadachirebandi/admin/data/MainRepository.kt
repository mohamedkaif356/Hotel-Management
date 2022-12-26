package com.wadachirebandi.admin.data

import com.wadachirebandi.admin.api.EntryApi
import com.wadachirebandi.admin.api.VillaApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val villaApi: VillaApi,
    private val entryApi: EntryApi
) {

    suspend fun allVilla() = villaApi.allVilla()

    suspend fun entryByDate(villaId: String, startDate: String, endDate: String) =
        entryApi.entryByDate(villaId, startDate, endDate)

    suspend fun entryByVilla(villaId: String) =
        villaApi.villaEntry(villaId)

    suspend fun singleEntry(entryId: String) = entryApi.singleEntry(entryId)

}