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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

/**
 * LoadingMovie switches composable according to the loading state.
 */
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    detailsScreenViewModel: DetailsScreenViewModel = viewModel(factory = DetailsScreenViewModel.Factory),
) {
    val state by detailsScreenViewModel.detailsLoadingState.collectAsState()
    when (val s = state) {
        is DetailsLoadingState.Ready -> Details(
            movie = s.movie,
            modifier = modifier
        )
        is DetailsLoadingState.NotFound -> throw DetailsError.NoMovieFound(detailsScreenViewModel.movieId)
        else -> Loading(modifier = modifier)
    }
}

/**
 * Composable for DetailsLoadingState.Loading state.
 */
@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Loading...", style = MaterialTheme.typography.headlineMedium)
    }
}