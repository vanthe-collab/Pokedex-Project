package com.example.pokedex.APIService

import com.example.pokedex.Model.PokemonDetailResponse
import com.example.pokedex.Model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface Apipokemon {
    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2/"
    }

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonResponse>

    //dùng để lấy url từ pokemon chi tiết
    @GET
    suspend fun getPokemonDetail(@Url url: String): Response<PokemonDetailResponse>
}