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

package com.example.tvcomposeintroduction.ui.screens.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Button
import androidx.tv.material3.Carousel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.tvcomposeintroduction.R
import com.example.tvcomposeintroduction.data.Movie
import com.example.tvcomposeintroduction.ui.components.MovieCard

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CatalogBrowser(
    modifier: Modifier = Modifier,
    catalogBrowserViewModel: CatalogBrowserViewModel = hiltViewModel(),
    onMovieSelected: (Movie) -> Unit = {}
) {
    val categoryList by catalogBrowserViewModel.categoryList.collectAsState()
    TvLazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        contentPadding = PaddingValues(horizontal = 58.dp, vertical = 36.dp)
    ) {
        item {
            val featuredMovieList by catalogBrowserViewModel.featuredMovieList.collectAsState()
            Carousel(
                itemCount = featuredMovieList.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(376.dp),
            ) { indexOfCarouselItem ->
                val featuredMovie = featuredMovieList[indexOfCarouselItem]
                Box(contentAlignment = Alignment.BottomStart) {
                    AsyncImage(
                        model = featuredMovie.backgroundImageUrl,
                        contentDescription = null,
                        placeholder = painterResource(
                            id = R.drawable.placeholder
                        ),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                    Column(modifier = Modifier.padding(36.dp),) {
                        Text(
                            text = featuredMovie.title,
                            style =
                            MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                        Button(onClick = { onMovieSelected(featuredMovie) }) {
                            Text(text = stringResource(id = R.string.show_details))
                        }
                    }
                }
            }
        }
        items(categoryList) { category ->
            Text(text = category.name)
            TvLazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(category.movieList) { movie ->
                    MovieCard(
                        movie,
                        onClick = {
                            onMovieSelected(it)
                        }
                    )
                }
            }
        }
    }
}
