package co.martketing.rickandmortyapp.domain.model

data class CharacterPage(
    val characters: List<Character>,
    val hasNextPage: Boolean
)
