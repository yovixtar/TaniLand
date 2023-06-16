package com.ykstar.bangkit.taniland.pages.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ykstar.bangkit.taniland.R
import com.ykstar.bangkit.taniland.databinding.FragmentProfileBinding
import com.ykstar.bangkit.taniland.pages.premium.PlanPremiumActivity
import com.ykstar.bangkit.taniland.pages.startup.AuthActivity
import com.ykstar.bangkit.taniland.preferences.UserPreference
import com.ykstar.bangkit.taniland.utils.InternetActive
import com.ykstar.bangkit.taniland.utils.Resource
import com.ykstar.bangkit.taniland.utils.showPrimaryToast
import com.ykstar.bangkit.taniland.viewmodels.HomeViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var userPreferences: UserPreference
    private lateinit var progressDialog: Dialog

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        userPreferences = UserPreference(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = Dialog(requireContext())
        progressDialog.setContentView(R.layout.dialog_progress)
        progressDialog.setCancelable(false)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.buttonLaporan.setOnClickListener {
            val intent = Intent(requireContext(), PlanPremiumActivity::class.java)
            startActivity(intent)
        }

        if (!InternetActive.isOnline(requireContext())) {
            InternetActive.showNoInternetDialog(requireContext())
        } else {
            progressDialog.show()
            userPreferences.getToken()?.let { viewModel.getUserData(it) }
            viewModel.userResponse.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { userModel ->
                            binding.username.text = userModel.username
                            binding.statusUser.text =
                                if (userModel.premium) getString(R.string.premium) else getString(R.string.standard)
                            binding.email.text = userModel.email
                            Glide.with(this)
                                .load(userModel.photo)
                                .into(binding.profileImage)
                            progressDialog.dismiss()
                        }

                    }

                    is Resource.Error -> {
                        context?.showPrimaryToast(getString(R.string.silahkan_login_ulang), false)
                        userPreferences.removeUserID()
                        userPreferences.removeToken()
                        progressDialog.dismiss()
                        val intent = Intent(context, AuthActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }
            }

            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            auth = Firebase.auth

            binding.logoutButton.setOnClickListener {
                userPreferences.removeUserID()
                userPreferences.removeToken()
                auth.signOut()
                googleSignInClient.signOut().addOnCompleteListener {
                    val intent = Intent(context, AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}