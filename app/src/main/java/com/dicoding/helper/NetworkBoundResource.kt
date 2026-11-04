package com.dicoding.helper

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.Loading

        // Load the data from the database first
        val dbSource = this.loadFromDatabase()
        result.addSource(dbSource) { newData ->

            if (shouldFetch(newData)) {
                // Fetch from the network if needed
                fetchFromNetwork()
            } else {
                // Use the loaded data from the database
                result.value = Resource.Success(newData)
            }
        }
    }

    private fun fetchFromNetwork() {
        result.value = Resource.Loading
        Log.d("NetworkBoundResource", "Fetching from network...")

        // Create the network call
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            Log.d("NetworkBoundResource", "API Response: $response")

            when (response) {
                is ApiResponse.Success -> {
                    saveCallResult(response.data) // Save the result to the database
                    // Re-query the database after saving
                    val dbSource = loadFromDatabase()
                    result.addSource(dbSource) { newData ->
                        result.value =
                            Resource.Success(newData) // Emit the new data
                        Log.d("NetworkBoundResource", "Reloaded with new data.")
                    }
                }

                is ApiResponse.Error -> {
                    result.value = Resource.Error(response.error) // Emit the error
                    Log.d("NetworkBoundResource", "API Error: ${response.error}")
                }

            }
        }
    }

    protected abstract fun loadFromDatabase(): LiveData<ResultType>
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
    protected abstract fun saveCallResult(item: RequestType)
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    fun asLiveData(): LiveData<Resource<ResultType>> = result
}