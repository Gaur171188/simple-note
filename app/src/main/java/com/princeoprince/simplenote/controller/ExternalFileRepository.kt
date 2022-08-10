package com.princeoprince.simplenote.controller

import android.content.Context
import android.os.Environment
import com.princeoprince.simplenote.model.Note
import com.princeoprince.simplenote.utils.noteDirectory
import com.princeoprince.simplenote.utils.noteFile
import java.io.FileOutputStream

class ExternalFileRepository(var context: Context) : NoteRepository {
    override fun addNote(note: Note) {
        if (isExternalStorageWritable()) {
            FileOutputStream(noteFile(note.fileName, noteDirectory(context))).use {
                it.write(note.noteText.toByteArray())
            }
        }
    }

    override fun getNote(fileName: String): Note {
        TODO("Not yet implemented")
    }

    override fun deleteNote(fileName: String): Boolean {
        TODO("Not yet implemented")
    }

    private fun isExternalStorageWritable() : Boolean =
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
}