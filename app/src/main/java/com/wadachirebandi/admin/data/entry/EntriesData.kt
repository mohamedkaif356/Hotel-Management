package com.wadachirebandi.admin.data.entry

data class EntriesData(
    val __v: Int,
    val _id: String,
    val amount: Int,
    val arrivingDate: String,
    val breakFast: Boolean,
    val checkIn: String,
    val checkOut: String,
    val contact: String,
    val createdAt: String,
    val dinner: Boolean,
    val documents: List<String>,
    val guestEmail: String,
    val guestName: String,
    val guests: String,
    val lunch: Boolean,
    val paymentMethod: String,
    val updatedAt: String,
    val villaId: String,
    val villaNo: String
)