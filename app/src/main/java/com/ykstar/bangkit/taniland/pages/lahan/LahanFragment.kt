package com.ykstar.bangkit.taniland.pages.lahan

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.adapters.LahanAdapter
import com.ykstar.bangkit.taniland.databinding.FragmentLahanBinding
import com.ykstar.bangkit.taniland.models.LahanResponse
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.viewmodels.LahanViewModel

class LahanFragment : Fragment() {

    private lateinit var binding: FragmentLahanBinding
    private lateinit var viewModel: LahanViewModel
    private lateinit var userPreferences: UserPreference
    private lateinit var progressDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLahanBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[LahanViewModel::class.java]
        userPreferences = UserPreference(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (!InternetActive.isOnline(requireContext())) {
            InternetActive.showNoInternetDialog(requireContext())
        } else {
            progressDialog.show()
            userPreferences.getToken()?.let { viewModel.getLahanData(it) }
            viewModel.lahanState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is LahanViewModel.LahanState.Success<*> -> {
                        when (val resource = state.data) {
                            is Resource.Success<*> -> {
                                when (val data = resource.data) {
                                    is LahanResponse -> {
                                        binding.noData.visibility =
                                            if (data.data.isEmpty()) View.VISIBLE else View.GONE
                                        binding.lahanRecyclerView.apply {
                                            layoutManager = LinearLayoutManager(requireContext())
                                            adapter = LahanAdapter(data.data)
                                        }
                                        progressDialog.dismiss()
                                    }
                                }
                            }

                            is Resource.Error<*> -> {

                                progressDialog.dismiss()
                            }
                        }
                    }

                    is LahanViewModel.LahanState.Error -> {

                        progressDialog.dismiss()
                    }
                }
            }
        }

    }
}
