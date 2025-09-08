package com.shaya.myprofile.di

import com.shaya.myprofile.data.remote.ProfileService
import com.shaya.myprofile.data.repository.ProfileRepositoryImpl
import com.shaya.myprofile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideContactRepository(
        profileService: ProfileService,
    ): ProfileRepository {
        return ProfileRepositoryImpl(profileService)
    }
}