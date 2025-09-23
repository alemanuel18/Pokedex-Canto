package com.example.pokedexcanto.data

data class PokemonListResponse(
    val results: List<PokemonListItem>
)

data class PokemonListItem(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.split("/").dropLast(1).last().toInt()
}

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSprites,
    val stats: List<PokemonStat>,
    val types: List<PokemonType>
)

data class PokemonSprites(
    val front_default: String?,
    val back_default: String?,
    val front_shiny: String?,
    val back_shiny: String?
)

data class PokemonStat(
    val base_stat: Int,
    val stat: PokemonStatInfo
)

data class PokemonStatInfo(
    val name: String
)

data class PokemonType(
    val type: PokemonTypeInfo
)

data class PokemonTypeInfo(
    val name: String
)

data class TypeDetail(
    val damage_relations: DamageRelations
)

data class DamageRelations(
    val double_damage_from: List<PokemonTypeInfo>
)