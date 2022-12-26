package com.wadachirebandi.data.villa

data class VillaData(
    val __v: Int,
    val _id: String,
    val address: String,
    val careTaker: String,
    val contact: Long,
    val createdAt: String,
    val gallery: List<String>,
    val updatedAt: String,
    val villaName: String
)