package com.pokedex.repositories

import com.pokedex.data.PokemonService
import com.pokedex.data.remote.BasicInfo
import com.pokedex.data.remote.Content
import com.pokedex.topics.rxjava.PokemonListFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class PokemonRepositoryCoroutines(
    private val pokemonService: PokemonService
)
    : IPokemonRepositoryCoroutines{
    override suspend fun retrivePokemons(): Content? {
        val response = pokemonService.getPokemonsCoroutines(150)
        return if(response.isSuccessful){
            val result = response.body()
            result?.let {
                coroutineScope {
                    for (pokemon in result.results) {
                        val url = pokemon.url
                        val pokemonIndex = url.split("/")
                        pokemon.urlImg = PokemonListFragmentViewModel.POKEMON_IMAGES_ENDPOINT
                            .plus(pokemonIndex[pokemonIndex.size - 2])
                            .plus(".png")
                        launch(Dispatchers.IO) { getDetails(pokemon.url, pokemon) }
                    }
                }
            }
            result
        }else {
            null
        }
    }

    override suspend fun getDetails(url: String, pokemon: BasicInfo){
        val response = pokemonService.getPokemonDetailsCoroutines(url)
        if(response.isSuccessful){
            val result = response.body()
            result?.let {
                var typeString = "-"
                for (type in it.types) {
                    typeString = typeString
                        .plus(type.type.name)
                        .plus("-")
                }
                pokemon.types = typeString
            }
        }
    }
}