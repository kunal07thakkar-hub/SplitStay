
package com.splitstay.app.kyc.presentation

import androidx.lifecycle.ViewModel
import com.splitstay.app.kyc.data.KycApplication
import com.splitstay.app.kyc.data.LocalKycRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class KycViewModel(
    private val repository: LocalKycRepository
) : ViewModel() {

    private val _application =
        MutableStateFlow(repository.get() ?: KycApplication())

    val application: StateFlow<KycApplication> =
        _application.asStateFlow()

    fun saveDraft(application: KycApplication) {
        val draft = application.copy(status = "DRAFT")
        repository.save(draft)
        _application.value = draft
    }

    fun submitForDemoReview(application: KycApplication) {
        val pending = application.copy(
            status = "PENDING_REVIEW",
            rejectionReason = ""
        )

        repository.save(pending)
        _application.value = pending
    }

    fun updateDemoStatus(status: String, reason: String = "") {
        repository.updateStatus(status, reason)
        _application.value = repository.get() ?: KycApplication()
    }

    fun clearApplication() {
        repository.clear()
        _application.value = KycApplication()
    }
}
