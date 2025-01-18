package com.erha.dogsrf.Recordatorios

import java.io.Serializable

data class Reminder(
    val type: String,
    val date: String,
    val time: String,
    val location: String,
    val notes: String
) : Serializable

