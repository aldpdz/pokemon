package com.pokedex.data

import com.pokedex.data.remote.Content
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonService {
    @GET(Endpoints.POKEMON)
    fun getPokemons(
        @Query("limit") limit: Int
    ): Observable<Response<Content>>
}