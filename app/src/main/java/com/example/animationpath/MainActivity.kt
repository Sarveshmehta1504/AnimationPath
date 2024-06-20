package com.example.animationpath

import android.graphics.Path
import android.graphics.PathMeasure
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.animationpath.ui.theme.AnimationPathTheme
import kotlin.math.atan2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimationPathTheme {
                AnimationPath()

            }
        }
    }
}

@Composable
fun AnimationPath(){
    val pathPortion = remember {
        androidx.compose.animation.core.Animatable(initialValue = 0f)
    }
    LaunchedEffect(true)
    {
        pathPortion.animateTo(
            targetValue = 1f,
            animationSpec = androidx.compose.animation.core.tween(
                durationMillis = 5000
            )
        )
    }
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(500f,800f)
        quadraticBezierTo(100f,1000f,800f,2000f)
    }
    val outPath = Path()
    val pos = FloatArray(2)
    val tan = FloatArray(2)
    PathMeasure().apply {
        setPath(path.asAndroidPath(), false)
        getSegment(
            0f,
            pathPortion.value * length, outPath,
            true
        )
        getPosTan(
            pathPortion.value * length,
            pos,
            tan
        )

    }
    androidx.compose.foundation.Canvas(
        modifier = Modifier.fillMaxSize()
    ){
        drawPath(
            path = outPath.asComposePath(),
            color = Color.Red,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = 5.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
        val x = pos[0]
        val y = pos[1]
        val degree = -atan2(tan[0], tan[1]) * (180 / Math.PI.toFloat()) -180f
        rotate(degrees = degree, pivot = Offset(x,y)){
            drawPath(
                path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(x,y -30f)
                    lineTo(x-30f,y+60f)
                    lineTo(x+30f,y+60f)
                    close()
                },
                color = Color.Red
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun AnimationPathPreview(){
    AnimationPath()
}
