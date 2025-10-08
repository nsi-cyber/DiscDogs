package com.discdogs.app.presentation.book_detail

import com.discdogs.app.domain.Book

data class BookDetailState(
    val isLoading: Boolean = true,
    val isFavorite: Boolean = false,
    val book: Book? = null
)
