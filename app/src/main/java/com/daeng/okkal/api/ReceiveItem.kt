package com.daeng.okkal.api

import com.google.gson.annotations.SerializedName

data class ReceiveItem(
    @SerializedName("response") var response: Any,
)