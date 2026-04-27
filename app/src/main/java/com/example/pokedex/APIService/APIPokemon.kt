package com.example.pokedex.APIService

import com.example.pokedex.Model.EvolutionChainResponse
import com.example.pokedex.Model.PokemonDetailResponse
import com.example.pokedex.Model.PokemonResponse
import com.example.pokedex.Model.PokemonResponseStats
import com.example.pokedex.Model.PokemonSpeciesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface APIPokemon {
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

    @GET("pokemon/{id}")
    suspend fun getPokemonStats(
        @Path("id") id: Int
    ): Response<PokemonResponseStats>

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): Response<PokemonSpeciesResponse>

    @GET
    suspend fun getEvolutionChain(
        @Url url: String
    ): Response<EvolutionChainResponse>

    //dành cho cac pokemon có ID từ 10000 trở đi
    @GET
    suspend fun getPokemonSpeciesByUrl(@Url url: String): Response<PokemonSpeciesResponse>
}