package com.daeng.okkal.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KraApiInterface {
    @GET("Race_Result_total")
    fun reqRaceResult(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo") pageNo: String,
        @Query("numOfRows") numOfRows: String,
        @Query("_type") type: String
    ) : Call<ReceiveRaceResult>
}