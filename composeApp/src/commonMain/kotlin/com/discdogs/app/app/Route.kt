package com.discdogs.app.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object MainScreen : Route

    @Serializable
    data object MainGraph : Route

    @Serializable
    data object BookList : Route

    @Serializable
    data class BookDetail(val id: String) : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object Scan : Route

    @Serializable
    data object Favorites : Route

    @Serializable
    data class FavoriteList(val id: Long) : Route

    @Serializable
    data class ReleaseVersions(val masterId: Int) : Route

    @Serializable
    data class ReleaseDetail(
        val releaseId: Int,
        val image: String? = null,
        val source: String
    ) : Route


}