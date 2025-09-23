package com.example.pokedexcanto.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonRepository {
    private val api = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonApiService::class.java)

    suspend fun getPokemonList(): List<PokemonListItem> {
        return withContext(Dispatchers.IO) {
            api.getPokemonList().results
        }
    }

    suspend fun getPokemonDetail(id: Int): PokemonDetail {
        return withContext(Dispatchers.IO) {
            api.getPokemonDetail(id)
        }
    }

    suspend fun getPokemonWeaknesses(types: List<String>): List<String> {
        return withContext(Dispatchers.IO) {
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
            weaknesses.toList()
        }
    }
}