package com.splitstay.app.property.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitstay.app.property.data.PropertyDraft
import com.splitstay.app.property.data.PropertyListing
import com.splitstay.app.property.data.PropertyRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PropertyViewModel(
    private val repository: PropertyRepository
) : ViewModel() {

    private val _properties = MutableStateFlow<List<PropertyListing>>(emptyList())
    val properties: StateFlow<List<PropertyListing>> = _properties.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _saveErrorMessage = MutableStateFlow<String?>(null)
    val saveErrorMessage: StateFlow<String?> = _saveErrorMessage.asStateFlow()

    private val _saveCompleted = MutableStateFlow(false)
    val saveCompleted: StateFlow<Boolean> = _saveCompleted.asStateFlow()

    private var observationJob: Job? = null

    fun observeProperties() {
        if (observationJob?.isActive == true) return

        observationJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                repository.observeOwnerProperties().collect { result ->
                    _properties.value = result
                    _isLoading.value = false
                }
            } catch (exception: Exception) {
                _errorMessage.value =
                    exception.localizedMessage
                        ?: "Could not load your properties."

                _isLoading.value = false
            }
        }
    }

    fun retryLoading() {
        observationJob?.cancel()
        observationJob = null
        observeProperties()
    }

    fun addProperty(draft: PropertyDraft) {
        if (_isSaving.value) return

        viewModelScope.launch {
            _isSaving.value = true
            _saveErrorMessage.value = null
            _saveCompleted.value = false

            try {
                repository.addProperty(draft)
                _saveCompleted.value = true
            } catch (exception: Exception) {
                _saveErrorMessage.value =
                    exception.localizedMessage
                        ?: "Could not save this property."
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun clearSaveState() {
        _saveCompleted.value = false
        _saveErrorMessage.value = null
    }
}