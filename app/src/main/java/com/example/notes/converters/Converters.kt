package com.example.notes.converters

import java.text.SimpleDateFormat
import java.util.Locale

fun ConverterLongToString(timeLong: Long?) : String {
    val dateFormatt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateLocal = dateFormatt.format(timeLong)
    return dateLocal
}