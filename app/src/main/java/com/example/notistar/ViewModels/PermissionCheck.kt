package com.example.notistar.ViewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionCheck : ViewModel() {

    private val _isGranted = MutableStateFlow<Boolean>(false);
    val hasPermission = _isGranted.asStateFlow()

    fun updatePermission(value: Boolean) {
        _isGranted.value = value
    }
}