package com.nivelais.kpass.presentation.di

import com.nivelais.kpass.data.db.ObjectBox
import com.nivelais.kpass.data.repository.KFilesManagerRepositoryImpl
import com.nivelais.kpass.domain.repository.KFilesExplorerRepository
import com.nivelais.kpass.domain.repository.KFilesManagerRepository
import com.nivelais.kpass.domain.usecases.explorer.OpenKFileUseCase
import com.nivelais.kpass.domain.usecases.manager.CreateKFileUseCase
import com.nivelais.kpass.domain.usecases.manager.ImportKFileUseCase
import com.nivelais.kpass.domain.usecases.manager.ListKFilesUseCase
import com.nivelais.kpass.domain.usecases.manager.RemoveKFileUseCase
import com.nivelais.kpass.presentation.ui.launch.LaunchViewModel
import com.nivelais.kpass.presentation.ui.main.MainViewModel
import com.nivelais.kpass.presentation.ui.manage.ManageKFileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Module pour la base de donée
 */
val objectboxModule = module {
    single { ObjectBox.init(androidContext()) }
}

/**
 * Module for all the repository implementation
 */
val repositoryModule = module {
    single { KFilesManagerRepositoryImpl(get(), androidContext().filesDir) as KFilesManagerRepository }
}

/**
 * Module for all the use case
 */
val useCasesModule = module {
    // Manage kfiles use cases
    single { ListKFilesUseCase(get()) }
    single { CreateKFileUseCase(get()) }
    single { ImportKFileUseCase(get()) }
    single { RemoveKFileUseCase(get()) }

    // Explorer use case
    single { OpenKFileUseCase(get()) }
}

/**
 * Module pour les view model
 */
val viewModelModule = module {
    viewModel { LaunchViewModel(get()) }
    viewModel { ManageKFileViewModel(get(), get(), get(), get()) }
    viewModel { MainViewModel() }
}