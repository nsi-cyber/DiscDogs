package com.discdogs.app.data.repository

import com.discdogs.app.data.database.dao.FavoriteReleaseDao
import com.discdogs.app.data.database.dao.RecentReleaseDao
import com.discdogs.app.data.database.dao.ReleaseDao
import com.discdogs.app.data.database.dao.ReleaseListDao
import com.discdogs.app.data.database.dao.ReleaseListReleaseDao
import com.discdogs.app.data.database.dao.UserPreferenceDao
import com.discdogs.app.data.database.model.FavoriteRelease
import com.discdogs.app.data.database.model.RecentRelease
import com.discdogs.app.data.database.model.Release
import com.discdogs.app.data.database.model.ReleaseList
import com.discdogs.app.data.database.model.ReleaseListRelease
import com.discdogs.app.data.database.model.UserPreference
import com.discdogs.app.data.database.model.toUiModel
import com.discdogs.app.presentation.model.VinylDetailUiModel
import com.discdogs.app.presentation.model.VinylResultUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class LibraryRepository(
    private val favoriteReleaseDao: FavoriteReleaseDao,
    private val recentReleaseDao: RecentReleaseDao,
    private val releaseDao: ReleaseDao,
    private val userPreferenceDao: UserPreferenceDao,
    private val releaseListDao: ReleaseListDao,
    private val releaseListReleaseDao: ReleaseListReleaseDao
) {

    // Favorite operations
    fun getAllFavorites(): Flow<List<VinylResultUiModel>> =
        favoriteReleaseDao.getAllFavorites().map { it.map { it.toUiModel() } }
            .distinctUntilChanged()

    fun isFavorite(releaseId: Int): Flow<Boolean> =
        favoriteReleaseDao.isFavorite(releaseId).distinctUntilChanged()

    fun getFavoriteCount(): Flow<Int> =
        favoriteReleaseDao.getFavoriteCount().distinctUntilChanged()

    suspend fun addToFavorites(vinylDetail: VinylDetailUiModel) {
        val favorite = FavoriteRelease(
            id = vinylDetail.id,
            title = vinylDetail.title,
            thumb = vinylDetail.thumb,
            artists = vinylDetail.artists?.map { it.name } ?: emptyList(),
            year = vinylDetail.year,
            genres = vinylDetail.genres ?: emptyList(),
            country = vinylDetail.country,
            released = vinylDetail.released,
            masterId = vinylDetail.masterId,
            formats = vinylDetail.formats?.flatMap { format ->
                val formatList = mutableListOf<String>()
                formatList.add(format.name)
                format.descriptions?.let { descriptions ->
                    formatList.addAll(descriptions)
                }
                formatList
            } ?: emptyList(),
            labels = vinylDetail.labels?.map { it.name.orEmpty() } ?: emptyList()
        )
        favoriteReleaseDao.insertFavorite(favorite)
    }

    suspend fun removeFromFavorites(releaseId: Int) {
        favoriteReleaseDao.deleteFavoriteById(releaseId)
    }

    // Recent releases operations
    fun getRecentReleases(): Flow<List<VinylResultUiModel>> =
        recentReleaseDao.getRecentReleases().map { it.map { it.toUiModel() } }
            .distinctUntilChanged()

    fun getRecentReleasesBySource(source: String): Flow<List<VinylResultUiModel>> =
        recentReleaseDao.getRecentReleasesBySource(source).map { it.map { it.toUiModel() } }
            .distinctUntilChanged()

    suspend fun addRecentRelease(vinylDetail: VinylDetailUiModel, source: String) {
        val recentRelease = RecentRelease(
            id = vinylDetail.id,
            title = vinylDetail.title,
            thumb = vinylDetail.thumb,
            artists = vinylDetail.artists?.map { it.name } ?: emptyList(),
            year = vinylDetail.year,
            genres = vinylDetail.genres ?: emptyList(),
            formats = vinylDetail.formats?.flatMap { format ->
                val formatList = mutableListOf<String>()
                formatList.add(format.name)
                format.descriptions?.let { descriptions ->
                    formatList.addAll(descriptions)
                }
                formatList
            } ?: emptyList(),
            country = vinylDetail.country,
            masterId = vinylDetail.masterId,
            source = source
        )
        recentReleaseDao.insertRecentRelease(recentRelease)
        recentReleaseDao.cleanupOldReleases()
    }

    suspend fun clearRecentReleases() {
        recentReleaseDao.clearAllRecentReleases()
    }

    suspend fun clearRecentReleasesBySource(source: String) {
        recentReleaseDao.clearRecentReleasesBySource(source)
    }

    // User preference operations
    suspend fun isFirstTime(): Boolean {
        return userPreferenceDao.isFirstTime() != false // Default to true if no preference exists
    }

    suspend fun setFirstTimeCompleted() {
        val currentPreference = userPreferenceDao.isFirstTime()
        if (currentPreference == null) {
            // Create new preference record
            userPreferenceDao.insertUserPreference(UserPreference(isFirstTime = false))
        } else {
            // Update existing record
            userPreferenceDao.updateFirstTime(false)
        }
    }


    fun getUserPreference(): Flow<UserPreference?> =
        userPreferenceDao.getUserPreference().distinctUntilChanged()

    // Rating dialog operations
    suspend fun getLastRatingPromptTime(): Long {
        return userPreferenceDao.getLastRatingPromptTime() ?: 0L
    }

    suspend fun isRatingDialogDismissed(): Boolean {
        return userPreferenceDao.isRatingDialogDismissed() == true
    }

    suspend fun hasUserRated(): Boolean {
        return userPreferenceDao.hasUserRated() == true
    }

    suspend fun shouldShowRatingDialog(): Boolean {
        // Don't show if user has already rated or dismissed permanently
        if (hasUserRated() || isRatingDialogDismissed()) {
            return false
        }

        val lastPromptTime = getLastRatingPromptTime()
        val currentTime = System.now().toEpochMilliseconds()
        val fiveDaysInMillis = 5 * 24 * 60 * 60 * 1000L // 5 days in milliseconds

        // If never prompted before (lastPromptTime = 0), this means it's the first time
        // Don't show dialog yet - user needs to experience the app for 5 days first
        if (lastPromptTime == 0L) {
            return false
        }

        // Show if it's been more than 5 days since last prompt
        return (currentTime - lastPromptTime) >= fiveDaysInMillis
    }

    suspend fun updateLastRatingPromptTime() {
        val isFirstTime = userPreferenceDao.isFirstTime()
        if (isFirstTime == null) {
            // Create new preference record with rating prompt time
            userPreferenceDao.insertUserPreference(
                UserPreference(lastRatingPromptTime = System.now().toEpochMilliseconds())
            )
        } else {
            userPreferenceDao.updateLastRatingPromptTime(System.now().toEpochMilliseconds())
        }
    }

    suspend fun setRatingDialogDismissed() {
        val isFirstTime = userPreferenceDao.isFirstTime()
        if (isFirstTime == null) {
            // Create new preference record with dialog dismissed
            userPreferenceDao.insertUserPreference(
                UserPreference(ratingDialogDismissed = true)
            )
        } else {
            userPreferenceDao.setRatingDialogDismissed(true)
        }
    }

    suspend fun setUserHasRated() {
        val isFirstTime = userPreferenceDao.isFirstTime()
        if (isFirstTime == null) {
            // Create new preference record with user rated
            userPreferenceDao.insertUserPreference(
                UserPreference(hasRated = true)
            )
        } else {
            userPreferenceDao.setUserHasRated(true)
        }
    }

    suspend fun initializeRatingTimer() {
        val lastPromptTime = getLastRatingPromptTime()
        // Only initialize if not already set (first time app is used)
        if (lastPromptTime == 0L) {
            updateLastRatingPromptTime()
        }
    }

    // List operations
    fun getAllLists(): Flow<List<ReleaseList>> = releaseListDao.getAllLists()

    suspend fun createList(name: String): Long {
        val list = ReleaseList(name = name)
        return releaseListDao.insertList(list)
    }


    suspend fun updateList(list: ReleaseList) {
        val updatedList = list.copy(updatedAt = System.now().toEpochMilliseconds())
        releaseListDao.updateList(updatedList)
    }

    suspend fun deleteList(listId: Long) {
        releaseListDao.deleteListById(listId)
    }

    fun getReleasesInList(listId: Long): Flow<List<VinylResultUiModel>> =
        releaseListReleaseDao.getReleasesInList(listId).map { it.map { it.toUiModel() } }
            .distinctUntilChanged()

    suspend fun addReleaseToList(listId: Long, vinylDetail: VinylDetailUiModel) {
        // Check if release is already in the list
        val isAlreadyInList = releaseListReleaseDao.isReleaseInList(listId, vinylDetail.id)
        if (isAlreadyInList.first()) {
            throw IllegalArgumentException("Release is already in this list")
        }

        // First ensure the release details are stored in the releases table
        val release = Release(
            id = vinylDetail.id,
            title = vinylDetail.title,
            thumb = vinylDetail.thumb,
            artists = vinylDetail.artists?.map { it.name } ?: emptyList(),
            year = vinylDetail.year,
            genres = vinylDetail.genres ?: emptyList(),
            country = vinylDetail.country,
            released = vinylDetail.released,
            masterId = vinylDetail.masterId,
            formats = vinylDetail.formats?.flatMap { format ->
                val formatList = mutableListOf<String>()
                formatList.add(format.name)
                format.descriptions?.let { descriptions ->
                    formatList.addAll(descriptions)
                }
                formatList
            } ?: emptyList(),
            labels = vinylDetail.labels?.map { it.name.orEmpty() } ?: emptyList()
        )

        // Store release details if not already there
        releaseDao.insertRelease(release)

        // Then add to list
        val releaseListRelease = ReleaseListRelease(listId = listId, releaseId = vinylDetail.id)
        releaseListReleaseDao.insertReleaseToList(releaseListRelease)

        // Update list's updatedAt timestamp
        val list = releaseListDao.getListById(listId)
        list?.let { updateList(it) }
    }

    suspend fun removeReleaseFromList(listId: Long, releaseId: Int) {
        releaseListReleaseDao.removeReleaseFromListById(listId, releaseId)

        // Update list's updatedAt timestamp
        val list = releaseListDao.getListById(listId)
        list?.let { updateList(it) }
    }

    fun isReleaseInList(listId: Long, releaseId: Int): Flow<Boolean> =
        releaseListReleaseDao.isReleaseInList(listId, releaseId).distinctUntilChanged()

    fun getReleaseCountInList(listId: Long): Flow<Int> =
        releaseListReleaseDao.getReleaseCountInList(listId).distinctUntilChanged()

    fun getListsContainingRelease(releaseId: Int): Flow<Set<Long>> =
        releaseListReleaseDao.getListsContainingRelease(releaseId).map { it.toSet() }
            .distinctUntilChanged()

    suspend fun getListById(listId: Long): ReleaseList? =
        releaseListDao.getListById(listId)
}
