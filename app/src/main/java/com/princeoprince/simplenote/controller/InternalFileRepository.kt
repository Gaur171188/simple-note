package com.princeoprince.simplenote.controller

import android.content.Context
import com.princeoprince.simplenote.model.Note
import com.princeoprince.simplenote.utils.noteDirectory
import com.princeoprince.simplenote.utils.noteFile

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
        return noteFile(fileName, noteDirectory(context)).delete()
    }
}
