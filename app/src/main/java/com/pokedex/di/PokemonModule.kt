package com.pokedex.di

import com.pokedex.data.Endpoints
import com.pokedex.data.PokemonService
import com.pokedex.repositories.IPokemonRepository
import com.pokedex.repositories.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PokemonApiServiceModule {
    @Singleton
    @Provides
    fun providesPokemonApiService(): PokemonService {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Endpoints.BASE_URL)
                .build()
                .create(PokemonService::class.java)
    }
}

@InstallIn(ViewModelComponent::class)
@Module
object PokemonRepositoryModule {
    @Provides
    fun providePokemonRepository(
        pokemonService: PokemonService
    ): IPokemonRepository {
        return PokemonRepository(pokemonService)
    }
}