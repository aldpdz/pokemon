package com.pokedex.pokemonList

import androidx.lifecycle.*
import com.pokedex.data.remote.BasicInfo
import com.pokedex.repositories.IPokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class PokemonListFragmentViewModel @Inject constructor(
    private val pokemonRepository: IPokemonRepository
) : ViewModel(){

    companion object{
        const val POKEMON_IMAGES_ENDPOINT = "https://pokeres.bastionbot.org/images/pokemon/"
    }

    private val compositeDisposable = CompositeDisposable()

    private val _pokemons = MutableLiveData<List<BasicInfo>>()
    val pokemons: LiveData<List<BasicInfo>> get() = _pokemons

    init {
        getPokemons()
    }

    private fun getPokemons() {
        compositeDisposable.add(
                pokemonRepository.retrivePokemons()
                        .subscribeOn(Schedulers.io())
                        .map { content ->
                            content.body()?.let {
                                val list = it.results
                                for (pokemon in list) {
                                    val url = pokemon.url
                                    val pokemonIndex = url.split("/")
                                    pokemon.url = POKEMON_IMAGES_ENDPOINT
                                            .plus(pokemonIndex[pokemonIndex.size - 2])
                                            .plus(".png")
                                }
                                return@map list
                            }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            _pokemons.value = it
                        }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}