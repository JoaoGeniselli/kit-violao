package com.dosei.music.scoreconverter.converter

import com.dosei.music.scoreconverter.GuitarPositions

data class CurrentNote(
    val name: String,
    val scorePosition: Int,
    val tablaturePositions: GuitarPositions
)