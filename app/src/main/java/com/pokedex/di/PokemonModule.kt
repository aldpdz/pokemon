package com.pokedex.di

import com.pokedex.data.Endpoints
import com.pokedex.data.PokemonService
import com.pokedex.repositories.IPokemonRepository
import com.pokedex.repositories.IPokemonRepositoryCoroutines
import com.pokedex.repositories.PokemonRepository
import com.pokedex.repositories.PokemonRepositoryCoroutines
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RxJavaService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CoroutineService

@InstallIn(SingletonComponent::class)
@Module
object PokemonApiServiceModule {

    @RxJavaService
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

    @CoroutineService
    @Singleton
    @Provides
    fun providesPokemonCoroutineApiService() : PokemonService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
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
        @RxJavaService pokemonService: PokemonService
    ): IPokemonRepository {
        return PokemonRepository(pokemonService)
    }
}

@InstallIn(ViewModelComponent::class)
@Module
object PokemonRepositoryCoroutinesModule {
    @Provides
    fun providePokemonCoroutineRepository(
        @CoroutineService pokemonService: PokemonService
    ): IPokemonRepositoryCoroutines {
        return PokemonRepositoryCoroutines(pokemonService)
    }
}