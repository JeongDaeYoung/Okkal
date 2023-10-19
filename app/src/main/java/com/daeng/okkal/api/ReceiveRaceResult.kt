package com.daeng.okkal.api

import com.google.gson.annotations.SerializedName

data class ReceiveRaceResult(
    @SerializedName("response") var response: ReceiveRaceResultResponse
)

data class ReceiveRaceResultResponse(
    @SerializedName("header") var header: ReceiveRaceResultHeader,
    @SerializedName("body") var body: ReceiveRaceResultBody
)


data class ReceiveRaceResultHeader(
    @SerializedName("resultCode") var resultCode: String,
    @SerializedName("resultMsg") var resultMsg: String
)

data class ReceiveRaceResultBody(
    @SerializedName("items") var items: ReceiveRaceResultItems
)

data class ReceiveRaceResultItems(
    @SerializedName("item") var item: ArrayList<ReceiveRaceResultItem>
)

data class ReceiveRaceResultItem(
    @SerializedName("age") var age: Int,                      //
    @SerializedName("hrName") var name: String,               // 마명
    @SerializedName("name") var country: String,              // 출신국가가)
)