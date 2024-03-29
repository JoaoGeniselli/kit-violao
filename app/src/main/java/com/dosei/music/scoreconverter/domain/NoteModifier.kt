package com.dosei.music.scoreconverter.domain

enum class NoteModifier(val noteSuffix: String, val absolutePositionDiff: Int) {
    SHARP("#", 1),
    FLAT("b", -1)
}