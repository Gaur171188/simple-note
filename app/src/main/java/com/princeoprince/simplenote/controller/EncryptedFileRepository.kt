package com.princeoprince.simplenote.controller

import android.content.Context
import android.util.Log
import com.princeoprince.simplenote.model.Note
import com.princeoprince.simplenote.utils.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
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
        val note = Note(fileName, "")
        if (isExternalStorageReadable()) {
            ObjectInputStream(noteFileInputStream(note.fileName)).use {
                val mapFromFile= it.readObject() as HashMap<String, ByteArray>
                val decrypted = decrypt(mapFromFile)
                if (decrypted != null) {
                    note.noteText = String(decrypted)
                }
            }
        }
        return note
    }

    override fun deleteNote(fileName: String): Boolean =
        isExternalStorageWritable() &&
                noteFile(fileName, context.getExternalFilesDir(null).toString()).delete()

    private fun encrypt(plainTextBytes: ByteArray): HashMap<String, ByteArray> {

        val map = HashMap<String, ByteArray>()

        try {
            val random = SecureRandom()
            val salt = ByteArray(256)
            random.nextBytes(salt)

            // Use AES (Advanced Encryption Standard) and PBKDF2 (Password-Based Key Derivation Function)
            val passwordChar = passwordString.toCharArray()
            val pbKeySpec = PBEKeySpec(passwordChar, salt, ITERATION_COUNT, KEY_LENGTH)
            val secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM)
            val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
            val keySpec = SecretKeySpec(keyBytes, KEY_SPEC_ALGORITHM)

            // Use IV (Initialisation Vector)
            val ivRandom = SecureRandom()
            val iv = ByteArray(16)
            ivRandom.nextBytes(iv)
            val ivSpec = IvParameterSpec(iv)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypted = cipher.doFinal(plainTextBytes)
            map[SALT] = salt
            map[IV] = iv
            map[ENCRYPTED] = encrypted

        } catch (e: Exception) {
            Log.e(tag, "Encryption Exception", e)
        }

        return map
    }

    private fun decrypt(map: HashMap<String, ByteArray>): ByteArray? {
        var decrypted: ByteArray? = null

        try {
            val salt = map[SALT]
            val iv = map[IV]
            val encrypted = map[ENCRYPTED]
            val passwordChar = passwordString.toCharArray()
            val pbKeySpec = PBEKeySpec(passwordChar, salt, ITERATION_COUNT, KEY_LENGTH)
            var secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM)
            val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
            val keySpec = SecretKeySpec(keyBytes, KEY_SPEC_ALGORITHM)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            decrypted = cipher.doFinal(encrypted)
        } catch (e: Exception) {
            Log.e(tag, "Decryption Exception", e)
        }

        return decrypted
    }

    private fun noteFileOutputStream(fileName: String): FileOutputStream =
        FileOutputStream(noteFile(fileName, context.getExternalFilesDir(null).toString()))

    private fun noteFileInputStream(fileName: String): FileInputStream =
        FileInputStream(noteFile(fileName, context.getExternalFilesDir(null).toString()))
}