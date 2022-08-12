package com.princeoprince.simplenote.controller

import android.content.Context
import android.util.Log
import com.princeoprince.simplenote.model.Note
import com.princeoprince.simplenote.utils.isExternalStorageWritable
import com.princeoprince.simplenote.utils.noteFile
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class EncryptedFileRepository(var context: Context) : NoteRepository {

    private val passwordString = "#prince@prince#"
    private val tag = this::class.simpleName

    override fun addNote(note: Note) {
        if (isExternalStorageWritable()) {
            ObjectOutputStream(noteFileOutputStream(note.fileName)).use {
                it.writeObject(encrypt(note.noteText.toByteArray()))
            }
        }
    }

    override fun getNote(fileName: String): Note {
        TODO("Not yet implemented")
    }

    override fun deleteNote(fileName: String): Boolean {
        TODO("Not yet implemented")
    }

    private fun encrypt(plainTextBytes: ByteArray): HashMap<String, ByteArray> {

        val map = HashMap<String, ByteArray>()

        try {
            val random = SecureRandom()
            val salt = ByteArray(256)
            random.nextBytes(salt)

            // Use AES (Advanced Encryption Standard) and PBKDF2 (Password-Based Key Derivation Function)
            val passwordChar = passwordString.toCharArray()
            val pbKeySpec = PBEKeySpec(passwordChar, salt, 1324, 256)
            val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
            val keySpec = SecretKeySpec(keyBytes, "AES")

            // Use IV (Initialisation Vector)
            val ivRandom = SecureRandom()
            val iv = ByteArray(16)
            ivRandom.nextBytes(iv)
            val ivSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypted = cipher.doFinal(plainTextBytes)
            map["salt"] = salt
            map["iv"] = iv
            map["encrypted"] = encrypted

        } catch (e: Exception) {
            Log.e(tag, "Encryption Exception", e)
        }

        return map
    }

    private fun noteFileOutputStream(fileName: String): FileOutputStream =
        FileOutputStream(noteFile(fileName, context.getExternalFilesDir(null).toString()))
}