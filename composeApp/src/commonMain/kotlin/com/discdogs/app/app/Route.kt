package com.discdogs.app.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    data object Onboarding : Route

    @Serializable
    data object MainScreen : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object Scan : Route

    @Serializable
    data object Favorites : Route

    @Serializable
    data class FavoriteList(val id: Long) : Route

    @Serializable
    data class ReleaseVersions(
        val masterId: Int, val source: String
    ) : Route

    @Serializable
    data class ReleaseDetail(
        val releaseId: Int,
        val image: String? = null,
        val source: String
    ) : Route

    @Serializable
    data class MasterDetail(
        val masterId: Int,
        val image: String? = null,
        val source: String
    ) : Route


}