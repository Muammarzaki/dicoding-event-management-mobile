package com.dicoding.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.dto.DicodingDetailEvent
import com.dicoding.dto.DicodingListEvent
import com.dicoding.dto.Event
import com.dicoding.helper.ApiResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.Call

class EventApi {

    fun requestEvents(
        api: Call<DicodingListEvent>
    ): LiveData<ApiResponse<List<Event>>> = liveData(Dispatchers.IO) {
        Log.d("Function Call", "${api::class.simpleName}")
        try {
            val response = api.execute()
            if (response.isSuccessful) {
                val events = response.body()?.listEvents ?: emptyList()
                emit(ApiResponse.Success(events))
            } else {
                emit(ApiResponse.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("API", "onFailure: ${e.message}")
            emit(ApiResponse.Error("Failed: ${e.message}"))
        }
    }

    fun requestEventDetail(
        api: Call<DicodingDetailEvent>
    ): LiveData<ApiResponse<Event>> = liveData(Dispatchers.IO) {
        Log.d("Function Call", "${api::class.simpleName}")
        try {
            val response = api.execute()
            if (response.isSuccessful) {
                val events =
                    response.body()?.event ?: throw RuntimeException("Data from server null")
                emit(ApiResponse.Success(events))
            } else {
                emit(ApiResponse.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("API", "onFailure: ${e.message}")
            emit(ApiResponse.Error("Failed: ${e.message}"))
        }
    }
}