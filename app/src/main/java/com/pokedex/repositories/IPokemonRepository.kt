package com.pokedex.repositories

import com.pokedex.data.remote.Content
import io.reactivex.Observable
import retrofit2.Response

interface IPokemonRepository {
    fun retrivePokemons(): Observable<Response<Content>>
}