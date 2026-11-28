
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

    // Owner properties
    private val _properties =
        MutableStateFlow<List<PropertyListing>>(emptyList())
    val properties: StateFlow<List<PropertyListing>> =
        _properties.asStateFlow()

    // Student discovery properties
    private val _studentProperties =
        MutableStateFlow<List<PropertyListing>>(emptyList())
    val studentProperties: StateFlow<List<PropertyListing>> =
        _studentProperties.asStateFlow()

    private val _isStudentLoading = MutableStateFlow(false)
    val isStudentLoading: StateFlow<Boolean> =
        _isStudentLoading.asStateFlow()

    private val _studentErrorMessage = MutableStateFlow<String?>(null)
    val studentErrorMessage: StateFlow<String?> =
        _studentErrorMessage.asStateFlow()

    // Existing owner loading and saving states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _saveErrorMessage = MutableStateFlow<String?>(null)
    val saveErrorMessage: StateFlow<String?> =
        _saveErrorMessage.asStateFlow()

    private val _saveCompleted = MutableStateFlow(false)
    val saveCompleted: StateFlow<Boolean> =
        _saveCompleted.asStateFlow()

    private var observationJob: Job? = null
    private var studentObservationJob: Job? = null

    // Existing owner observation
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

    // New student property observation
    fun observeStudentProperties() {
        if (studentObservationJob?.isActive == true) return

        studentObservationJob = viewModelScope.launch {
            _isStudentLoading.value = true
            _studentErrorMessage.value = null

            try {
                repository.observeStudentProperties().collect { result ->
                    _studentProperties.value = result
                    _isStudentLoading.value = false
                }
            } catch (exception: Exception) {
                _studentErrorMessage.value =
                    exception.localizedMessage
                        ?: "Could not load available properties."

                _isStudentLoading.value = false
            }
        }
    }

    fun retryStudentLoading() {
        studentObservationJob?.cancel()
        studentObservationJob = null
        observeStudentProperties()
    }

    // Existing owner property creation
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
