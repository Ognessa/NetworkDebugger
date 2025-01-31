package com.ognessa.networkdebugger.di

import com.ognessa.networkdebugger.ui.screen.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainAppModule {

    @Provides
    fun provideMainViewModel(): MainViewModel {
        return MainViewModel()
    }
}