package co.martketing.rickandmortyapp.presentation.characters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import co.martketing.rickandmortyapp.R
import co.martketing.rickandmortyapp.domain.model.Character
import co.martketing.rickandmortyapp.domain.model.CharacterStatus

@Composable
fun CharacterItem(character: Character, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = character.imageUrl,
            contentDescription = character.name,
            placeholder = painterResource(R.drawable.ic_placeholder),
            error = painterResource(R.drawable.ic_image_error),
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = character.status.label(),
                style = MaterialTheme.typography.labelMedium,
                color = character.status.color()
            )
        }
    }
}

private fun CharacterStatus.label(): String = when (this) {
    CharacterStatus.ALIVE -> "Alive"
    CharacterStatus.DEAD -> "Dead"
    CharacterStatus.UNKNOWN -> "Unknown"
}

private fun CharacterStatus.color(): Color = when (this) {
    CharacterStatus.ALIVE -> Color(0xFF97CE4C)
    CharacterStatus.DEAD -> Color(0xFFE74C3C)
    CharacterStatus.UNKNOWN -> Color(0xFF9E9E9E)
}
