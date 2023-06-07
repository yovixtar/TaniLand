package com.ykstar.bangkit.taniland.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykstar.bangkit.taniland.models.AuthResponse
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.repositories.UserRepository
import com.ykstar.bangkit.taniland.utils.Resource
import kotlinx.coroutines.launch

class UserViewModel(context: Context) : ViewModel() {
    private val userRepository = UserRepository()
    private val userPreferences = UserPreference(context)
    private val _authenticationState = MutableLiveData<Resource<AuthResponse>>()
    val authenticationState: LiveData<Resource<AuthResponse>>
        get() = _authenticationState

    fun authenticate(username: String?, email: String?) {
        viewModelScope.launch {
            val resource = userRepository.authenticate(username, email)
            _authenticationState.value = resource
            if (resource is Resource.Success) {
                resource.data?.data?.id?.let { userPreferences.saveUserID(it) }
                resource.data?.data?.token?.let { userPreferences.saveToken(it) }
            }
        }
    }
}
