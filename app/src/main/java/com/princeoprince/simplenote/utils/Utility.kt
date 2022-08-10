package com.princeoprince.simplenote.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import java.io.File

fun Activity.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

fun noteFile(fileName: String, dir: String): File = File(dir, fileName)

fun noteDirectory(context: Context): String = context.filesDir.absolutePath
