package com.pokedex.pokemonList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.pokedex.R
import com.pokedex.databinding.FragmentPokemonListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListFragmentViewModel by viewModels()
    lateinit var adapter: PokemonAdapter
    lateinit var binding: FragmentPokemonListBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PokemonAdapter()
        view.findViewById<RecyclerView>(R.id.rv_pokemon).adapter = adapter

        viewModel.pokemons.observe(viewLifecycleOwner, {
            it?.let {
                adapter.dataSet = it
            }
        })

        binding.btnSearch.setOnClickListener {
            viewModel.getPokemons(binding.etFilter.text.toString())
        }

        binding.btnLast10.setOnClickListener {
            viewModel.getLast10()
        }
    }
}