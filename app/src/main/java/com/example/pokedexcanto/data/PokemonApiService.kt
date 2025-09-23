package com.example.pokedexcanto.data

import retrofit2.http.GET
import retrofit2.http.Path


interface PokemonApiService {
    @GET("pokemon?limit=151")
    suspend fun getPokemonList(): PokemonListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): PokemonDetail

    @GET("type/{type}")
    suspend fun getTypeDetail(@Path("type") type: String): TypeDetail
}