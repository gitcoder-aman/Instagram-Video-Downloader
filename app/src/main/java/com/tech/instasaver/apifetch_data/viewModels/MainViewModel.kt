package com.tech.instasaver.apifetch_data.viewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.instasaver.apifetch_data.data.model.Variable
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

    val response: MutableState<ApiState> = mutableStateOf(ApiState.Empty)
//    var reelId: MutableState<String> = mutableStateOf("")
//        var reelId: State<String> = _reelId //observe in ui


//    val reelsLiveData: LiveData<InstaModel>
//        get() = repository.reelsVideo


    init {
        getInstaVideo()
    }

    private fun getInstaVideo() = viewModelScope.launch {
        Log.d("@@viewModel", "getInstaVideo2: ${Variable.reelId}")
        instaRepository.getInstaVideo(Variable.reelId!!)
            .onStart {
                response.value = ApiState.Loading
            }.catch {
                response.value = ApiState.Failure(it)
            }.collect {
                response.value = ApiState.Success(it)
            }
    }

}