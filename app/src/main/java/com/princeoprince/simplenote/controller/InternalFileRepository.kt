package com.princeoprince.simplenote.controller

import android.content.Context
import com.princeoprince.simplenote.model.Note
import java.io.File

class InternalFileRepository(var context: Context) : NoteRepository {
    override fun addNote(note: Note) {
        TODO("Not yet implemented")
    }

    override fun getNote(fileName: String): Note {
        TODO("Not yet implemented")
    }

    override fun deleteNote(fileName: String): Boolean {
        TODO("Not yet implemented")
    }

    private fun noteFile(fileName: String): File = File(noteDirectory(), fileName)

    private fun noteDirectory(): String = context.filesDir.absolutePath

}
