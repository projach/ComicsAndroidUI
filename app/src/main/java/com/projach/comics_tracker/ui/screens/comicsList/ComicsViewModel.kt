package com.projach.comics_tracker.ui.screens.comicsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.projach.comics_tracker.data.session.SessionManager
import com.projach.comics_tracker.domain.dataResult.onError
import com.projach.comics_tracker.domain.dataResult.onSuccess
import com.projach.comics_tracker.domain.models.ComicModel
import com.projach.comics_tracker.domain.useCases.ComicsUseCase
import com.projach.comics_tracker.ui.navigation.NavigationAction
import com.projach.comics_tracker.ui.navigation.Screen
import com.projach.comics_tracker.ui.screens.comicsList.ComicsEffect.ShowError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ComicsIntent {
    object Logout : ComicsIntent
    data class ComicClick(val id: Int) : ComicsIntent
    data class DeleteComic(val id: Int, val onDelete: () -> Unit) : ComicsIntent
    object AddNewComic : ComicsIntent
}

sealed interface ComicsEffect {
    data class ShowError(val message: String) : ComicsEffect
    data class Navigate(val navigationAction: NavigationAction) : ComicsEffect
}

@HiltViewModel
class ComicsViewModel @Inject constructor(
    private val comicsUseCase: ComicsUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    val comics: Flow<PagingData<ComicModel>> = comicsUseCase.getComics()

    private val _uiState = MutableStateFlow(ComicsUiState())
    val uiState: StateFlow<ComicsUiState> = _uiState

    private val _effect = Channel<ComicsEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: ComicsIntent) {
        when (intent) {
            is ComicsIntent.ComicClick -> {

            }

            is ComicsIntent.DeleteComic -> {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                viewModelScope.launch(Dispatchers.IO) {
                    comicsUseCase.deleteComic(intent.id).onSuccess {
                        intent.onDelete.invoke()
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }.onError {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _effect.send(ShowError("Could not delete comic please try again"))
                    }
                }
            }

            ComicsIntent.Logout -> {
                sessionManager.logout()
            }

            ComicsIntent.AddNewComic -> {
                viewModelScope.launch {
                    _effect.send(ComicsEffect.Navigate(NavigationAction.NavigateTo(Screen.AddComicScreen)))
                }
            }
        }
    }
}