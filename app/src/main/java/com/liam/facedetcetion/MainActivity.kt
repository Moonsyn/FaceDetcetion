package com.liam.facedetcetion

import android.Manifest
import android.graphics.ImageDecoder
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MainScreen()
		}
	}
}

@Composable
private fun MainScreen() {
	val context = LocalContext.current

	var imageBitmap by remember { mutableStateOf(ImageBitmap(32, 32)) }
	val faces = remember { mutableStateListOf<Face>() }

	val pickGalleryLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.PickVisualMedia()
	) {
		if (it == null)
			throw NullPointerException()
		val source = ImageDecoder.createSource(context.contentResolver, it)
		imageBitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
			decoder.setTargetSize(1080, 1080)
		}.asImageBitmap()
	}

	val requestPermissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission()
	) { isGranted ->
		if (isGranted) {
			Log.d("test5", "권한이 동의되었습니다.")
		} else {
			Log.d("test5", "권한이 거부되었습니다.")
		}
	}

	LaunchedEffect(imageBitmap) {
		faces.clear()

		val image = InputImage.fromBitmap(imageBitmap.asAndroidBitmap(), 0)
		faces.addAll(ImageProcessor.processInputImage(image))
	}

	Column(
		modifier = Modifier.fillMaxSize()
	) {
		Button(
			onClick = {
				if (checkSinglePermission(context, Manifest.permission.READ_MEDIA_IMAGES)) {
					pickGalleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
				} else {
					requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
				}
			}
		) {
			Text(text = "갤러리에서 불러오기")
		}

		Canvas(
			modifier = Modifier.fillMaxSize()
		) {
			drawImage(
				image = imageBitmap
			)
			faces.forEach { face ->
				val rect = face.boundingBox
				drawRect(
					color = Color.Green,
					style = Stroke(
						width = 2.dp.toPx()
					),
					topLeft = Offset(rect.left.toFloat(), rect.top.toFloat()),
					size = Size(rect.width().toFloat(), rect.height().toFloat()),
				)
			}
		}
	}
}