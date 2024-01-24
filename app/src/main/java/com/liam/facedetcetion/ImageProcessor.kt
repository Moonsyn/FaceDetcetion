package com.liam.facedetcetion

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object ImageProcessor {

	private val highAccuracyOpts = FaceDetectorOptions.Builder()
		.setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
		.setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
		.setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
		.build()

	private val detector = FaceDetection.getClient(highAccuracyOpts)

	suspend fun processInputImage(image: InputImage): List<Face> {
		return suspendCoroutine { continuation ->
			detector.process(image).addOnSuccessListener {  faces ->
				continuation.resume(faces)
			}
			.addOnFailureListener {
				throw it
			}
		}
	}
}