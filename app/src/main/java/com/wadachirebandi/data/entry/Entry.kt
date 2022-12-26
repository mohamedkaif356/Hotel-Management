package com.wadachirebandi.data.entry

data class Entry(
    val amount: Int,
    val arrivingDate: String,
    val checkIn: String,
    val checkOut: String,
    val contact: Long,
    val documents: List<String>,
    val guestName: String,
    val guests: String,
    val lunch: Boolean? = false,
    val breakfast: Boolean? = false,
    val dinner: Boolean? = false,
    val paymentMethod: String,
    val villaId: String,
    val villaNo: String
)