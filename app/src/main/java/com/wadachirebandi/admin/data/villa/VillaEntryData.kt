package com.wadachirebandi.admin.data.villa

import com.wadachirebandi.admin.data.entry.SingleEntryData

data class VillaEntryData(
    val entriesCount: Int,
    val entriesData: List<SingleEntryData>
)