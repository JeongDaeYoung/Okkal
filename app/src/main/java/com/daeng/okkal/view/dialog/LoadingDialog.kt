package com.daeng.okkal.view.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.daeng.okkal.R

/**
 * Created by JDY on 2024-01-02
 *
 * 로딩 다이얼로그
 */


@Composable
fun LoadingDialog() {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.loading_progress)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Dialog(onDismissRequest = { /*TODO*/ }) {
        Box(modifier = Modifier
            .width(100.dp)
            .height(100.dp)
        ){
            LottieAnimation(
                composition = composition,
                progress = { progress })
        }
    }
}





@Preview
@Composable
fun LoadingPreview() {
    MaterialTheme {
        LoadingDialog()
    }
}