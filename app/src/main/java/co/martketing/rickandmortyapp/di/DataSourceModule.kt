package co.martketing.rickandmortyapp.di

import co.martketing.rickandmortyapp.data.remote.datasource.CharacterRemoteDataSource
import co.martketing.rickandmortyapp.data.remote.datasource.CharacterRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindCharacterRemoteDataSource(
        impl: CharacterRemoteDataSourceImpl
    ): CharacterRemoteDataSource
}
