package com.shaya.myprofile.data.mapper

import com.shaya.myprofile.data.remote.dto.UserDto
import com.shaya.myprofile.domain.Activity
import com.shaya.myprofile.domain.Location
import com.shaya.myprofile.domain.Profile
import com.shaya.myprofile.domain.Social
import com.shaya.myprofile.domain.Statistics
import com.shaya.myprofile.domain.User


fun UserDto.toUser(): User {
    return User(
        username = username ?: "",
        name = name ?: "",
        location = Location(
            city = location?.city ?: "",
            country = location?.country ?: ""
        ),
        avatar = avatar ?: "",
        social = Social(
            website = social?.website ?: "",
            profiles = social?.profiles?.map {
                Profile(
                    platform = it.platform ?: "",
                    url = it.url ?: ""
                )
            } ?: emptyList()
        ),
        statistics = Statistics(
            followers = statistics?.followers ?: 0,
            following = statistics?.following ?: 0,
            activity = Activity(
                shots = statistics?.activity?.shots ?: 0,
                collections = statistics?.activity?.collections ?: 0
            )
        )
    )
}
