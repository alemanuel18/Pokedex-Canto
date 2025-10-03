package com.example.pokedexcanto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexcanto.data.PokemonListItem
import com.example.pokedexcanto.data.PokemonRepository
import com.example.pokedexcanto.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PokemonListUiState(
    val pokemonList: List<PokemonListItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class PokemonListViewModel : ViewModel() {
    private val repository = PokemonRepository()

    private val _uiState = MutableStateFlow(PokemonListUiState())
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        loadPokemonList()
    }

    fun loadPokemonList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = repository.getPokemonList()) {
                is Result.Success -> {
                    _uiState.value = PokemonListUiState(
                        pokemonList = result.data,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = PokemonListUiState(
                        pokemonList = emptyList(),
                        isLoading = false,
                        errorMessage = "Error al cargar la lista de Pokémon: ${result.exception.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}