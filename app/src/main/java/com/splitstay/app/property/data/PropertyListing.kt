package com.splitstay.app.property.data

import com.google.firebase.Timestamp

data class PropertyListing(
    val id: String = "",
    val ownerUid: String = "",
    val propertyName: String = "",
    val propertyType: String = "PG",
    val genderPreference: String = "Boys",
    val address: String = "",
    val city: String = "",
    val pincode: String = "",
    val description: String = "",
    val monthlyRent: Double = 0.0,
    val createdAt: Timestamp? = null
)

data class PropertyDraft(
    val propertyName: String,
    val propertyType: String,
    val genderPreference: String,
    val address: String,
    val city: String,
    val pincode: String,
    val description: String,
    val monthlyRent: Double
)