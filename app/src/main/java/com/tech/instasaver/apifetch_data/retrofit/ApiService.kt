package com.tech.instasaver.apifetch_data.retrofit

import com.tech.instasaver.apifetch_data.data.model.reelModel.InstaModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

//1
interface ApiService {

    companion object{
        const val BASE_URL = "https://www.instagram.com/"
        const val END_POINT = "?__a=1&__d=dis"
    }
    @GET("reel/{reelId}/$END_POINT")
    suspend fun getReelVideo(@Path("reelId") reelId: String) : Response<InstaModel>

    @GET("p/{photo_id}/$END_POINT")
    suspend fun getPhoto(@Path("photoId") photoId: String) : Response<InstaModel>
}