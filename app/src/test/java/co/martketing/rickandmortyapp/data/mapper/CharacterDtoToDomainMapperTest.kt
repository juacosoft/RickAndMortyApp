package co.martketing.rickandmortyapp.data.mapper

import co.martketing.rickandmortyapp.data.remote.dto.CharacterDto
import co.martketing.rickandmortyapp.data.remote.dto.CharacterLocationDto
import co.martketing.rickandmortyapp.data.remote.dto.CharacterOriginDto
import co.martketing.rickandmortyapp.domain.model.CharacterStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterDtoToDomainMapperTest {

    private fun buildDto(status: String = "Alive") = CharacterDto(
        id = 1,
        name = "Rick Sanchez",
        status = status,
        species = "Human",
        type = "",
        gender = "Male",
        origin = CharacterOriginDto(name = "Earth", url = ""),
        location = CharacterLocationDto(name = "Citadel of Ricks", url = ""),
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        url = "https://rickandmortyapi.com/api/character/1"
    )

    @Test
    fun `toDomain maps all fields correctly`() {
        val dto = buildDto("Alive")
        val character = dto.toDomain()

        assertEquals(1, character.id)
        assertEquals("Rick Sanchez", character.name)
        assertEquals(CharacterStatus.ALIVE, character.status)
        assertEquals("Human", character.species)
        assertEquals("Male", character.gender)
        assertEquals("https://rickandmortyapi.com/api/character/avatar/1.jpeg", character.imageUrl)
        assertEquals("Earth", character.originName)
        assertEquals("Citadel of Ricks", character.locationName)
    }

    @Test
    fun `toDomain maps Alive status correctly`() {
        assertEquals(CharacterStatus.ALIVE, buildDto("Alive").toDomain().status)
    }

    @Test
    fun `toDomain maps Dead status correctly`() {
        assertEquals(CharacterStatus.DEAD, buildDto("Dead").toDomain().status)
    }

    @Test
    fun `toDomain maps unknown string to UNKNOWN status`() {
        assertEquals(CharacterStatus.UNKNOWN, buildDto("unknown").toDomain().status)
    }

    @Test
    fun `toDomain maps unrecognized status string to UNKNOWN`() {
        assertEquals(CharacterStatus.UNKNOWN, buildDto("whatever").toDomain().status)
    }
}
