package com.shaya.myprofile.data.remote

import com.shaya.myprofile.data.remote.dto.UserResponse
import retrofit2.http.GET

interface ProfileService {

    @GET("data.json")
    suspend fun getUser(): UserResponse

}