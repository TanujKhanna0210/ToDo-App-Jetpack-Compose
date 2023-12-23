package com.example.todousingcompose.di

import com.example.todousingcompose.repository.PhoneAuthRepository
import com.example.todousingcompose.repository.PhoneAuthRepositoryImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesFirebaseAuthRepository(
        repo: PhoneAuthRepositoryImplementation
    ): PhoneAuthRepository

}