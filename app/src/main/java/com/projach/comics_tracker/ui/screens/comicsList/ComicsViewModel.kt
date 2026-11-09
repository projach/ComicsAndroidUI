package com.projach.comics_tracker.ui.screens.comicsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projach.comics_tracker.core.Logger
import com.projach.comics_tracker.domain.dataResult.onError
import com.projach.comics_tracker.domain.dataResult.onSuccess
import com.projach.comics_tracker.domain.repository.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicsViewModel @Inject constructor(
    private val comicsRepository: ComicsRepository
) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            comicsRepository.getComics().onSuccess { comics ->
                Logger.d("COMICS_VIEWMODEL", "We got comics $comics")
            }.onError {
                Logger.e("COMICS_VIEWMODEL", "We have error $it")
            }
        }
    }
}