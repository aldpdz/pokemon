package com.pokedex.topics.customViews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pokedex.databinding.FragmentCustomViewBinding
import com.pokedex.topics.rxjava.PokemonAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomViewFragment : Fragment() {
    private val viewModel: CustomViewFragmentViewModel by viewModels()
    lateinit var adapter: PokemonAdapter
    lateinit var binding: FragmentCustomViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomViewBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PokemonAdapter()
        binding.rvPokemon.adapter = adapter

        viewModel.pokemons.observe(viewLifecycleOwner, {
            it?.let {
                adapter.dataSet = it
            }
        })
    }
}