package com.pokedex.repositories

import com.pokedex.data.PokemonService
import com.pokedex.data.remote.DetailInfo
import io.reactivex.Observable
import retrofit2.Response

class PokemonRepository(private val pokemonService: PokemonService) : IPokemonRepository {
    override fun retrivePokemons() = pokemonService.getPokemons(151)
    override fun getDetails(url: String) = pokemonService.getPokemonDetails(url)
}
