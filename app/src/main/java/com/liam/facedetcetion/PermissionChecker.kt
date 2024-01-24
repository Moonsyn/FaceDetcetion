package com.liam.facedetcetion

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat

fun checkSinglePermission(
	context: Context,
	permission: String
): Boolean {
	if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
		Log.d("test5", "권한이 이미 존재합니다.")
		return true
	}
	return false
}

fun checkMultiplePermission(
	context: Context,
	permissions: List<String>
): Boolean {
	permissions.forEach {
		if (ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED)
			return false
	}
	return true
}
