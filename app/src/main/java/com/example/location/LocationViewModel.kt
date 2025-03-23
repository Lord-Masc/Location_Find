package com.example.location

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {
    private val _location = mutableStateOf<LoctaionData?>(null)
    val location: State<LoctaionData?> = _location

    fun updateLocation(newLocation: LoctaionData){
        _location.value = newLocation
    }
}