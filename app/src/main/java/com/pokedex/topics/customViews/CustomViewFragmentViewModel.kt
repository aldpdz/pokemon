package com.pokedex.topics.customViews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pokedex.data.remote.BasicInfo
import com.pokedex.repositories.IPokemonRepositoryCoroutines
import com.pokedex.utils.RemoteStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomViewFragmentViewModel @Inject constructor(
    private val pokemonRepositoryCoroutines: IPokemonRepositoryCoroutines
) : ViewModel() {
    private val _networkStatus = MutableLiveData<RemoteStatus>()
    val networkStatus : LiveData<RemoteStatus> get() = _networkStatus

    private val _pokemons = MutableLiveData<List<BasicInfo>>()
    val pokemons: LiveData<List<BasicInfo>> get() = _pokemons

    init {
        getPokemons()
    }

    private fun getPokemons() {
        _networkStatus.value = RemoteStatus.LOADING
        viewModelScope.launch {
            try {
                _pokemons.value = pokemonRepositoryCoroutines.retrivePokemons()?.results
                _networkStatus.value = RemoteStatus.DONE
            }catch (e: Exception){
                _networkStatus.value = RemoteStatus.DONE
            }
        }
    }
}