package com.example.pokedexcanto.ui.viewmodels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedexcanto.data.PokemonDetail
import com.example.pokedexcanto.data.PokemonRepository
import kotlinx.coroutines.launch

class PokemonDetailViewModel : ViewModel() {
    private val repository = PokemonRepository()

    private val _pokemonDetail = mutableStateOf<PokemonDetail?>(null)
    val pokemonDetail: State<PokemonDetail?> = _pokemonDetail

    private val _weaknesses = mutableStateOf<List<String>>(emptyList())
    val weaknesses: State<List<String>> = _weaknesses

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    fun loadPokemon(pokemonId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val pokemon = repository.getPokemonDetail(pokemonId)
                _pokemonDetail.value = pokemon

                // Cargar debilidades
                val types = pokemon.types.map { it.type.name }
                _weaknesses.value = repository.getPokemonWeaknesses(types)
            } catch (e: Exception) {
                // Manejar error
            } finally {
                _isLoading.value = false
            }
        }
    }
}