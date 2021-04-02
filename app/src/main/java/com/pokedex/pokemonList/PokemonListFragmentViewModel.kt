package com.pokedex.pokemonList

import androidx.lifecycle.*
import com.pokedex.data.remote.BasicInfo
import com.pokedex.data.remote.Type
import com.pokedex.repositories.IPokemonRepository
import com.pokedex.utils.RemoteStatus
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

    private val _networkStatus = MutableLiveData<RemoteStatus>()
    val networkStatus : LiveData<RemoteStatus> get() = _networkStatus

    init {
        getPokemons()
    }

    private fun getPokemons() {
        _networkStatus.value = RemoteStatus.LOADING
        compositeDisposable.add(
                pokemonRepository.retrivePokemons()
                        .subscribeOn(Schedulers.io())
                        .map { content ->
                            content.body()?.let {
                                val list = it.results
                                for (pokemon in list) {
                                    val url = pokemon.url
                                    val pokemonIndex = url.split("/")
                                    pokemon.urlImg = POKEMON_IMAGES_ENDPOINT
                                            .plus(pokemonIndex[pokemonIndex.size - 2])
                                            .plus(".png")

                                    pokemonRepository.getDetails(pokemon.url)
                                        .subscribeOn(Schedulers.io())
                                        .subscribe{ response ->
                                            response.body()?.let { detail ->
                                                var typeString = "-"
                                                for(type in detail.types){
                                                    typeString = typeString
                                                        .plus(type.type.name)
                                                        .plus("-")
                                                }
                                                pokemon.types = typeString
                                            }
                                        }
                                }

                                return@map list
                            }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe (
                            {
                                _networkStatus.value = RemoteStatus.DONE
                                _pokemons.value = it },
                            {
                                _networkStatus.value = RemoteStatus.ERROR })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}