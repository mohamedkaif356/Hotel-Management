package com.wadachirebandi.admin.data.entry

data class Entry(
    val count: Int,
    val `data`: List<EntriesData>,
    val success: Boolean
)