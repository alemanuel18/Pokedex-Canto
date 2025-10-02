package com.example.pokedexcanto.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

class PokemonRepository {
    private val api = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonApiService::class.java)

    suspend fun getPokemonList(): Result<List<PokemonListItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPokemonList()
                Result.Success(response.results)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPokemonDetail(id)
                Result.Success(response)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getPokemonWeaknesses(types: List<String>): Result<List<String>> {
        return withContext(Dispatchers.IO) {
            try {
                val weaknesses = mutableSetOf<String>()
                types.forEach { type ->
                    try {
                        val typeDetail = api.getTypeDetail(type)
                        typeDetail.damage_relations.double_damage_from.forEach { weakness ->
                            weaknesses.add(weakness.name)
                        }
                    } catch (e: Exception) {
                    }
                }
                Result.Success(weaknesses.toList())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}