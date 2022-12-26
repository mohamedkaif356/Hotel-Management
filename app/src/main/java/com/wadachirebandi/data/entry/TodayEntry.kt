package com.wadachirebandi.data.entry

data class TodayEntry(
    val count: Int,
    val `data`: List<SingleEntry>,
    val success: Boolean
)