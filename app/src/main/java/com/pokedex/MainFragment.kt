package com.pokedex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pokedex.databinding.FragmentTopicsBinding

class MainFragment : Fragment(){
    lateinit var binding: FragmentTopicsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicsBinding.inflate(inflater)

        setUpButtons()

        return binding.root
    }

    private fun setUpButtons() {
        binding.btnRxjava.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_FirstFragment)
        }
        binding.btnCustomViews.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_customViewFragment)
        }
    }
}