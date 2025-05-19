package com.example.mapsapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mapsapp.utils.PermissionStatus
import androidx.compose.runtime.State


class PermissionViewModel: ViewModel() {
    private val _permissionStatus = mutableStateOf<PermissionStatus?>(null)
    val permissionStatus: State<PermissionStatus?> = _permissionStatus

    fun updatePermissionStatus(status: PermissionStatus) {
        _permissionStatus.value = status
    }


    private val _permissionsStatus = mutableStateOf<Map<String, PermissionStatus>>(emptyMap())
    val permissionsStatus: State<Map<String, PermissionStatus>> = _permissionsStatus

    fun updatePermissionStatus(permission: String, status: PermissionStatus) {
        _permissionsStatus.value = _permissionsStatus.value.toMutableMap().apply {
            this[permission] = status
        }
    }

}
