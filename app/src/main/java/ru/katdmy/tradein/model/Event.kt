package ru.katdmy.tradein.model

import android.graphics.Color
import java.time.LocalDateTime

data class Event(
    val name: String,
    val color: Color,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val description: String? = null
)