package com.shaya.myprofile.domain.repository

import com.shaya.myprofile.domain.User
import com.shaya.myprofile.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun getUserDetails(): Flow<Resource<User>>
}