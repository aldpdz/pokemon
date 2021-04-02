package com.pokedex.data.remote

data class DetailInfo(
    val types: List<TypeInfo>,
    val height: Int,
    val weight: Int
//    val sprites: List<Sprites>
    )