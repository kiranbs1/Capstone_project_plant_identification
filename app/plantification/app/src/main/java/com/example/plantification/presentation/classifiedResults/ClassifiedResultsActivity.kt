package com.example.plantification.presentation.classifiedResults

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plantification.data.requests.RandomImageJsonReturnItem
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


@AndroidEntryPoint
class ClassifiedResultsActivity  : ComponentActivity(){

    private val classifiedResultsViewModel: ClassifiedResultsViewModel by viewModels()
//    private var intentState = mutableStateOf<Intent?>(null)
    private lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        intentState.value = getIntent()
        imageUri = Uri.parse(intent.extras?.getString("image_uri"))
        classifiedResultsViewModel.classifyImage(classifiedResultsViewModel.encodeBase64(imageUri, contentResolver))
        val imageBytes = contentResolver.openInputStream(imageUri)?.readBytes()
        val bitmapImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes?.size!!)
        setContent {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                imageFrame(
                    contentDescription = "picture",
                    imageName = "Your Flower",
                    image = bitmapImage,
                    width = 250.dp,
                    height = 250.dp,
                    bitmapW = 900,
                    bitmapH = 900
                )
                resultsImageTable()
            }
        }
    }


    /**
     * Composable function to display an image frame.
     *
     * @param contentDescription Description of the image content.
     * @param imageName Name of the image.
     * @param image Bitmap representing the image.
     * @param width Width of the image frame.
     * @param height Height of the image frame.
     * @param bitmapW Width of the bitmap image.
     * @param bitmapH Height of the bitmap image.
     */
    @Composable
    fun imageFrame(contentDescription : String,  imageName: String, image : Bitmap, width: Dp = 117.dp,
                   height: Dp = 117.dp, bitmapW: Int = 600, bitmapH : Int = 600) {

        val scaledBitmap = Bitmap.createScaledBitmap(image, bitmapW, bitmapH, false)
        Card(
            modifier = Modifier
                .width(width)
                .height(height)
                .padding(5.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box() {
                Image(
                    bitmap = scaledBitmap.asImageBitmap(),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            ),
                            startY =300f
                        )
                    ))
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp), contentAlignment = Alignment.BottomStart) {
                    Text(text = imageName, color = Color.White)
                }
            }
        }
    }

    /**
     * Composable function to display a row of classified images.
     *
     * @param speciesName Name of the species.
     * @param images List of Bitmap images to display
     */
    @Composable
    fun imageRow(speciesName: String, images: List<Bitmap>) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
            imageFrame(
                contentDescription = speciesName + "image",
                imageName = speciesName,
                image = images[0]
            )
            imageFrame(
                contentDescription = speciesName + "image",
                imageName = speciesName,
                image = images[1]
            )
            imageFrame(
                contentDescription = speciesName + "image",
                imageName = speciesName,
                image = images[2]
            )
        }
    }

    /**
     * Composable function to display the results table of classified images.
     */
    @Composable
    fun resultsImageTable() {
        Column {
            // Display classified image results
            classifiedResultsViewModel.state.classifiedResults?.let {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        // Display class one result
                        it.class_one?.let { it1 ->
                            Text(
                                text = it1,
                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        }
                        it.class_one_rating?.let { it1 ->
                            Text(
                                text = it1,
                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    it.class_one?.let { it1 -> imageRow(it1, it.class_one_images) }

                    // Display class two result
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        it.class_two?.let { it1 ->
                            Text(
                                text = it1,
                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        }
                        it.class_two_rating?.let { it1 ->
                            Text(
                                text = it1,
                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    it.class_two?.let { it1 -> imageRow(it1, it.class_two_images) }

                    // Display class three result
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        it.class_three?.let { it1 ->
                            Text(
                                text = it1,
                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                        }
                        it.class_three_rating?.let { it1 ->
                            Text(
                                text = it1,
                                style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                    it.class_three?.let { it1 -> imageRow(it1, it.class_three_images) }
                }
            }
        }
    }
}