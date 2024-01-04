package com.daeng.okkal.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorProperty
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.daeng.okkal.R
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

/**
 * Created by JDY on 2023-12-29
 *
 * 색상표 선택 다이얼로그
 */

@Composable
fun ColorPickDialog(onDismissRequest: () -> Unit, onClickedOK: () -> Unit, colorPick: MutableState<Color>) {
    val controller = rememberColorPickerController()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HsvColorPicker(                              // 원형 색상표에서 터치한 포인트의 색상값을 추출하는 뷰 (skydoves/ColorPickerView)
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                        .padding(10.dp),
                    controller = controller,
                    onColorChanged = {                       // 사진을 터치해 색상이 바뀔때마다 호출
                        colorEnvelope: ColorEnvelope ->
                        colorPick.value = colorEnvelope.color
                    })

                Row {
                    Box(                                    // 선택된 색상을 표시해주는 Box
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .background(color = colorPick.value, RoundedCornerShape(10.dp))
                            .border(
                                1.dp,
                                colorResource(id = R.color.gray),
                                RoundedCornerShape(10.dp)
                            )
                    )

                    Column {
                        AlphaSlider(                         // HsvColorPicker에서 추출된 색상값의 투명도를 조절해주는 슬라이더
                            modifier = Modifier
                                .width(250.dp)
                                .padding(10.dp)
                                .height(30.dp),
                            controller = controller)

                        BrightnessSlider(                    // HsvColorPicker에서 추출된 색상값의 명도를 조절해주는 슬라이더
                            modifier = Modifier
                                .width(250.dp)
                                .padding(10.dp)
                                .height(30.dp),
                            controller = controller
                        )
                    }
                }

                Button(
                    onClick = { onClickedOK() },             // 상위뷰로 onClick 함수 전달
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
fun preview() {
    MaterialTheme {
        val previewColor = remember { mutableStateOf(Color.White) }
        ColorPickDialog(onDismissRequest = { /*TODO*/ }, onClickedOK = { /*TODO*/ }, colorPick = previewColor)
    }
}