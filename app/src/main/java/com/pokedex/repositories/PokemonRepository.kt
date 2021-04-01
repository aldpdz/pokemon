package com.pokedex.repositories

import com.pokedex.data.PokemonService

class PokemonRepository(private val pokemonService: PokemonService) : IPokemonRepository {
    override fun retrivePokemons() = pokemonService.getPokemons(151)
}
