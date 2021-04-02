package com.pokedex.data

import com.pokedex.data.remote.Content
import com.pokedex.data.remote.DetailInfo
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonService {
    @GET(Endpoints.POKEMON)
    fun getPokemons(
        @Query("limit") limit: Int
    ): Observable<Response<Content>>

    @GET
    fun getPokemonDetails(
        @Url url: String
    ): Observable<Response<DetailInfo>>
}