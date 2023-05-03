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

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvcomposeintroduction.data.Movie
import com.example.tvcomposeintroduction.data.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

/**
 * CatalogBrowserViewModel is a view model for CatalogBrowser screen.
 */
class CatalogBrowserViewModel(
    private val movieRepository: MovieRepository = MovieRepository(),
) : ViewModel() {
    val featuredMovieList: StateFlow<List<Movie>> = flow {
        emit(movieRepository.getFeaturedMovieList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), listOf())

    val categoryList: StateFlow<List<Category>> = flow {
        var list = listOf<Category>()
        movieRepository.getCategoryList().forEach {
            val category = Category(
                name = it,
                movieList = movieRepository.getMovieListByCategory(it)
            )
            list = list + listOf(category)
            emit(list)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), listOf())
}

@Stable
data class Category(val name: String, val movieList: List<Movie>)