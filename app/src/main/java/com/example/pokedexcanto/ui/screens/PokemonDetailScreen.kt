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