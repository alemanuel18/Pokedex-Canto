package com.example.pokedexcanto.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexcanto.data.PokemonListItem
import com.example.pokedexcanto.data.PokemonRepository
import kotlinx.coroutines.launch

class PokemonListViewModel : ViewModel() {
    private val repository = PokemonRepository()

    private val _pokemonList = mutableStateOf<List<PokemonListItem>>(emptyList())
    val pokemonList: State<List<PokemonListItem>> = _pokemonList

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadPokemonList()
    }

    private fun loadPokemonList() {
        viewModelScope.launch {
            try {
                _pokemonList.value = repository.getPokemonList()
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.value = false
            }
        }
    }
}