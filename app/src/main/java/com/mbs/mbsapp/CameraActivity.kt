package com.mbs.mbsapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.mbs.mbsapp.databinding.ActivityCameraBinding
import java.io.File
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors


class CameraActivity : AppCompatActivity() {
    lateinit var binding: ActivityCameraBinding
    var capture: ImageButton? = null
    var toggleFlash: ImageButton? = null
    var flipCamera: ImageButton? = null
    var previewView: PreviewView? = null
    var cameraFacing = CameraSelector.LENS_FACING_BACK
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)


        previewView = binding.cameraPreview
        capture = binding.capture
        toggleFlash = binding.toggleFlash
        flipCamera = binding.flipCamera
        var cameraFacing = CameraSelector.LENS_FACING_BACK
        if (ContextCompat.checkSelfPermission(
                this@CameraActivity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activityResultLauncher.launch(Manifest.permission.CAMERA)
        } else {
            startCamera(cameraFacing)
        }

        flipCamera!!.setOnClickListener(View.OnClickListener {
            cameraFacing = if (cameraFacing == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            startCamera(cameraFacing)
        })

    }


    private val activityResultLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            startCamera(cameraFacing)
        }
    }

    fun startCamera(cameraFacing: Int) {
        val aspectRatio = aspectRatio(previewView!!.width, previewView!!.height)
        val listenableFuture = ProcessCameraProvider.getInstance(this)
        listenableFuture.addListener({
            try {
                val cameraProvider =
                    listenableFuture.get() as ProcessCameraProvider
                val preview =
                    Preview.Builder().setTargetAspectRatio(aspectRatio).build()
                val imageCapture =
                    ImageCapture.Builder()
                        .setTargetResolution(Size(1080,1920 ))
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        //.setTargetRotation(windowManager.defaultDisplay.rotation)
                        .build()
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraFacing).build()
                cameraProvider.unbindAll()
                val camera: Camera =
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                capture!!.setOnClickListener {

                    takePicture(imageCapture)

                }
                toggleFlash!!.setOnClickListener { setFlashIcon(camera) }
                preview.setSurfaceProvider(previewView!!.surfaceProvider)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }



    fun takePicture(imageCapture: ImageCapture) {
        val file = File(getExternalFilesDir(null), System.currentTimeMillis().toString() + ".jpg")
        val outputFileOptions: ImageCapture.OutputFileOptions =
            ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(
            outputFileOptions,
            Executors.newCachedThreadPool(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    val photoUri = outputFileResults.savedUri ?: Uri.fromFile(File(outputFileResults.savedUri!!.path))
                    val resultIntent = Intent()
                    resultIntent.putExtra("imageUri", photoUri.toString())
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()

//                    runOnUiThread {
//                        Toast.makeText(
//                            this@CameraActivity,
//                            "Image saved at: " + file.getPath(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        this@CameraActivity.finish()
//                    }
                    // startCamera(cameraFacing)
                }

                override fun onError(exception: ImageCaptureException) {
                    runOnUiThread {
                        Toast.makeText(
                            this@CameraActivity,
                            "Failed to save: " + exception.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    startCamera(cameraFacing)
                }
            })
    }

    private fun setFlashIcon(camera: Camera) {
        if (camera.getCameraInfo().hasFlashUnit()) {
            if (camera.getCameraInfo().getTorchState().getValue() === 0) {
                camera.getCameraControl().enableTorch(true)
                toggleFlash!!.setImageResource(R.drawable.baseline_flash_off_24)
            } else {
                camera.getCameraControl().enableTorch(false)
                toggleFlash!!.setImageResource(R.drawable.baseline_flash_on_24)
            }
        } else {
            runOnUiThread {
                Toast.makeText(
                    this@CameraActivity,
                    "Flash is not available currently",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = Math.max(width, height).toDouble() / Math.min(width, height)
        return if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            AspectRatio.RATIO_4_3
        } else AspectRatio.RATIO_16_9
    }


//    private fun aspectRatio(width: Int, height: Int): Int {
//        val portraitRatio = Math.max(height, width).toDouble() / Math.min(height, width)
//        return if (Math.abs(portraitRatio - 3.0 / 4.0) <= Math.abs(portraitRatio - 9.0 / 16.0)) {
//            AspectRatio.RATIO_4_3
//        } else AspectRatio.RATIO_16_9
//    }

//    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
//        val matrix = Matrix()
//        matrix.postRotate(angle)
//        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
//    }
}