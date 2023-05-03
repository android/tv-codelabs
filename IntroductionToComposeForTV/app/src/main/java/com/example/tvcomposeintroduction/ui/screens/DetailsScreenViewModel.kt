/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tvcomposeintroduction.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.tvcomposeintroduction.data.Movie
import com.example.tvcomposeintroduction.data.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

/**
 * DetailsScreenViewModel is the view model for DetailsScreen.
 */
class DetailsScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository = MovieRepository()
) : ViewModel() {
    val movieId: Long = checkNotNull(savedStateHandle["id"])

    val detailsLoadingState: StateFlow<DetailsLoadingState> = flow {
        val movie = movieRepository.findMovieById(id = movieId)
        val state = if (movie == null) {
            DetailsLoadingState.NotFound
        } else {
            DetailsLoadingState.Ready(movie = movie)
        }
        emit(state)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), DetailsLoadingState.Loading)

    companion object{
        val Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                return DetailsScreenViewModel(savedStateHandle = extras.createSavedStateHandle()) as T
            }
        }
    }
}

/**
 * DetailsLoadingState represents the state of the loading state of move details
 */
sealed class DetailsLoadingState {
    // The screen is in this state when it is loading a movie object via repository.
    object Loading : DetailsLoadingState()
    // The screen is in this state when it can not find a movie object associated with the given ID.
    object NotFound : DetailsLoadingState()
    // The screen is in this state when it become ready to display the movie info.
    class Ready(val movie: Movie) : DetailsLoadingState()
}

