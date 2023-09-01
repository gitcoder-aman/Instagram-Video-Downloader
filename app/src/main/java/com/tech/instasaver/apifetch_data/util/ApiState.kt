package com.tech.instasaver.apifetch_data.util

import com.tech.instasaver.apifetch_data.data.model.reelModel.InstaModel
import retrofit2.Response

//5
sealed class ApiState{
     class Success(val data : Response<InstaModel>) : ApiState()
     class Failure(val msg:Throwable) : ApiState()
    object Loading : ApiState()
    object Empty : ApiState()
}
