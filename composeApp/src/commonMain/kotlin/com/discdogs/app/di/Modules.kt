package com.discdogs.app.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.discdogs.app.core.audioPlayer.AudioRepository
import com.discdogs.app.core.audioPlayer.AudioRepositoryImpl
import com.discdogs.app.data.database.DatabaseFactory
import com.discdogs.app.data.database.FavoriteBookDatabase
import com.discdogs.app.data.network.KtorRemoteDataSource
import com.discdogs.app.data.network.RemoteDataSource
import com.discdogs.app.data.repository.DefaultBookRepository
import com.discdogs.app.domain.BookRepository
import com.discdogs.app.presentation.SelectedBookViewModel
import com.discdogs.app.presentation.book_detail.BookDetailViewModel
import com.discdogs.app.presentation.book_list.BookListViewModel
import com.discdogs.app.core.data.HttpClientFactory
import com.discdogs.app.data.repository.ExternalRepositoryImpl
import com.discdogs.app.data.repository.NetworkRepositoryImpl
import com.discdogs.app.domain.ExternalRepository
import com.discdogs.app.domain.NetworkRepository
import com.discdogs.app.presentation.detail.DetailViewModel
import com.discdogs.app.presentation.search.SearchViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteDataSource).bind<RemoteDataSource>()
    singleOf(::DefaultBookRepository).bind<BookRepository>()
    singleOf(::NetworkRepositoryImpl).bind<NetworkRepository>()
    singleOf(::ExternalRepositoryImpl).bind<ExternalRepository>()
    singleOf(::AudioRepositoryImpl).bind<AudioRepository>()

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<FavoriteBookDatabase>().favoriteBookDao }

    viewModelOf(::BookListViewModel)
    viewModelOf(::BookDetailViewModel)
    viewModelOf(::SelectedBookViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::DetailViewModel)
}