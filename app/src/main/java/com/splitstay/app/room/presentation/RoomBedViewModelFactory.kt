
package com.splitstay.app.room.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.splitstay.app.room.data.RoomBedRepository

class RoomBedViewModelFactory(
    private val repository: RoomBedRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomBedViewModel::class.java)) {
            return RoomBedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
