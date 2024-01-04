package com.daeng.okkal.view.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.daeng.okkal.R

/**
 * Created by JDY on 2023-10-12
 */
@Composable
fun ColorRecommDialog(
    onDismissRequest: () -> Unit,
    colors: List<Int>,
    selRecommColor: MutableState<Int>,
    onClickedOK: () -> Unit,
    properties: DialogProperties = DialogProperties(),
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Surface(
            modifier = Modifier
                .width(500.dp)
                .height(200.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Color.Transparent,
                        RoundedCornerShape(10.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                LazyRow(
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp)
                        .background(
                            colorResource(id = R.color.light_beige),
                            RoundedCornerShape(10.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(colors) { index, color ->

                        Box(modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .background(Color(color), shape = RoundedCornerShape(10.dp))
                            .border(
                                BorderStroke(
                                    width = if (index == selRecommColor.value) 3.dp else 1.dp,
                                    if (index == selRecommColor.value) androidx.compose.ui.graphics.Color.Red else colorResource(
                                        id = R.color.dark_gray
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    selRecommColor.value = index
                                }
                            )
                        )

                        if (index < colors.size - 1) {
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {onClickedOK()},
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
