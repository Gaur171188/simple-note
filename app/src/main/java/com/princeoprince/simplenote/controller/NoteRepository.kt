package com.princeoprince.simplenote.controller

import com.princeoprince.simplenote.model.Note

interface NoteRepository {
    fun addNote(note: Note)
    fun getNote(fileName: String): Note
    fun deleteNote(fileName: String): Boolean
}
