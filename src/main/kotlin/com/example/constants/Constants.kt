package com.example.constants

import java.util.*
import kotlin.streams.asSequence

fun randomID(): String {
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789"
    return Random().ints(12, 0, source.length).asSequence().map(source::get).joinToString("")
}