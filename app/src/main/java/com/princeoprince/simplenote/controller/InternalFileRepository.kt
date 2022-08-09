package com.princeoprince.simplenote.controller

import android.content.Context
import com.princeoprince.simplenote.model.Note
import java.io.File

class InternalFileRepository(var context: Context) : NoteRepository {
    override fun addNote(note: Note) {
        context.openFileOutput(note.fileName, Context.MODE_PRIVATE).use {
            it.write(note.noteText.toByteArray())
        }
    }

    override fun getNote(fileName: String): Note {
        val note = Note(fileName, "")
        context.openFileInput(fileName).use {
            note.noteText = it.bufferedReader().use { it.readText() }
        }
        return note
    }

    override fun deleteNote(fileName: String): Boolean {
        return noteFile(fileName).delete()
    }

    private fun noteFile(fileName: String): File = File(noteDirectory(), fileName)

    private fun noteDirectory(): String = context.filesDir.absolutePath

}
