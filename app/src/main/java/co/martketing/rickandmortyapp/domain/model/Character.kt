package co.martketing.rickandmortyapp.domain.model

data class Character(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val gender: String,
    val imageUrl: String,
    val originName: String,
    val locationName: String
)
