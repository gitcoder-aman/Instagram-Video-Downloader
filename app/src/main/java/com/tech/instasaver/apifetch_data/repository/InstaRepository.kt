package com.tech.instasaver.apifetch_data.repository

import com.tech.instasaver.apifetch_data.data.model.reelModel.InstaModel
import com.tech.instasaver.apifetch_data.retrofit.ApiService
import com.tech.instasaver.apifetch_data.util.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject


//4
class InstaRepository @Inject constructor(private val apiService: ApiService){

    fun getInstaVideo( reelId : String) : Flow<Response<InstaModel>> = flow {
        emit(apiService.getReelVideo(reelId))
    }.flowOn(Dispatchers.IO)

}