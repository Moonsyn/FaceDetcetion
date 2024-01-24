package com.liam.facedetcetion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable

class ImageDetectionActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ImageDetectionScreen()
		}
	}
}

@Composable
private fun ImageDetectionScreen() {

}