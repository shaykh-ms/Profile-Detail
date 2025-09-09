package com.shaya.myprofile.data.repository

import com.shaya.myprofile.data.mapper.toUser
import com.shaya.myprofile.data.remote.ProfileService
import com.shaya.myprofile.domain.User
import com.shaya.myprofile.domain.repository.ProfileRepository
import com.shaya.myprofile.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileService
) : ProfileRepository {
    override suspend fun getUserDetails(): Flow<Resource<User>> = flow {

        emit(Resource.Loading(true))
        try {
            val response = profileApi.getUser()
            if (response.user != null) {
                val userData = response.user.toUser()
                emit(Resource.Success(userData))
            } else {
                emit(Resource.Error("Failed to fetch contacts"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage ?: "Unknown error"}"))
        } finally {
            emit(Resource.Loading(false))
        }
    }
}
