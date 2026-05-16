package co.martketing.rickandmortyapp.di

import co.martketing.rickandmortyapp.data.repository.CharacterRepositoryImpl
import co.martketing.rickandmortyapp.domain.repository.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindCharacterRepository(impl: CharacterRepositoryImpl): CharacterRepository
}
