package com.projach.comics_tracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.comics_tracker.data.source.remote.ServerClient
import kotlinx.coroutines.launch

class ComicsTrackerApplicationViewModel(
    private val serverClient: ServerClient
) : ViewModel() {
    fun awakeServer() {
        viewModelScope.launch {
            serverClient.awake()
        }
    }
}