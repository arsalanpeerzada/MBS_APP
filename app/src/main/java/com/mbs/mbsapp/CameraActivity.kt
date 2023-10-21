package com.mbs.mbsapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
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
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.mbs.mbsapp.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors


class CameraActivity : AppCompatActivity() {
    lateinit var binding: ActivityCameraBinding
    var capture: ImageButton? = null
    var toggleFlash: ImageButton? = null
    var flipCamera: ImageButton? = null
    var previewView: PreviewView? = null
    var cameraFacing = CameraSelector.LENS_FACING_BACK
    lateinit var cameraController: LifecycleCameraController
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

        capture!!.setOnClickListener {
            takePicture()
        }

    }


    private val activityResultLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            startCamera(cameraFacing)
        }
    }

    /*    fun startCamera(cameraFacing: Int) {
            val aspectRatio = aspectRatio(previewView!!.width, previewView!!.height)
            val listenableFuture = ProcessCameraProvider.getInstance(this)
            listenableFuture.addListener({
                try {
                    val cameraProvider =
                        listenableFuture.get() as ProcessCameraProvider
                    val preview =
                        Preview.Builder().build()
                    preview.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(cameraFacing).build()

                    val imageCapture =
                        ImageCapture.Builder()
                            .setTargetResolution(Size(1080,1920 ))
                            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                            .setTargetRotation(getImageCaptureRotation(cameraProvider, cameraSelector,cameraFacing))
                            .build()

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
        }*/

    fun startCamera(cameraFacing: Int) {

        var preview: PreviewView = binding.cameraPreview
        cameraController = LifecycleCameraController(baseContext)
        cameraController.bindToLifecycle(this)
        if (cameraFacing == CameraSelector.LENS_FACING_BACK)
            cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        else
            cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        preview.controller = cameraController
    }


    fun takePicture() {
        val file = File(getExternalFilesDir(null), System.currentTimeMillis().toString() + ".jpg")
        val outputFileOptions: ImageCapture.OutputFileOptions =
            ImageCapture.OutputFileOptions.Builder(file).build()
        cameraController.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    val photoUri1 = outputFileResults.savedUri
                        ?: Uri.fromFile(File(outputFileResults.savedUri!!.path))

                    val inputStream = contentResolver.openInputStream(photoUri1)
                    val bitmap = BitmapFactory.decodeStream(inputStream)

// Rotate the image
                    val rotationDegrees = 90 // Specify the desired rotation angle
                    val matrix = Matrix()
                    matrix.postRotate(rotationDegrees.toFloat())
                    val rotatedBitmap =
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

                    var photoUri = bitmapToUri(this@CameraActivity, rotatedBitmap)

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

    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        // Get the application's internal storage directory (or any other directory you prefer)
        val cw = ContextWrapper(context)
        val directory = cw.getDir("images", Context.MODE_PRIVATE)

        // Create a unique filename for the image
        val fileName = "image_${System.currentTimeMillis()}.jpg"

        // Create a new file in the directory
        val file = File(directory, fileName)

        try {
            // Open a file output stream and save the Bitmap to the file
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()

            // Return the Uri of the saved image
            return Uri.fromFile(file)

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

}