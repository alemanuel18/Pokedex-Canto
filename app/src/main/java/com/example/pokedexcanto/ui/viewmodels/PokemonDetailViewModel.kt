package com.example.pokedexcanto.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexcanto.data.PokemonDetail
import com.example.pokedexcanto.data.PokemonRepository
import com.example.pokedexcanto.data.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PokemonDetailUiState(
    val pokemon: PokemonDetail? = null,
    val weaknesses: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class PokemonDetailViewModel : ViewModel() {
    private val repository = PokemonRepository()

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    fun loadPokemon(pokemonId: Int) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState(isLoading = true)

            when (val result = repository.getPokemonDetail(pokemonId)) {
                is Result.Success -> {
                    val pokemon = result.data
                    _uiState.value = _uiState.value.copy(
                        pokemon = pokemon,
                        isLoading = false
                    )

                    // Cargar debilidades
                    loadWeaknesses(pokemon.types.map { it.type.name })
                }
                is Result.Error -> {
                    _uiState.value = PokemonDetailUiState(
                        isLoading = false,
                        errorMessage = "Error al cargar el Pokémon: ${result.exception.message}"
                    )
                }
            }
        }
    }

    private suspend fun loadWeaknesses(types: List<String>) {
        when (val result = repository.getPokemonWeaknesses(types)) {
            is Result.Success -> {
                _uiState.value = _uiState.value.copy(weaknesses = result.data)
            }
            is Result.Error -> {
                // No actualizamos el error aquí para no sobrescribir el pokemon cargado
                _uiState.value = _uiState.value.copy(
                    weaknesses = emptyList()
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}