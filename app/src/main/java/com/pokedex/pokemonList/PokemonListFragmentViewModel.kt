package com.pokedex.pokemonList

import androidx.lifecycle.*
import com.pokedex.data.remote.BasicInfo
import com.pokedex.repositories.IPokemonRepository
import com.pokedex.utils.RemoteStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
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

    fun getPokemons(filter: String? = null) {
        _networkStatus.value = RemoteStatus.LOADING
        compositeDisposable.add(
                pokemonRepository.retrivePokemons()
                        .subscribeOn(Schedulers.io())
                        .map { content ->
                            content.body()?.let {
                                val list = it.results
                                getImageUrl(list)

                                return@map list
                            }
                        }
                        .map {
                            val filterList = mutableListOf<BasicInfo>()
                            for (poke in it){
                                if(filter == null || poke.name.contains(filter, ignoreCase = true)
                                        || filter.isEmpty()){
                                    filterList.add(poke)
                                }
                            }
                            return@map filterList
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

    private fun getDetails(pokemon: BasicInfo) {
        compositeDisposable.add(
                pokemonRepository.getDetails(pokemon.url)
                        .subscribeOn(Schedulers.io())
                        .subscribe ({ response ->
                            response.body()?.let { detail ->
                                var typeString = "-"
                                for (type in detail.types) {
                                    typeString = typeString
                                            .plus(type.type.name)
                                            .plus("-")
                                }
                                pokemon.types = typeString
                            }
                        }, {

                        })
        )
    }

    fun getLast10() {
        _networkStatus.value = RemoteStatus.LOADING
        compositeDisposable.add(
                pokemonRepository.retrivePokemons()
                        .subscribeOn(Schedulers.io())
                        .map { content ->
                            content.body()?.let {
                                val list = it.results
                                getImageUrl(list)
                                return@map list
                            }
                        }
                        .map {
                            val create = createObservable(it)
                            create.observeOn(Schedulers.io())
                        }
                        .subscribe (
                                {
                                    it.subscribeOn(Schedulers.io())
                                            .takeLast(10)
                                            .map { basicInfo ->
                                                Thread.sleep(1000)
                                                basicInfo
                                            }
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe{ basicInfo ->
                                                _networkStatus.value = RemoteStatus.DONE
                                                _pokemons.value = listOf(basicInfo)
                                            }
                                },
                                {
                                    _networkStatus.value = RemoteStatus.ERROR }))
    }

    private fun createObservable(content: List<BasicInfo>) =
        Observable.create<BasicInfo>{ emmiter ->
            content.forEach { basicInfo ->
                emmiter.onNext(basicInfo)
            }
            emmiter.onComplete()
        }


    private fun getImageUrl(list: List<BasicInfo>) {
        for (pokemon in list) {
            val url = pokemon.url
            val pokemonIndex = url.split("/")
            pokemon.urlImg = POKEMON_IMAGES_ENDPOINT
                    .plus(pokemonIndex[pokemonIndex.size - 2])
                    .plus(".png")

            getDetails(pokemon)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}