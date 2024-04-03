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

package com.example.tvcomposeintroduction.data

import java.io.Serializable

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
data class Movie(
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var backgroundImageUrl: String = "",
    var cardImageUrl: String = "",
    var videoUrl: String = "",
    var studio: String = ""
) : Serializable {

    override fun toString(): String {
        return "Movie{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", videoUrl='" + videoUrl + '\'' +
            ", backgroundImageUrl='" + backgroundImageUrl + '\'' +
            ", cardImageUrl='" + cardImageUrl + '\'' +
            '}'
    }

    companion object {
        internal const val serialVersionUID = 727566175075960653L
    }
}
