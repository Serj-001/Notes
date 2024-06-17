package com.example.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    var id: Int = 0

    //@ColumnInfo(name = "noteTitle")
    var title: String = ""

    //@ColumnInfo(name = "noteDescription")
    var description: String = ""

    constructor() {}

    constructor(title: String, description: String) {
        this.title = title
        this.description = description
    }
}

