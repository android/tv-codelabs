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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.tvcomposeintroduction.data.Movie

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Details(movie: Movie, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = movie.cardImageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 58.dp, vertical = 36.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = movie.studio,
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}
