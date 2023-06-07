package com.ykstar.bangkit.taniland.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ykstar.bangkit.taniland.models.AuthResponse
import com.ykstar.bangkit.taniland.models.UserHomeResponse
import com.ykstar.bangkit.taniland.models.UserModel
import com.ykstar.bangkit.taniland.models.WeatherResponse
import com.ykstar.bangkit.taniland.repositories.UserRepository
import com.ykstar.bangkit.taniland.repositories.WeatherRepository
import com.ykstar.bangkit.taniland.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _userResponseState = MutableLiveData<Resource<UserModel>>()
    val userResponse: LiveData<Resource<UserModel>>
        get() = _userResponseState
    private val userRepository = UserRepository()

    fun getUserData(token: String) {
        viewModelScope.launch {
            val resource = userRepository.getUserHome(token)
            _userResponseState.value = resource
        }
    }
}

class WeatherViewModel: ViewModel() {
    private val repository: WeatherRepository = WeatherRepository()
    private val _weather = MutableLiveData<WeatherResponse>()
    val weather: LiveData<WeatherResponse> get() = _weather

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            val response = repository.getWeather(lat, lon)
            _weather.value = response
        }
    }
}