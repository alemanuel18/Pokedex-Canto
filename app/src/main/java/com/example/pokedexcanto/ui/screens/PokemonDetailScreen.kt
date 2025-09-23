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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onBackClick: () -> Unit,
    viewModel: PokemonDetailViewModel = viewModel()
) {
    val pokemon by viewModel.pokemonDetail
    val weaknesses by viewModel.weaknesses
    val isLoading by viewModel.isLoading

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemon(pokemonId)
    }

    Scaffold(
        topBar = {
            pokemon?.let { poke ->
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
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            pokemon?.let { poke ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Imágenes del Pokémon
                    PokemonImagesSection(pokemon = poke)

                    // Información básica
                    BasicInfoSection(pokemon = poke)

                    // Debilidades
                    WeaknessesSection(weaknesses = weaknesses)

                    // Estadísticas
                    StatsSection(stats = poke.stats)
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
                text = "Imágenes",
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
                    label = "Frente"
                )
                ImageWithLabel(
                    imageUrl = pokemon.sprites.back_default,
                    label = "Atrás"
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
                    label = "Frente Shiny"
                )
                ImageWithLabel(
                    imageUrl = pokemon.sprites.back_shiny,
                    label = "Atrás Shiny"
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
                text = "Información Básica",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow("Nombre", pokemon.name.replaceFirstChar { it.uppercase() })
            InfoRow("Número", "#${pokemon.id.toString().padStart(3, '0')}")
            InfoRow("Altura", "${pokemon.height / 10.0} m")
            InfoRow("Peso", "${pokemon.weight / 10.0} kg")
            InfoRow("Tipos", pokemon.types.joinToString(", ") {
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
                    text = "Debilidades",
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
                text = "Estadísticas",
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
            progress = (statValue / 255f).coerceAtMost(1f),
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