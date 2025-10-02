package com.example.pokedexcanto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokedexcanto.data.PokemonDetail
import com.example.pokedexcanto.data.PokemonStat
import com.example.pokedexcanto.ui.viewmodels.PokemonDetailViewModel
import androidx.compose.ui.res.stringResource
import com.example.pokedexcanto.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemon(pokemonId)
    }

    Scaffold(
        topBar = {
            uiState.pokemon?.let { poke ->
                TopAppBar(
                    title = {
                        Column {
                            Text(poke.name.replaceFirstChar { it.uppercase() })
                            Text(
                                text = "#${poke.id.toString().padStart(3, '0')}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        snackbarHost = {
            uiState.errorMessage?.let { error ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.errorMessage != null && uiState.pokemon == null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadPokemon(pokemonId) }) {
                            Text("Reintentar")
                        }
                    }
                }
                uiState.pokemon != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Imágenes del Pokémon
                        PokemonImagesSection(pokemon = uiState.pokemon!!)

                        // Información básica
                        BasicInfoSection(pokemon = uiState.pokemon!!)

                        // Debilidades
                        WeaknessesSection(weaknesses = uiState.weaknesses)

                        // Estadísticas
                        StatsSection(stats = uiState.pokemon!!.stats)
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonImagesSection(pokemon: PokemonDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.images_section_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Imágenes normales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ImageWithLabel(
                    imageUrl = pokemon.sprites.front_default,
                    label = stringResource(R.string.image_front)
                )
                ImageWithLabel(
                    imageUrl = pokemon.sprites.back_default,
                    label = stringResource(R.string.image_back)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imágenes shiny
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ImageWithLabel(
                    imageUrl = pokemon.sprites.front_shiny,
                    label = stringResource(R.string.image_front_shiny)
                )
                ImageWithLabel(
                    imageUrl = pokemon.sprites.back_shiny,
                    label = stringResource(R.string.image_back_shiny)
                )
            }
        }
    }
}

@Composable
fun ImageWithLabel(
    imageUrl: String?,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = label,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentScale = ContentScale.Fit
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun BasicInfoSection(pokemon: PokemonDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.basic_info_section_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(stringResource(R.string.info_name), pokemon.name.replaceFirstChar { it.uppercase() })
            InfoRow(stringResource(R.string.info_number), "#${pokemon.id.toString().padStart(3, '0')}")
            InfoRow(stringResource(R.string.info_height), "${pokemon.height / 10.0} m")
            InfoRow(stringResource(R.string.info_weight), "${pokemon.weight / 10.0} kg")
            InfoRow(stringResource(R.string.info_types), pokemon.types.joinToString(", ") {
                it.type.name.replaceFirstChar { char -> char.uppercase() }
            })
        }
    }
}

@Composable
fun WeaknessesSection(weaknesses: List<String>) {
    if (weaknesses.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.weaknesses_section_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = weaknesses.joinToString(", ") {
                        it.replaceFirstChar { char -> char.uppercase() }
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun StatsSection(stats: List<PokemonStat>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.stats_section_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            stats.forEach { stat ->
                StatBar(
                    statName = stat.stat.name.replaceFirstChar { it.uppercase() },
                    statValue = stat.base_stat
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun StatBar(
    statName: String,
    statValue: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = statName)
            Text(text = statValue.toString())
        }

        LinearProgressIndicator(
            progress = { (statValue / 255f).coerceAtMost(1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Medium
        )
        Text(text = value)
    }
}