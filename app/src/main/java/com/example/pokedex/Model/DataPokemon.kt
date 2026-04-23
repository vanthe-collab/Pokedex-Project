package com.example.pokedex.Model

import com.google.gson.annotations.SerializedName

//Dành cho apdater
data class DataPokemon(
    val id: Int,
    val name: String,
    val types: List<String>,
    val imageUrl: String,
)


// === Dành để hứng dữ liệu từ API ===
data class PokemonResponse(
    val results: List<PokemonListItem>
)

data class PokemonListItem(
    val name: String,
    val url: String
)


// 1. Json chứa chi tiết pokemon
data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val types: List<TypeSlot>, // Hứng mảng Hệ bị lồng sâu
    val sprites: Sprites       // Hứng mảng Ảnh bị lồng sâu
) {
    // Rút gọn link ảnh
    fun getOfficialImageUrl(): String {
        return sprites.other?.officialArtwork?.frontDefault ?: sprites.frontDefault
        ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png"
    }

    //Biến mảng Object thành mảng chữ ["Fire", "Flying"]
    fun getSimpleTypes(): List<String> {
        return types.map { it.type.name }
    }
}

// CÁC CLASS PHỤ TRỢ ĐỂ BÓC TÁCH JSON LỒNG NHAU

// Dành cho việc tách ảnh
data class Sprites(
    @SerializedName("front_default") // đề phòng ảnh offical-art ko có trong API, chèn ảnh pixel vào
    val frontDefault: String?, val other: OtherSprites?
)

data class OtherSprites(@SerializedName("official-artwork") val officialArtwork: OfficialArtwork?)
data class OfficialArtwork(@SerializedName("front_default") val frontDefault: String?)

// Dành cho việc bóc tách Hệ
data class TypeSlot(val type: TypeDetail)
data class TypeDetail(val name: String)