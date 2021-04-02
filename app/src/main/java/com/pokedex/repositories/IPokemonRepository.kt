package com.pokedex.repositories

import com.pokedex.data.remote.Content
import com.pokedex.data.remote.DetailInfo
import io.reactivex.Observable
import retrofit2.Response

interface IPokemonRepository {
    fun retrivePokemons(): Observable<Response<Content>>
    fun getDetails(url: String): Observable<Response<DetailInfo>>
}