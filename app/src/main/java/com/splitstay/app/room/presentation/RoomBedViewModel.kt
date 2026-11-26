
package com.splitstay.app.room.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.splitstay.app.room.data.Bed
import com.splitstay.app.room.data.BedDraft
import com.splitstay.app.room.data.BedStatus
import com.splitstay.app.room.data.Room
import com.splitstay.app.room.data.RoomDraft
import com.splitstay.app.room.data.RoomBedRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoomBedViewModel(
    private val repository: RoomBedRepository
) : ViewModel() {

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms.asStateFlow()

    private val _beds = MutableStateFlow<List<Bed>>(emptyList())
    val beds: StateFlow<List<Bed>> = _beds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    private var roomsJob: Job? = null
    private var bedsJob: Job? = null

    fun observeRooms(propertyId: String) {
        roomsJob?.cancel()
        bedsJob?.cancel()
        _beds.value = emptyList()
        _error.value = null
        _isLoading.value = true

        roomsJob = viewModelScope.launch {
            try {
                repository.observeRooms(propertyId).collect {
                    _rooms.value = it
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unable to load rooms."
                _isLoading.value = false
            }
        }
    }

    fun observeBeds(propertyId: String, roomId: String) {
        bedsJob?.cancel()
        _beds.value = emptyList()
        _error.value = null
        _isLoading.value = true

        bedsJob = viewModelScope.launch {
            try {
                repository.observeBeds(propertyId, roomId).collect {
                    _beds.value = it
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unable to load beds."
                _isLoading.value = false
            }
        }
    }

    fun addRoom(propertyId: String, draft: RoomDraft) {
        performSave {
            repository.addRoom(propertyId, draft)
        }
    }

    fun updateRoom(
        propertyId: String,
        room: Room,
        draft: RoomDraft
    ) {
        performSave {
            repository.updateRoom(propertyId, room, draft)
        }
    }

    fun deleteRoom(propertyId: String, roomId: String) {
        performSave {
            repository.deleteRoom(propertyId, roomId)
        }
    }

    fun addBed(
        propertyId: String,
        roomId: String,
        draft: BedDraft
    ) {
        performSave {
            repository.addBed(propertyId, roomId, draft)
        }
    }

    fun updateBedStatus(
        propertyId: String,
        roomId: String,
        bedId: String,
        status: BedStatus
    ) {
        performSave {
            repository.updateBedStatus(propertyId, roomId, bedId, status)
        }
    }

    fun deleteBed(propertyId: String, roomId: String, bedId: String) {
        performSave {
            repository.deleteBed(propertyId, roomId, bedId)
        }
    }

    private fun performSave(action: suspend () -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            _error.value = null
            _message.value = null

            try {
                action()
                _message.value = "Changes saved successfully."
            } catch (e: Exception) {
                _error.value = e.message ?: "Something went wrong."
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun clearMessages() {
        _error.value = null
        _message.value = null
    }

    override fun onCleared() {
        roomsJob?.cancel()
        bedsJob?.cancel()
        super.onCleared()
    }
}
