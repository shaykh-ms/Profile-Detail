package com.shaya.myprofile.presentation.profile_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaya.myprofile.domain.User
import com.shaya.myprofile.domain.repository.ProfileRepository
import com.shaya.myprofile.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Loading(true))
    val user: StateFlow<Resource<User>> = _user.asStateFlow()

    fun fetchUser() {
        viewModelScope.launch {
            repository.getUserDetails().collect { result ->
                _user.value = result
            }
        }
    }
}
