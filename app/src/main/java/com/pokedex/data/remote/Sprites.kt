package com.pokedex.data.remote

import com.squareup.moshi.Json

data class Sprites (
    @Json(name = "front_default")
    val frontDefault: String)