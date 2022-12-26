package com.wadachirebandi.data

import com.wadachirebandi.api.EntryApi
import com.wadachirebandi.api.VillaApi
import com.wadachirebandi.data.entry.Entry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val villaApi: VillaApi,
    private val entryApi: EntryApi
) {

    suspend fun allVilla() = villaApi.allVilla()

    suspend fun entry(entry: Entry) = entryApi.entry(entry)

    suspend fun todayEntry() = entryApi.dailyEntry()

    suspend fun deleteEntry(villaId: String) = entryApi.deleteEntry(villaId)

}