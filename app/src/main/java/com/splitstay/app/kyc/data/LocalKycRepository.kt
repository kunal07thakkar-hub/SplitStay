
package com.splitstay.app.kyc.data

import android.content.Context
import org.json.JSONObject

class LocalKycRepository(context: Context) {

    private val preferences = context.getSharedPreferences(
        "splitstay_kyc",
        Context.MODE_PRIVATE
    )

    fun save(application: KycApplication) {
        val json = JSONObject().apply {
            put("ownerName", application.ownerName)
            put("propertyName", application.propertyName)
            put("propertyType", application.propertyType)
            put("propertyAddress", application.propertyAddress)
            put("city", application.city)
            put("pincode", application.pincode)
            put("identityType", application.identityType)
            put("documentSelected", application.documentSelected)
            put("propertyPhotoCount", application.propertyPhotoCount)
            put("status", application.status)
            put("rejectionReason", application.rejectionReason)
        }

        preferences.edit()
            .putString("application", json.toString())
            .apply()
    }

    fun get(): KycApplication? {
        val saved = preferences.getString("application", null)
            ?: return null

        return try {
            val json = JSONObject(saved)

            KycApplication(
                ownerName = json.optString("ownerName"),
                propertyName = json.optString("propertyName"),
                propertyType = json.optString("propertyType", "PG"),
                propertyAddress = json.optString("propertyAddress"),
                city = json.optString("city"),
                pincode = json.optString("pincode"),
                identityType = json.optString("identityType", "PAN Card"),
                documentSelected = json.optBoolean("documentSelected"),
                propertyPhotoCount = json.optInt("propertyPhotoCount"),
                status = json.optString("status", "DRAFT"),
                rejectionReason = json.optString("rejectionReason")
            )
        } catch (exception: Exception) {
            null
        }
    }

    fun updateStatus(status: String, reason: String = "") {
        val current = get() ?: return

        save(
            current.copy(
                status = status,
                rejectionReason = reason
            )
        )
    }

    fun clear() {
        preferences.edit().clear().apply()
    }
}
