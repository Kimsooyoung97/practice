package com.example.image

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.image.ui.theme.ImageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var selectUri by remember { mutableStateOf<Uri?>(null) }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                selectUri = uri
            }
        )
        selectUri?.let {
            val context = LocalContext.current
            val bitmap =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(context.contentResolver, it)
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                }
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "")
        }
        Button(onClick = {
            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            Text(text = "변경")
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var currentImageIndex by remember { mutableStateOf(0) }

    val imageInfoList = listOf(
        ImageInfo(R.drawable.chel, "당신은 챌린저 입니다."),
        ImageInfo(R.drawable.gma, "당신은 그랜드마스터 입니다."),
        ImageInfo(R.drawable.master, "당신은 마스터 입니다."),
        ImageInfo(R.drawable.dia, "당신은 다이아몬드 입니다."),
        ImageInfo(R.drawable.ple, "당신은 플래티넘 입니다."),
        ImageInfo(R.drawable.gold, "당신은 골드 입니다."),
        ImageInfo(R.drawable.siver, "당신은 실버 입니다."),
        ImageInfo(R.drawable.bronz, "당신은 브론즈 입니다."),
        ImageInfo(R.drawable.ion, "당신은 아이언 입니다."),
    )

    Column(
        modifier = Modifier.size(500.dp,500.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
            Image(
                modifier = modifier.size(300.dp,300.dp).clickable { val randomIndex = (0 until imageInfoList.size).random()
                    currentImageIndex = randomIndex },

                painter = painterResource(id = imageInfoList[currentImageIndex].imageResourceId),
                contentDescription = ""
            )
        Text(
            text = imageInfoList[currentImageIndex].imageText,
            modifier = Modifier.padding(32.dp),
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {

            Button(modifier = Modifier.size(200.dp, 40.dp),
                onClick = {
                    val randomIndex = (0 until imageInfoList.size).random()
                    currentImageIndex = randomIndex
                }
            ) {
                Text("당신의 롤 티어는?")
            }
        }

    }
}

data class ImageInfo(val imageResourceId: Int, val imageText: String)


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImageTheme {
        Greeting("Android")
    }
}