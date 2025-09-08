package com.shaya.myprofile.data.remote.dto

data class UserResponse(
    val user: UserDto? = null
)

data class UserDto(
    val username: String? = null,
    val name: String? = null,
    val location: Location? = null,
    val avatar: String? = null,
    val social: Social? = null,
    val statistics: Statistics? = null
)

data class Location(
    val city: String? = null,
    val country: String? = null
)

data class Social(
    val website: String? = null,
    val profiles: List<Profile>? = null
)

data class Profile(
    val platform: String? = null,
    val url: String? = null
)

data class Statistics(
    val followers: Int? = null,
    val following: Int? = null,
    val activity: Activity? = null
)

data class Activity(
    val shots: Int? = null,
    val collections: Int? = null
)
