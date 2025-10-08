package com.discdogs.app.data.repository

import androidx.sqlite.SQLiteException
import com.discdogs.app.data.database.FavoriteBookDao
import com.discdogs.app.data.mappers.toBook
import com.discdogs.app.data.mappers.toBookEntity
import com.discdogs.app.data.network.RemoteDataSource
import com.discdogs.app.domain.Book
import com.discdogs.app.domain.BookRepository
import com.discdogs.app.core.domain.DataError
import com.discdogs.app.core.domain.EmptyResult
import com.discdogs.app.core.domain.Result
import com.discdogs.app.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultBookRepository(
    private val RemoteDataSource: RemoteDataSource,
    private val favoriteBookDao: FavoriteBookDao
): BookRepository {
    override suspend fun searchBooks(query: String): Result<List<Book>, DataError.Remote> {
        TODO("Not yet implemented")
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        TODO("Not yet implemented")
    }


    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.map { it.toBook() }
            }
    }

    override fun isBookFavorite(id: String): Flow<Boolean> {
        return favoriteBookDao
            .getFavoriteBooks()
            .map { bookEntities ->
                bookEntities.any { it.id == id }
            }
    }

    override suspend fun markAsFavorite(book: Book): EmptyResult<DataError.Local> {
        return try {
            favoriteBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        } catch(e: SQLiteException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteFromFavorites(id: String) {
        favoriteBookDao.deleteFavoriteBook(id)
    }
}