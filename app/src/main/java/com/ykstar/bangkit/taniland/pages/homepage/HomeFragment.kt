package com.ykstar.bangkit.taniland.pages.homepage

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.adapters.HomeAdapter
import com.ykstar.bangkit.taniland.adapters.LahanAdapter
import com.ykstar.bangkit.taniland.databinding.FragmentHomeBinding
import com.ykstar.bangkit.taniland.pages.startup.AuthActivity
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.viewmodels.HomeViewModel
import com.ykstar.bangkit.taniland.viewmodels.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var userPreferences: UserPreference
    private lateinit var progressDialog: Dialog
    private val weatherViewModel: WeatherViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        userPreferences = UserPreference(requireContext())

        if (!InternetActive.isOnline(requireContext())) {
            InternetActive.showNoInternetDialog(requireContext())
        } else {
            progressDialog.show()
            weatherViewModel.weather.observe(viewLifecycleOwner) { weatherResponse ->
                val tempInCelsius = (weatherResponse.main.temp - 273.15).roundToInt()

                val localeID = Locale("in", "ID")
                val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", localeID)
                sdf.timeZone = TimeZone.getDefault()
                val currentDate = sdf.format(Date(weatherResponse.date * 1000))

                binding.tanggalWeather.text = currentDate
                binding.suhuWeather.text =
                    getString(R.string.suhu_weather, tempInCelsius.toString())
                val weatherDescriptionInEnglish = weatherResponse.weatherList[0].description
                val weatherDescriptionInIndonesian = translateWeatherDescription(weatherDescriptionInEnglish)

                val weatherDescriptionCapitalized = weatherDescriptionInIndonesian.split(" ").joinToString(" ") { it ->
                    it.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                } }
                binding.weatherStatusChip.text = weatherDescriptionCapitalized
            }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    Log.d("Lokasi",location.toString())
                    location?.let {
                        weatherViewModel.fetchWeather(it.latitude, it.longitude)
                    }
                }

            userPreferences.getToken()?.let { viewModel.getUserData(it) }
            viewModel.userResponse.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { userModel ->
                            binding.noData.visibility =
                                if (userModel.lahan.isEmpty()) View.VISIBLE else View.GONE
                            binding.namaUser.text = userModel.username
                            binding.statusUser.text =
                                if (userModel.premium) "Premium" else "Standard"
                            Glide.with(this)
                                .load(userModel.photo)
                                .into(binding.profileImage)

                            binding.lahanHomeRv.apply {
                                layoutManager = LinearLayoutManager(requireContext())
                                adapter = LahanAdapter(userModel.lahan)
                            }
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.silahkan_login_ulang),
                            Toast.LENGTH_LONG
                        ).show()
                        userPreferences.removeUserID()
                        userPreferences.removeToken()
                        val intent = Intent(context, AuthActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            }

            binding.lihatSemuaLahan.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_lahan)
            }
        }
    }

    private fun translateWeatherDescription(desc: String): String {
        return when(desc.lowercase(Locale.getDefault())) {
            "clear sky" -> "Langit Cerah"
            "few clouds" -> "Sedikit Berawan"
            "scattered clouds" -> "Berawan Tersebar"
            "broken clouds" -> "Berawan"
            "shower rain" -> "Hujan Gerimis"
            "rain" -> "Hujan"
            "thunderstorm" -> "Badai Petir"
            "snow" -> "Salju"
            "mist" -> "Kabut"
            else -> desc
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val groupLiveData = GroupLiveData(weatherViewModel.weather, viewModel.userResponse)

        groupLiveData.observe(viewLifecycleOwner) { (weatherResponse, userResponse) ->
            if (weatherResponse != null && userResponse != null) {
                progressDialog.dismiss()
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

class GroupLiveData<T1, T2>(source1: LiveData<T1>, source2: LiveData<T2>) : LiveData<Pair<T1?, T2?>>() {
    private var data1: T1? = null
    private var data2: T2? = null

    init {
        this.value = Pair(data1, data2)

        source1.observeForever {
            data1 = it
            this.value = Pair(data1, data2)
        }

        source2.observeForever {
            data2 = it
            this.value = Pair(data1, data2)
        }
    }
}
