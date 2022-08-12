package com.princeoprince.simplenote.controller

import android.content.Context
import com.princeoprince.simplenote.model.Note
import com.princeoprince.simplenote.utils.isExternalStorageReadable
import com.princeoprince.simplenote.utils.isExternalStorageWritable
import com.princeoprince.simplenote.utils.noteFile
import java.io.FileInputStream
import java.io.FileOutputStream

class ExternalFileRepository(var context: Context) : NoteRepository {
    override fun addNote(note: Note) {
        if (isExternalStorageWritable()) {
            FileOutputStream(noteFile(note.fileName, context.getExternalFilesDir(null).toString()))
                .use { it.write(note.noteText.toByteArray()) }
        }
    }

    override fun getNote(fileName: String): Note {
        val note = Note(fileName, "")
        if (isExternalStorageReadable()) {
            FileInputStream(noteFile(note.fileName, context.getExternalFilesDir(null).toString()))
                .use {note.noteText = it.bufferedReader().use { it.readText() } }
        }
        return note
    }

    override fun deleteNote(fileName: String): Boolean {
        return isExternalStorageWritable() &&
                noteFile(fileName, context.getExternalFilesDir(null).toString()).delete()
    }
}