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

package com.example.tvcomposeintroduction.data

import com.example.tvcomposeintroduction.data.MovieAPI.loadCategoryList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Integer.max
import java.lang.Integer.min

/**
 * MovieRepository class is a repository for Movies.
 * The repository caches movies and category list on memory to speed up response.
 */
class MovieRepository(
    private val dataSource: MovieAPI = MovieAPI,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * In-memory caches.
     */
    private val categoryMovieListMap: MutableMap<String, List<Movie>> = mutableMapOf()
    private val idMovieMap: MutableMap<Long, Movie?> = mutableMapOf()
    private var categoryList: List<String> = listOf()

    /*
     Mutexes for in-memory caches.
     Please refer to https://developer.android.com/topic/architecture/data-layer#in-memory-cache for details
     */
    private val categoryMovieListMapMutex = Mutex()
    private val idMovieMapMutex = Mutex()
    private val categoryListMutex = Mutex()

    suspend fun getCategoryList(): List<String> {
        if (categoryList.isEmpty()) {
            categoryListMutex.withLock {
                categoryList = loadCategoryList().unwrapOr(categoryList)
            }
        }
        prefetchMovieListByCategory(category = categoryList[0])
        return categoryListMutex.withLock { categoryList }
    }

    suspend fun getFeaturedMovieList(): List<Movie> {
        val movieList = dataSource.loadFeaturedMovieList().unwrapOr(listOf())
        updateCache(movieList)
        return movieList
    }

    suspend fun findMovieById(id: Long): Movie? {
        idMovieMapMutex.withLock {
            if (!idMovieMap.contains(id)) {
                idMovieMap[id] = dataSource.findMovieById(id).unwrapOr(null)
            }
        }
        return idMovieMapMutex.withLock { idMovieMap[id] }
    }

    suspend fun getMovieListByCategory(category: String): List<Movie> {
        loadMovieListByCategory(category)
        prefetchNeighborCategories(category = category)
        return categoryMovieListMapMutex.withLock { categoryMovieListMap[category]!! }
    }

    private suspend fun loadMovieListByCategory(category: String, force: Boolean = false) {
        if (force || !categoryMovieListMap.contains(category)) {
            updateCache(
                category = category,
                movieList = dataSource.getMovieListByCategory(category).unwrapOr(listOf())
            )
        }
    }

    private suspend fun updateCache(category: String, movieList: List<Movie>) {
        categoryMovieListMapMutex.withLock {
            categoryMovieListMap[category] = movieList
        }
        updateCache(movieList)
    }

    private suspend fun updateCache(movieList: List<Movie>) {
        idMovieMapMutex.withLock {
            movieList.forEach {
                idMovieMap[it.id] = it
            }
        }
    }

    private fun prefetchMovieListByCategory(category: String) {
        CoroutineScope(dispatcher).launch {
            loadMovieListByCategory(category)
        }
    }

    private fun prefetchNeighborCategories(
        category: String,
        neighborCount: Int = 1,
    ) {
        val indexForCategory = max(categoryList.indexOf(category), 0)
        val neighbors =
            (indexForCategory..min(
                indexForCategory + neighborCount,
                categoryList.size - 1
            )).map { categoryList[it] }
        CoroutineScope(dispatcher).launch {
            neighbors.forEach { loadMovieListByCategory(it) }
        }
    }
}
