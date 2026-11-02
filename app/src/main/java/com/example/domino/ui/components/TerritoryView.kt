package com.example.domino.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domino.data.model.Territory
import com.example.domino.data.model.TileEffect
import com.example.domino.ui.theme.TileBonus
import com.example.domino.ui.theme.TileFortress
import com.example.domino.ui.theme.TileNeutral
import com.example.domino.ui.theme.TilePortal
import com.example.domino.ui.theme.TileTrap
import com.example.domino.ui.theme.TileTreasure

@Composable
fun TerritoryView(
    territory: Territory,
    ownerColor: Color?,
    onClick: () -> Unit,
    cellSize: Dp = 48.dp
) {
    var backgroundColor = tileEffectColor(territory.effect)
    if (ownerColor != null) {
        backgroundColor = ownerColor
    }

    Box(
        modifier = Modifier
            .size(cellSize)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (territory.isEmpty()) {
            if (territory.effect != TileEffect.NONE) {
                Text(
                    text = territory.effect.label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        } else {
            Text(
                text = territory.number.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (territory.effect == TileEffect.FORTRESS && !territory.isControlled()) {
            Text(
                text = territory.conquests.toString() + "/" + territory.conquestsNeeded().toString(),
                fontSize = 8.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

fun tileEffectColor(effect: TileEffect): Color {
    if (effect == TileEffect.BONUS) {
        return TileBonus
    }
    if (effect == TileEffect.TREASURE) {
        return TileTreasure
    }
    if (effect == TileEffect.TRAP) {
        return TileTrap
    }
    if (effect == TileEffect.FORTRESS) {
        return TileFortress
    }
    if (effect == TileEffect.PORTAL) {
        return TilePortal
    }
    return TileNeutral
}