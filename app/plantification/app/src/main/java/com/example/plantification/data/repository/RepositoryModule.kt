package com.example.plantification.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

/**
 * Dagger module for providing repository bindings.
 */
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the [PlantRepositoryImpl] implementation to the [PlantRepository] interface.
     *
     * @param plantRepositoryImpl the implementation of [PlantRepository]
     * @return an instance of [PlantRepository]
     */
    @Binds
    @Singleton
    abstract fun bindPlantRepository(
        plantRepositoryImpl: PlantRepositoryImpl
    ): PlantRepository
}
