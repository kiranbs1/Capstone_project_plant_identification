package com.example.plantification.presentation.mainActivity

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.plantification.R
import com.example.plantification.RequestMultiplePermissions
import com.example.plantification.data.requests.RandomImageJsonReturnItem
import com.example.plantification.presentation.classifiedResults.ClassifiedResultsActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels() //viewmodel for managing data
    private var imageUriState = mutableStateOf<Uri?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.loadHomeImages() // Load home images when activity is created
        setContent {
            // Set up the UI using Jetpack Compose
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
            ) {
                Column {
                    displayLogo() // Display the logo at the top of the screen
                    Text(
                        text = "Popular Plants",
                        style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                    imageTable() // Display the grid of images
                }
                photoSelector() // Display the photo upload button
                takePhoto() // Display the take photo button
                // Request necessary permissions
                RequestMultiplePermissions(
                    permissions = listOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                    )
                )
            }
        }
    }

    /**
     * Register launcher for selecting an image from gallery
     * ActivityResultContracts.GetContent() defines native app to be opened
      */
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        Log.i("plant", "button clicked")
        imageUriState.value = uri // Set the selected image URI
        if (uri != null) {
            // If URI is not null, start ClassifiedResultsActivity with the selected image URI
            val intent = Intent(this@MainActivity, ClassifiedResultsActivity::class.java)
            intent.putExtra("image_uri", uri.toString())
            startActivity(intent)
        }
    }

    /**
     * Composable function to display the button for selecting/uploading an image.
     */
    @Composable
    fun photoSelector() {
        Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        // Launch the image selection process
                        selectImageLauncher.launch("image/*")
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Upload Image")
                }
            }
        }
    }

    /**
     * Composable function to display the button for taking a photo.
     */
    @Composable
    fun takePhoto() {
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            Button(onClick = {
                // Launch the camera to take a photo
                takePicture.launch(createImageCaptureUri(contentResolver))
            }) {
                // Display a camera icon
                Canvas(modifier = Modifier.size(0.dp), onDraw = {
                    drawCircle(color = Color.Gray)
                })
                Icon(Icons.Rounded.PhotoCamera, contentDescription = "take picture icon", Modifier.size(30.dp))
            }
        }
    }

    /**
     * Register launcher for taking a photo
     * ActivityResultContracts.GetContent() defines native app to be opened
     */
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
        if (isSuccessful) {
            val uri = createImageCaptureUri(contentResolver) // Get the URI of the captured image
            val intent = Intent(this@MainActivity, ClassifiedResultsActivity::class.java)
            intent.putExtra("image_uri", uri.toString()) // Pass the image URI to ClassifiedResultsActivity
            startActivity(intent)
        }
    }

    /**
     * Creates a Uri for capturing images.
     *
     * @param contentResolver the content resolver to use for inserting the image
     * @return the Uri for the captured image
     */
    fun createImageCaptureUri(contentResolver: ContentResolver): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "CapturedImage")
            put(MediaStore.Images.Media.DESCRIPTION, "Captured image using Camera")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }

    /**
     * Composable function to display the logo of the application.
     */
    @Composable
    fun displayLogo() {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth(.45f)) {
                // Display the logo image
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "plantification logo",
                    contentScale = ContentScale.Fit,
                )
            }
            Box(modifier = Modifier.padding(130.dp, 80.dp, 0.dp, 0.dp)) {
                // Display the application name
                Text(
                    text = "Plantification",
                    style = TextStyle(color = Color.White, fontSize = 22.sp)
                )
            }
        }
    }

    /**
     * Composable function to display an image frame with text within
     * the frame and a gradiant at the bottom.
     */
    @Composable
    fun imageFrame(contentDescription: String, contentAlignment: Alignment, image: RandomImageJsonReturnItem) {
        // Decode the image from Base64 and create a scaled bitmap
        val decodedString: ByteArray = Base64.decode(image.image[0], Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        val scaledBitmap = Bitmap.createScaledBitmap(decodedByte, 600, 600, false)

        // Display the image inside a Card
        Card(
            modifier = Modifier
                .width(125.dp)
                .height(125.dp)
                .padding(5.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box {
                // Display the image
                Image(
                    bitmap = scaledBitmap.asImageBitmap(),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Fit,
                )
                // Add a gradient overlay
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY =200f
                        )
                    ))
                // Display the name of the image at the bottom
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp), contentAlignment = Alignment.BottomStart) {
                    Text(text = image.name, color = Color.White)
                }
            }
        }
    }

    /**
     * Composable function to display a row of images.
     */
    @Composable
    fun imageRow(images: List<RandomImageJsonReturnItem>) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
            // Display three images in a row
            imageFrame(
                contentDescription = "plant",
                contentAlignment = Alignment.TopStart,
                image = images[0]
            )
            imageFrame(
                contentDescription = "plants",
                contentAlignment = Alignment.TopCenter,
                image = images[1]
            )
            imageFrame(
                contentDescription = "planty",
                contentAlignment = Alignment.TopEnd,
                image = images[2]
            )
        }
    }

    /**
     * Composable function to display the grid of images.
     */
    @Composable
    fun imageTable() {
        Column {
            // Display images in multiple rows
            mainViewModel.state.images?.let {
                imageRow(it.subList(0, 3))
                imageRow(it.subList(3, 6))
                imageRow(it.subList(6, 9))
            }
        }
    }
}
