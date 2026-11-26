
package com.splitstay.app.kyc.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.splitstay.app.kyc.data.LocalKycRepository

class KycViewModelFactory(
    private val repository: LocalKycRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KycViewModel::class.java)) {
            return KycViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
