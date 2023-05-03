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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * Option class represents a product of a process that can have no value.
 */
sealed class Option<out T> {
    object None : Option<Nothing>()
    data class Some<T>(val value: T): Option<T>()
}

fun <T> Option<T>.unwrapOr(defaultValue: T): T{
    return (this as? Option.Some)?.value ?: defaultValue
}

/**
 * MovieAPI object simulates API calls.
 */
object MovieAPI {

    suspend fun loadCategoryList(): Option<List<String>> {
        return withDelay {
            Option.Some(MovieList.categories)
        }
    }

    suspend fun loadFeaturedMovieList(): Option<List<Movie>> {
        return withDelay {
            Option.Some(MovieList.featured)
        }
    }

    suspend fun getMovieListByCategory(category: String): Option<List<Movie>> {
        return withDelay {
            Option.Some(MovieList.getByCategory(category))
        }
    }

    suspend fun findMovieById(id: Long): Option<Movie> {
        return withDelay {
            val movie = MovieList.findById(id)
            if(movie == null){
                Option.None
            }else{
                Option.Some(movie)
            }
        }
    }

}

private suspend fun<T> withDelay(
    delayInMilSec: Long = Random.nextLong(300, 1500),
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    task: suspend () -> T,
): T {
    return withContext(context = coroutineDispatcher){
        delay(delayInMilSec)
        task()
    }
}