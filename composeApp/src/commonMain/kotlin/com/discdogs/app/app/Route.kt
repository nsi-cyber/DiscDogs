package com.discdogs.app.app

 import com.discdogs.app.presentation.detail.DetailSource
 import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object BookGraph : Route

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
    data class ReleaseVersions(val masterId: Int) : Route

    @Serializable
    data class ReleaseDetail(
        val releaseId: Int,
        val image: String? = null,
        val source: String
    ) : Route


}