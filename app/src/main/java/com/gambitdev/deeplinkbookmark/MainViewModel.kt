package com.gambitdev.deeplinkbookmark

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ScreenState(""))
    val uiState: StateFlow<ScreenState> = _uiState.asStateFlow()
}

@Immutable
data class ScreenState(val deepLinkUrl: String)