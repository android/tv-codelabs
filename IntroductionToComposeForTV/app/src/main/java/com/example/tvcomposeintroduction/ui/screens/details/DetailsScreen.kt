/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tvcomposeintroduction.ui.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

/**
 * LoadingMovie switches composable according to the loading state.
 */
@Composable
fun DetailsScreen(
    backAction: () -> Unit,
    modifier: Modifier = Modifier,
    detailsScreenViewModel: DetailsScreenViewModel = hiltViewModel()
) {
    val state by detailsScreenViewModel.detailsLoadingState.collectAsStateWithLifecycle()

    when (val s = state) {
        is DetailsLoadingState.Ready -> Details(
            movie = s.movie,
            modifier = modifier
        )

        is DetailsLoadingState.NotFound -> NotFound(
            backAction = backAction,
            modifier = modifier.fillMaxSize()
        )

        else -> Loading(modifier = modifier.fillMaxSize())
    }
}

/**
 * Composable for DetailsLoadingState.Loading state.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(text = "Loading...", style = MaterialTheme.typography.displayMedium)
    }
}

/**
 *
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun NotFound(backAction: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text(text = "Not found", style = MaterialTheme.typography.displayMedium)
            Button(onClick = backAction) {
                Text(text = "Back to the previous screen")
            }
        }
    }
}

