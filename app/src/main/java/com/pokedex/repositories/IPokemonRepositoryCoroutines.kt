package com.pokedex.repositories

import com.pokedex.data.remote.BasicInfo
import com.pokedex.data.remote.Content

interface IPokemonRepositoryCoroutines {
    suspend fun retrivePokemons(): Content?
    suspend fun getDetails(url: String, pokemon: BasicInfo)
}