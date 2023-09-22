package com.tech.instasaver.apifetch_data.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.instasaver.apifetch_data.repository.InstaRepository
import com.tech.instasaver.apifetch_data.util.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val instaRepository: InstaRepository) :
    ViewModel() {

    val response: MutableState<ApiState> = mutableStateOf(ApiState.Empty)   // storing response data

//    init {
//        Log.d("@@@@main", "8" + response.value)
//        Log.d("@@@@main", "9" + instaRepository)
//        getInstaVideo()
//    }
    fun fetchInstaVideo(reelId: String) {
        Log.d("@@@@main", "8" + response.value)
        Log.d("@@@@main", "9" + instaRepository)
        viewModelScope.launch {
            Log.d("@@viewModel", "getInstaVideo2: $reelId")

            if (reelId != "") {

                instaRepository.getInstaVideo(reelId)
                    .onStart {
                        response.value = ApiState.Loading
                        Log.d("@@@@main", "10" + response.value)
                    }.catch {
                        response.value = ApiState.Failure(it)
                        Log.d("@@@@main", "11" + response.value)
                    }.collect {
                        response.value = ApiState.Success(it)
                        Log.d("@@@@main", "12" + response.value)
                    }
            }
        }
    }
    fun fetchInstaPhoto(photoId: String) {
        Log.d("@@@@main", "8" + response.value)
        Log.d("@@@@main", "9" + instaRepository)
        viewModelScope.launch {
            Log.d("@@viewModel", "getInstaPhoto: $photoId")

            if (photoId != "") {

                instaRepository.getInstaVideo(photoId)
                    .onStart {
                        response.value = ApiState.Loading
                        Log.d("@@@@main", "10" + response.value)
                    }.catch {
                        response.value = ApiState.Failure(it)
                        Log.d("@@@@main", "11" + response.value)
                    }.collect {
                        response.value = ApiState.Success(it)
                        Log.d("@@@@main", "12" + response.value)
                    }
            }
        }
    }
}