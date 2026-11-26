
package com.splitstay.app.kyc.data

data class KycApplication(
    val ownerName: String = "",
    val propertyName: String = "",
    val propertyType: String = "PG",
    val propertyAddress: String = "",
    val city: String = "",
    val pincode: String = "",
    val identityType: String = "PAN Card",
    val documentSelected: Boolean = false,
    val propertyPhotoCount: Int = 0,
    val status: String = "DRAFT",
    val rejectionReason: String = ""
)
