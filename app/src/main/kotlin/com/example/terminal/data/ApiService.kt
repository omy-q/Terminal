package com.example.terminal.data

import com.example.terminal.data.dto.BarsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("aggs/ticker/{stocksTicker}/range/{multiplier}/{timespan}/{from}/{to}")
    suspend fun loadBars(
        @Path("stocksTicker") stocksTicker: String = "AAPL",
        @Path("multiplier") multiplier: Int = 1,
        @Path("timespan") timespan: String = "hour",
        @Path("from") from: String = "2024-01-09",
        @Path("to") to: String = "2025-01-09",
        @Query("adjusted") adjusted: Boolean = true,
        @Query("sort") sort: String = "desc",
        @Query("limit") limit: Int = 50000,
    ): BarsResponse
}
