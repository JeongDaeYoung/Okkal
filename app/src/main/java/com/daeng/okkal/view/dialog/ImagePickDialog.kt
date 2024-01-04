package com.daeng.okkal.view.dialog

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.createBitmap
import com.daeng.okkal.R
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.ImageColorPicker
import com.github.skydoves.colorpicker.compose.PaletteContentScale
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

/**
 * Created by JDY on 2024-01-01
 */

@Composable
fun ImagePickDialog(onDismissRequest: () -> Unit, onClickedOK: () -> Unit, imagePick: MutableState<Color>, imageBitmap: MutableState<Bitmap>) {
    val controller = rememberColorPickerController()

    val colorHex = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            modifier = Modifier
                .width(500.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(10.dp),
                    controller = controller,
                    paletteImageBitmap = imageBitmap.value.asImageBitmap(),
                    paletteContentScale = PaletteContentScale.FIT,
                    onColorChanged = {
                            colorEnvelope: ColorEnvelope ->
                        imagePick.value = colorEnvelope.color
                        colorHex.value = colorEnvelope.hexCode
                    })

                Row {
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .background(color = imagePick.value, RoundedCornerShape(10.dp))
                            .border(
                                1.dp,
                                colorResource(id = R.color.gray),
                                RoundedCornerShape(10.dp)
                            )
                    )
                    
                    Text(text = colorHex.value)
                }

                Button(
                    onClick = { onClickedOK() },
                    modifier = Modifier
                        .width(80.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.white)),
                    border = BorderStroke(1.dp, colorResource(id = R.color.dark_gray))
                ) {
                    Text(text = stringResource(id = R.string.common_ok))
                }
            }
        }
    }
}


@Preview
@Composable
fun ImagePickDialogPreview() {
    MaterialTheme {
        val previewColor = remember { mutableStateOf(Color.White) }
        val previewBitmap = remember {mutableStateOf(Bitmap.createBitmap(10,20,Bitmap.Config.ARGB_8888))}
        ImagePickDialog(onDismissRequest = { /*TODO*/ }, onClickedOK = { /*TODO*/ }, imagePick = previewColor, previewBitmap)
    }
}